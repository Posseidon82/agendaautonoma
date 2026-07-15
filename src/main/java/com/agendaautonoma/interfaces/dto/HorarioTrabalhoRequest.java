package com.agendaautonoma.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioTrabalhoRequest {
    @NotBlank
    private String diasTrabalho; // ex: "SEG,TER,QUA,QUI,SEX"
    @NotNull
    private LocalTime inicioTrabalho;
    @NotNull
    private LocalTime fimTrabalho;
    @NotNull
    private Integer duracaoPadraoMinutos;
}