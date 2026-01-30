package com.example.ticket.domain.event;

import lombok.Getter;

/**
 * チケット再開イベント
 */
@Getter
public class TicketReopenedEvent extends DomainEvent {
    private final String ticketId;
    
    public TicketReopenedEvent(String ticketId) {
        super("TicketReopened");
        this.ticketId = ticketId;
    }
    
    @Override
    public String getAggregateId() {
        return ticketId;
    }
}
