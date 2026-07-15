package com.agendaautonoma.interfaces.dto;

import com.agendaautonoma.domain.Notificacao;
import com.agendaautonoma.domain.enums.TipoNotificacao;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificacaoResponse {
    private Long id;
    private Long agendamentoId;
    private TipoNotificacao tipo;
    private String destinatarioEmail;
    private String conteudo;
    private LocalDateTime dataEnvio;
    private boolean enviada;

    public NotificacaoResponse(Notificacao notificacao) {
        this.id = notificacao.getId();
        this.agendamentoId = notificacao.getAgendamentoId();
        this.tipo = notificacao.getTipo();
        this.destinatarioEmail = notificacao.getDestinatarioEmail();
        this.conteudo = notificacao.getConteudo();
        this.dataEnvio = notificacao.getDataEnvio();
        this.enviada = notificacao.isEnviada();
    }
}