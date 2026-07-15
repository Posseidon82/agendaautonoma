package com.agendaautonoma.infrastructure.persistence;

import com.agendaautonoma.domain.Agendamento;
import com.agendaautonoma.domain.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByProfissionalIdAndDataAndStatus(Long profissionalId, LocalDate data, StatusAgendamento status);
    List<Agendamento> findByClienteId(Long clienteId);
    List<Agendamento> findByProfissionalId(Long profissionalId);
    boolean existsByProfissionalIdAndDataAndHoraInicioLessThanAndHoraFimGreaterThanAndStatus(
            Long profissionalId, LocalDate data, LocalTime horaFim, LocalTime horaInicio, StatusAgendamento status);
}