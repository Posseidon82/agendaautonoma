package com.agendaautonoma.application;

import com.agendaautonoma.domain.Agendamento;
import com.agendaautonoma.domain.Usuario;
import com.agendaautonoma.domain.enums.StatusAgendamento;
import com.agendaautonoma.domain.enums.TipoNotificacao;
import com.agendaautonoma.infrastructure.persistence.AgendamentoRepository;
import com.agendaautonoma.interfaces.dto.AgendamentoRequest;
import com.agendaautonoma.interfaces.dto.ReagendamentoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioService usuarioService;
    private final ConflitoValidator conflitoValidator;
    private final NotificacaoService notificacaoService;

    @Transactional
    public Agendamento criarAgendamento(AgendamentoRequest request) {
        Usuario profissional = usuarioService.buscarPorId(request.getProfissionalId());
        Usuario cliente = usuarioService.getUsuarioLogado();

        // Calcular hora fim baseado na duração do profissional
        var horaFim = request.getHoraInicio()
                .plusMinutes(profissional.getDuracaoPadraoMinutos());

        // Validar conflito
        if (conflitoValidator.existeConflito(profissional.getId(), request.getData(),
                request.getHoraInicio(), horaFim)) {
            throw new RuntimeException("Horário indisponível");
        }

        Agendamento agendamento = Agendamento.builder()
                .profissionalId(profissional.getId())
                .clienteId(cliente.getId())
                .clienteNome(cliente.getNome())
                .clienteEmail(cliente.getEmail())
                .data(request.getData())
                .horaInicio(request.getHoraInicio())
                .horaFim(horaFim)
                .status(StatusAgendamento.CONFIRMADO)
                .build();

        Agendamento salvo = agendamentoRepository.save(agendamento);

        // Gerar notificação de confirmação
        notificacaoService.criarNotificacao(salvo, TipoNotificacao.CONFIRMACAO);

        return salvo;
    }

    @Transactional
    public void cancelarAgendamento(Long id, Long usuarioId) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        // Verificar permissão: só o cliente ou o profissional podem cancelar
        if (!usuario.getId().equals(agendamento.getClienteId()) &&
                !usuario.getId().equals(agendamento.getProfissionalId())) {
            throw new RuntimeException("Usuário não autorizado a cancelar este agendamento");
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamentoRepository.save(agendamento);

        // Gerar notificação de cancelamento
        notificacaoService.criarNotificacao(agendamento, TipoNotificacao.CANCELAMENTO);
    }

    @Transactional
    public Agendamento reagendar(Long id, ReagendamentoRequest request, Long usuarioId) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        if (!usuario.getId().equals(agendamento.getClienteId())) {
            throw new RuntimeException("Apenas o cliente pode reagendar");
        }

        Usuario profissional = usuarioService.buscarPorId(agendamento.getProfissionalId());

        // Calcular novo horário fim
        var novaHoraFim = request.getNovaHoraInicio()
                .plusMinutes(profissional.getDuracaoPadraoMinutos());

        // Validar conflito no novo horário
        if (conflitoValidator.existeConflito(profissional.getId(), request.getNovaData(),
                request.getNovaHoraInicio(), novaHoraFim)) {
            throw new RuntimeException("Horário indisponível para reagendamento");
        }

        agendamento.setData(request.getNovaData());
        agendamento.setHoraInicio(request.getNovaHoraInicio());
        agendamento.setHoraFim(novaHoraFim);
        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        agendamento.setAtualizadoEm(LocalDateTime.now());

        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listarMeusAgendamentos(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId);
    }

    public List<Agendamento> listarAgendamentosRecebidos(Long profissionalId) {
        return agendamentoRepository.findByProfissionalId(profissionalId);
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
    }
}