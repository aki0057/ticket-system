package com.example.ticket.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.ticket.domain.model.entity.Ticket;
import com.example.ticket.domain.model.value.Assignee;
import com.example.ticket.domain.model.value.DueDate;
import com.example.ticket.domain.model.value.Status;
import com.example.ticket.domain.model.value.TicketId;
import com.example.ticket.domain.repository.TicketRepository;
import com.example.ticket.infrastructure.persistence.entity.TicketJpaEntity;

import lombok.RequiredArgsConstructor;

/**
 * チケットリポジトリ実装
 */
@Component
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepository {

    private final TicketJpaRepository jpaRepository;

    @Override
    public Ticket save(Ticket ticket) {
        TicketJpaEntity entity = toJpaEntity(ticket);
        jpaRepository.save(entity);
        return ticket;
    }

    @Override
    public Optional<Ticket> findById(TicketId ticketId) {
        return jpaRepository.findById(ticketId.getValue())
                .map(this::toDomainModel);
    }

    @Override
    public List<Ticket> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> findByStatus(Status status) {
        return jpaRepository.findByStatus(status).stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> findByAssignee(String assigneeId) {
        return jpaRepository.findByAssigneeId(assigneeId).stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> findOverdueTickets(LocalDateTime now) {
        return jpaRepository.findOverdueTickets(now).stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> findSlaViolatedTickets() {
        return jpaRepository.findSlaViolatedTickets().stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(TicketId ticketId) {
        jpaRepository.deleteById(ticketId.getValue());
    }

    // エンティティ変換
    private TicketJpaEntity toJpaEntity(Ticket ticket) {
        return new TicketJpaEntity(
                ticket.getId().getValue(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getAssignee() != null ? ticket.getAssignee().getUserId() : null,
                ticket.getAssignee() != null ? ticket.getAssignee().getUserName() : null,
                ticket.getDueDate().getValue(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt());
    }

    private Ticket toDomainModel(TicketJpaEntity entity) {
        // リフレクションを使ってプライベートフィールドを設定
        try {
            Ticket ticket = new Ticket(
                    TicketId.of(entity.getId()),
                    entity.getTitle(),
                    entity.getDescription(),
                    DueDate.of(entity.getDueDate()));

            // ステータスを設定（新規以外の場合）
            if (entity.getStatus() != Status.NEW) {
                java.lang.reflect.Field statusField = Ticket.class.getDeclaredField("status");
                statusField.setAccessible(true);
                statusField.set(ticket, entity.getStatus());
            }

            // 担当者を設定
            if (entity.getAssigneeId() != null) {
                java.lang.reflect.Field assigneeField = Ticket.class.getDeclaredField("assignee");
                assigneeField.setAccessible(true);
                assigneeField.set(ticket, new Assignee(entity.getAssigneeId(), entity.getAssigneeName()));
            }

            // タイムスタンプを設定
            java.lang.reflect.Field createdAtField = Ticket.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(ticket, entity.getCreatedAt());

            java.lang.reflect.Field updatedAtField = Ticket.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(ticket, entity.getUpdatedAt());

            // イベントをクリア（永続化から読み込んだ場合は新規イベントなし）
            ticket.clearDomainEvents();

            return ticket;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JPA entity to domain model", e);
        }
    }
}
