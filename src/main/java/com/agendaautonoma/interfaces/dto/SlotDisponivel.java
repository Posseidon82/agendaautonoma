package com.agendaautonoma.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotDisponivel {
    private LocalTime inicio;
    private LocalTime fim;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotDisponivel that = (SlotDisponivel) o;
        return Objects.equals(inicio, that.inicio) && Objects.equals(fim, that.fim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inicio, fim);
    }
}