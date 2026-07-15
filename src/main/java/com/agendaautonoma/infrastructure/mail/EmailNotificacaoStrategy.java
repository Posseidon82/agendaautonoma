package com.agendaautonoma.infrastructure.mail;

import com.agendaautonoma.domain.Notificacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailNotificacaoStrategy {

    public void enviar(Notificacao notificacao) {
        log.info("=== ENVIO DE E-MAIL SIMULADO ===");
        log.info("Para: {}", notificacao.getDestinatarioEmail());
        log.info("Tipo: {}", notificacao.getTipo());
        log.info("Conteúdo: {}", notificacao.getConteudo());
        log.info("=================================");
        // Em produção, implementar com JavaMailSender
    }
}