package com.example.ticket.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ticket.domain.model.value.Status;
import com.example.ticket.infrastructure.persistence.entity.TicketJpaEntity;

/**
 * Spring Data JPAリポジトリ
 */
@Repository
public interface TicketJpaRepository extends JpaRepository<TicketJpaEntity, String> {

    List<TicketJpaEntity> findByStatus(Status status);

    List<TicketJpaEntity> findByAssigneeId(String assigneeId);

    @Query("SELECT t FROM TicketJpaEntity t WHERE t.dueDate < :now AND t.status <> 'RESOLVED'")
    List<TicketJpaEntity> findOverdueTickets(@Param("now") LocalDateTime now);

    @Query("SELECT t FROM TicketJpaEntity t WHERE t.dueDate < CURRENT_TIMESTAMP AND t.status <> 'RESOLVED'")
    List<TicketJpaEntity> findSlaViolatedTickets();
}
