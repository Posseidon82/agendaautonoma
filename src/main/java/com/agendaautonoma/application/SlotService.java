package com.agendaautonoma.application;

import com.agendaautonoma.domain.Agendamento;
import com.agendaautonoma.domain.Usuario;
import com.agendaautonoma.domain.enums.StatusAgendamento;
import com.agendaautonoma.interfaces.dto.SlotDisponivel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SlotService {

    /**
     * Calcula os slots disponíveis para um profissional em uma determinada data.
     * Considera os dias de trabalho, horário de expediente, duração padrão
     * e remove os horários já ocupados por agendamentos confirmados.
     */
    public List<SlotDisponivel> calcularSlots(Usuario profissional, LocalDate data,
                                              List<Agendamento> ocupados) {
        List<SlotDisponivel> slots = new ArrayList<>();

        // 1. Verificar se o profissional atende na data (dia da semana)
        String diaSemana = switch (data.getDayOfWeek()) {
            case MONDAY -> "SEG";
            case TUESDAY -> "TER";
            case WEDNESDAY -> "QUA";
            case THURSDAY -> "QUI";
            case FRIDAY -> "SEX";
            case SATURDAY -> "SAB";
            case SUNDAY -> "DOM";
        };

        Set<String> diasTrabalho = Set.of(profissional.getDiasTrabalho().split(","));
        if (!diasTrabalho.contains(diaSemana)) {
            return slots; // nenhum slot
        }

        // 2. Gerar todos os slots possíveis dentro do expediente
        LocalTime inicio = profissional.getInicioTrabalho();
        LocalTime fim = profissional.getFimTrabalho();
        int duracao = profissional.getDuracaoPadraoMinutos();

        LocalTime current = inicio;
        while (current.plusMinutes(duracao).isBefore(fim) || current.plusMinutes(duracao).equals(fim)) {
            LocalTime slotInicio = current;
            LocalTime slotFim = current.plusMinutes(duracao);
            slots.add(new SlotDisponivel(slotInicio, slotFim));
            current = slotFim;
        }

        // 3. Remover slots ocupados por agendamentos confirmados
        List<SlotDisponivel> ocupadosList = ocupados.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CONFIRMADO)
                .map(a -> new SlotDisponivel(a.getHoraInicio(), a.getHoraFim()))
                .collect(Collectors.toList());

        slots.removeAll(ocupadosList);

        return slots;
    }
}