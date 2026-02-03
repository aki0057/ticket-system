package com.example.ticket.domain.event;

import com.example.ticket.domain.model.value.Priority;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * チケット作成イベント
 */
@Getter
public class TicketCreatedEvent extends DomainEvent {
    private final String ticketId;
    private final String title;
    private final Priority priority;
    private final LocalDateTime dueDate;
    
    public TicketCreatedEvent(String ticketId, String title, Priority priority, LocalDateTime dueDate) {
        super("TicketCreated");
        this.ticketId = ticketId;
        this.title = title;
        this.priority = priority;
        this.dueDate = dueDate;
    }
    
    @Override
    public String getAggregateId() {
        return ticketId;
    }
}
