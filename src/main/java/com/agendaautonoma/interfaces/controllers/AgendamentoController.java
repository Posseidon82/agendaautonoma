package com.agendaautonoma.interfaces.controllers;

import com.agendaautonoma.application.AgendamentoService;
import com.agendaautonoma.application.SlotService;
import com.agendaautonoma.application.UsuarioService;
import com.agendaautonoma.domain.Agendamento;
import com.agendaautonoma.domain.Usuario;
import com.agendaautonoma.domain.enums.StatusAgendamento;
import com.agendaautonoma.infrastructure.persistence.AgendamentoRepository;
import com.agendaautonoma.interfaces.dto.AgendamentoRequest;
import com.agendaautonoma.interfaces.dto.AgendamentoResponse;
import com.agendaautonoma.interfaces.dto.ReagendamentoRequest;
import com.agendaautonoma.interfaces.dto.SlotDisponivel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final UsuarioService usuarioService;
    private final SlotService slotService;
    private final AgendamentoRepository agendamentoRepository;

    @GetMapping("/profissionais/{id}/slots")
    public ResponseEntity<List<SlotDisponivel>> consultarSlots(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        Usuario profissional = usuarioService.buscarPorId(id);
        List<Agendamento> ocupados = agendamentoRepository
                .findByProfissionalIdAndDataAndStatus(id, data, StatusAgendamento.CONFIRMADO);
        List<SlotDisponivel> slots = slotService.calcularSlots(profissional, data, ocupados);
        return ResponseEntity.ok(slots);
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criarAgendamento(
            @Valid @RequestBody AgendamentoRequest request) {
        Agendamento agendamento = agendamentoService.criarAgendamento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AgendamentoResponse(agendamento));
    }

    @GetMapping("/meus")
    public ResponseEntity<List<AgendamentoResponse>> listarMeus() {
        Usuario cliente = usuarioService.getUsuarioLogado();
        List<Agendamento> agendamentos = agendamentoService.listarMeusAgendamentos(cliente.getId());
        List<AgendamentoResponse> responses = agendamentos.stream()
                .map(AgendamentoResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/recebidos")
    public ResponseEntity<List<AgendamentoResponse>> listarRecebidos() {
        Usuario profissional = usuarioService.getUsuarioLogado();
        List<Agendamento> agendamentos = agendamentoService.listarAgendamentosRecebidos(profissional.getId());
        List<AgendamentoResponse> responses = agendamentos.stream()
                .map(AgendamentoResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        Usuario usuario = usuarioService.getUsuarioLogado();
        agendamentoService.cancelarAgendamento(id, usuario.getId());
        return ResponseEntity.ok("Agendamento cancelado com sucesso");
    }

    @PutMapping("/{id}/reagendar")
    public ResponseEntity<AgendamentoResponse> reagendar(
            @PathVariable Long id,
            @Valid @RequestBody ReagendamentoRequest request) {
        Usuario usuario = usuarioService.getUsuarioLogado();
        Agendamento atualizado = agendamentoService.reagendar(id, request, usuario.getId());
        return ResponseEntity.ok(new AgendamentoResponse(atualizado));
    }
}