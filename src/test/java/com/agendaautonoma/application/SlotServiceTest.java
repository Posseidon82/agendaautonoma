package com.agendaautonoma.application;

import com.agendaautonoma.domain.Agendamento;
import com.agendaautonoma.domain.Usuario;
import com.agendaautonoma.domain.enums.PapelUsuario;
import com.agendaautonoma.domain.enums.StatusAgendamento;
import com.agendaautonoma.interfaces.dto.SlotDisponivel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SlotServiceTest {

    @InjectMocks
    private SlotService slotService;

    private Usuario profissional;
    private LocalDate data;

    @BeforeEach
    void setUp() {
        profissional = Usuario.builder()
                .id(1L)
                .papel(PapelUsuario.PROFISSIONAL)
                .diasTrabalho("SEG,TER,QUA,QUI,SEX")
                .inicioTrabalho(LocalTime.of(9, 0))
                .fimTrabalho(LocalTime.of(18, 0))
                .duracaoPadraoMinutos(30)
                .build();
        data = LocalDate.of(2026, 7, 15); // quarta-feira
    }

    @Test
    void deveCalcularSlotsLivresConsiderandoOcupados() {
        // Dado: um slot ocupado das 14:00 às 14:30
        Agendamento ocupado = Agendamento.builder()
                .profissionalId(1L)
                .data(data)
                .horaInicio(LocalTime.of(14, 0))
                .horaFim(LocalTime.of(14, 30))
                .status(StatusAgendamento.CONFIRMADO)
                .build();

        List<Agendamento> ocupados = List.of(ocupado);
        List<SlotDisponivel> slots = slotService.calcularSlots(profissional, data, ocupados);

        // Então: não deve conter o slot 14:00-14:30
        assertFalse(slots.contains(new SlotDisponivel(LocalTime.of(14, 0), LocalTime.of(14, 30))));
        // Deve conter outros slots, por exemplo 14:30-15:00
        assertTrue(slots.contains(new SlotDisponivel(LocalTime.of(14, 30), LocalTime.of(15, 0))));
    }

    @Test
    void deveRetornarListaVaziaSeDiaNaoTrabalhado() {
        LocalDate domingo = LocalDate.of(2026, 7, 19); // domingo
        List<SlotDisponivel> slots = slotService.calcularSlots(profissional, domingo, List.of());
        assertTrue(slots.isEmpty());
    }
}