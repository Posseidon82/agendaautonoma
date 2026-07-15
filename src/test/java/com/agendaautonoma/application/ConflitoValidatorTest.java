package com.agendaautonoma.application;

import com.agendaautonoma.domain.Agendamento;
import com.agendaautonoma.domain.enums.StatusAgendamento;
import com.agendaautonoma.infrastructure.persistence.AgendamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConflitoValidatorTest {

    @Mock
    private AgendamentoRepository repository;

    @InjectMocks
    private ConflitoValidator validator;

    private Long profissionalId = 1L;
    private LocalDate data = LocalDate.of(2026, 7, 15);
    private LocalTime inicio = LocalTime.of(14, 0);
    private LocalTime fim = LocalTime.of(14, 30);

    @BeforeEach
    void setup() {
        // default
    }

    @Test
    void deveRetornarTrueSeHouveConflito() {
        // Dado: existe um agendamento no mesmo horário
        Agendamento existente = Agendamento.builder()
                .profissionalId(profissionalId)
                .data(data)
                .horaInicio(inicio)
                .horaFim(fim)
                .status(StatusAgendamento.CONFIRMADO)
                .build();

        when(repository.findByProfissionalIdAndDataAndStatus(profissionalId, data, StatusAgendamento.CONFIRMADO))
                .thenReturn(List.of(existente));

        assertTrue(validator.existeConflito(profissionalId, data, inicio, fim));
    }

    @Test
    void deveRetornarFalseSeNaoHouveConflito() {
        when(repository.findByProfissionalIdAndDataAndStatus(profissionalId, data, StatusAgendamento.CONFIRMADO))
                .thenReturn(List.of());

        assertFalse(validator.existeConflito(profissionalId, data, inicio, fim));
    }
}