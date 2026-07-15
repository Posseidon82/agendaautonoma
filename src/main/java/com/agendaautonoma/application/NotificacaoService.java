package com.agendaautonoma.application;

import com.agendaautonoma.domain.Agendamento;
import com.agendaautonoma.domain.Notificacao;
import com.agendaautonoma.domain.enums.TipoNotificacao;
import com.agendaautonoma.infrastructure.mail.EmailNotificacaoStrategy;
import com.agendaautonoma.infrastructure.persistence.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final EmailNotificacaoStrategy emailStrategy;

    @Transactional
    public Notificacao criarNotificacao(Agendamento agendamento, TipoNotificacao tipo) {
        String conteudo = gerarConteudo(agendamento, tipo);
        String destinatario = agendamento.getClienteEmail();

        Notificacao notificacao = Notificacao.builder()
                .agendamentoId(agendamento.getId())
                .tipo(tipo)
                .destinatarioEmail(destinatario)
                .conteudo(conteudo)
                .enviada(false)
                .build();

        Notificacao salva = notificacaoRepository.save(notificacao);

        // Disparo imediato (simples)
        enviar(salva);

        return salva;
    }

    public void enviar(Notificacao notificacao) {
        emailStrategy.enviar(notificacao);
        notificacao.setEnviada(true);
        notificacao.setDataEnvio(LocalDateTime.now());
        notificacaoRepository.save(notificacao);
    }

    public List<Notificacao> listarPorAgendamento(Long agendamentoId) {
        return notificacaoRepository.findByAgendamentoId(agendamentoId);
    }

    public List<Notificacao> listarNaoEnviadas() {
        return notificacaoRepository.findByEnviadaFalse();
    }

    private String gerarConteudo(Agendamento agendamento, TipoNotificacao tipo) {
        return switch (tipo) {
            case CONFIRMACAO -> String.format(
                    "Seu agendamento com o profissional ID %d foi confirmado para %s às %s. Duração: até %s.",
                    agendamento.getProfissionalId(), agendamento.getData(),
                    agendamento.getHoraInicio(), agendamento.getHoraFim()
            );
            case LEMBRETE -> String.format(
                    "Lembrete: você tem um agendamento amanhã, %s às %s.",
                    agendamento.getData(), agendamento.getHoraInicio()
            );
            case CANCELAMENTO -> String.format(
                    "Seu agendamento com o profissional ID %d para %s foi cancelado.",
                    agendamento.getProfissionalId(), agendamento.getData()
            );
        };
    }
}