package com.agendaautonoma.infrastructure.persistence;

import com.agendaautonoma.domain.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByAgendamentoId(Long agendamentoId);
    List<Notificacao> findByEnviadaFalse();
}