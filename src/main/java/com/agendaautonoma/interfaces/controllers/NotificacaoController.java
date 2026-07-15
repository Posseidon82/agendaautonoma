package com.agendaautonoma.interfaces.controllers;

import com.agendaautonoma.application.NotificacaoService;
import com.agendaautonoma.domain.Notificacao;
import com.agendaautonoma.interfaces.dto.NotificacaoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    @GetMapping
    public ResponseEntity<List<NotificacaoResponse>> listarTodas() {
        // Simplificação: listar todas as notificações não enviadas (ou todas)
        List<Notificacao> notificacoes = notificacaoService.listarNaoEnviadas();
        List<NotificacaoResponse> responses = notificacoes.stream()
                .map(NotificacaoResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}/enviar")
    public ResponseEntity<?> enviarManual(@PathVariable Long id) {
        // Buscar notificação e enviar
        // Não implementado para brevidade, mas poderia ser feito
        return ResponseEntity.ok("Envio manual disparado (simulado)");
    }
}