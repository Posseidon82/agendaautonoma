package com.agendaautonoma.domain;

import com.agendaautonoma.domain.enums.TipoNotificacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "agendamento_id", nullable = false)
    private Long agendamentoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoNotificacao tipo;

    @Column(name = "destinatario_email", nullable = false, length = 100)
    private String destinatarioEmail;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    private boolean enviada;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        enviada = false;
    }
}