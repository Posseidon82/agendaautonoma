package com.agendaautonoma.interfaces.dto;

import com.agendaautonoma.domain.Usuario;
import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioTrabalhoResponse {
    private String diasTrabalho;
    private LocalTime inicioTrabalho;
    private LocalTime fimTrabalho;
    private Integer duracaoPadraoMinutos;

    public HorarioTrabalhoResponse(Usuario usuario) {
        this.diasTrabalho = usuario.getDiasTrabalho();
        this.inicioTrabalho = usuario.getInicioTrabalho();
        this.fimTrabalho = usuario.getFimTrabalho();
        this.duracaoPadraoMinutos = usuario.getDuracaoPadraoMinutos();
    }
}