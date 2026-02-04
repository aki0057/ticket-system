package com.example.ticket.domain.event;

import java.time.LocalDateTime;

import lombok.Getter;

/**
 * チケット作成イベント
 */
@Getter
public class TicketCreatedEvent extends DomainEvent {
    private final String ticketId;
    private final String title;
    private final LocalDateTime dueDate;

    public TicketCreatedEvent(String ticketId, String title, LocalDateTime dueDate) {
        super("TicketCreated");
        this.ticketId = ticketId;
        this.title = title;
        this.dueDate = dueDate;
    }

    @Override
    public String getAggregateId() {
        return ticketId;
    }
}
