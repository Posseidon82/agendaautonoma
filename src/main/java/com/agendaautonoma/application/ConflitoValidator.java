package com.agendaautonoma.application;

import com.agendaautonoma.domain.Agendamento;
import com.agendaautonoma.domain.enums.StatusAgendamento;
import com.agendaautonoma.infrastructure.persistence.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConflitoValidator {

    private final AgendamentoRepository agendamentoRepository;

    /**
     * Verifica se existe conflito de horário para um profissional em determinada data e horário.
     * Considera apenas agendamentos com status CONFIRMADO.
     */
    public boolean existeConflito(Long profissionalId, LocalDate data, LocalTime inicio, LocalTime fim) {
        List<Agendamento> existentes = agendamentoRepository
                .findByProfissionalIdAndDataAndStatus(profissionalId, data, StatusAgendamento.CONFIRMADO);

        return existentes.stream().anyMatch(a ->
                !(a.getHoraFim().isBefore(inicio) || a.getHoraInicio().isAfter(fim))
        );
    }
}