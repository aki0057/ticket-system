package com.example.ticket.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 期限延長イベント
 */
@Getter
public class DueDateExtendedEvent extends DomainEvent {
    private final String ticketId;
    private final LocalDateTime oldDueDate;
    private final LocalDateTime newDueDate;
    
    public DueDateExtendedEvent(String ticketId, LocalDateTime oldDueDate, LocalDateTime newDueDate) {
        super("DueDateExtended");
        this.ticketId = ticketId;
        this.oldDueDate = oldDueDate;
        this.newDueDate = newDueDate;
    }
    
    @Override
    public String getAggregateId() {
        return ticketId;
    }
}
