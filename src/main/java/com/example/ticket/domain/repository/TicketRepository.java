package com.example.ticket.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.ticket.domain.model.entity.Ticket;
import com.example.ticket.domain.model.value.Status;
import com.example.ticket.domain.model.value.TicketId;

/**
 * チケットリポジトリインターフェース
 */
public interface TicketRepository {

    /**
     * チケット保存
     */
    Ticket save(Ticket ticket);

    /**
     * ID検索
     */
    Optional<Ticket> findById(TicketId ticketId);

    /**
     * 全件取得
     */
    List<Ticket> findAll();

    /**
     * ステータス別検索
     */
    List<Ticket> findByStatus(Status status);

    /**
     * 担当者別検索
     */
    List<Ticket> findByAssignee(String assigneeId);

    /**
     * 期限超過チケット検索
     */
    List<Ticket> findOverdueTickets(LocalDateTime now);

    /**
     * SLA違反チケット検索
     */
    List<Ticket> findSlaViolatedTickets();

    /**
     * チケット削除
     */
    void delete(TicketId ticketId);
}
