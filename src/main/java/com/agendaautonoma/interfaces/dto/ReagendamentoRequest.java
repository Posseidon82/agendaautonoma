package com.agendaautonoma.interfaces.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReagendamentoRequest {
    @NotNull @FutureOrPresent
    private LocalDate novaData;
    @NotNull
    private LocalTime novaHoraInicio;
}