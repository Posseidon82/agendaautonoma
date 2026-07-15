package com.agendaautonoma.interfaces.dto;

import com.agendaautonoma.domain.Agendamento;
import com.agendaautonoma.domain.enums.StatusAgendamento;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AgendamentoResponse {
    private Long id;
    private Long profissionalId;
    private Long clienteId;
    private String clienteNome;
    private String clienteEmail;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private StatusAgendamento status;

    public AgendamentoResponse(Agendamento agendamento) {
        this.id = agendamento.getId();
        this.profissionalId = agendamento.getProfissionalId();
        this.clienteId = agendamento.getClienteId();
        this.clienteNome = agendamento.getClienteNome();
        this.clienteEmail = agendamento.getClienteEmail();
        this.data = agendamento.getData();
        this.horaInicio = agendamento.getHoraInicio();
        this.horaFim = agendamento.getHoraFim();
        this.status = agendamento.getStatus();
    }
}