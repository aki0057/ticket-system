package com.example.ticket.infrastructure.persistence;

import com.example.ticket.domain.model.value.Priority;
import com.example.ticket.domain.model.value.Status;
import com.example.ticket.infrastructure.persistence.entity.TicketJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPAリポジトリ
 */
@Repository
public interface TicketJpaRepository extends JpaRepository<TicketJpaEntity, String> {
    
    List<TicketJpaEntity> findByStatus(Status status);
    
    List<TicketJpaEntity> findByAssigneeId(String assigneeId);
    
    List<TicketJpaEntity> findByPriority(Priority priority);
    
    @Query("SELECT t FROM TicketJpaEntity t WHERE t.dueDate < :now AND t.status <> 'RESOLVED'")
    List<TicketJpaEntity> findOverdueTickets(@Param("now") LocalDateTime now);
    
    @Query("SELECT t FROM TicketJpaEntity t WHERE t.dueDate < CURRENT_TIMESTAMP AND t.status <> 'RESOLVED'")
    List<TicketJpaEntity> findSlaViolatedTickets();
}
