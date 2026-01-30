package com.example.ticket.domain.event;

import lombok.Getter;

/**
 * チケット解決イベント
 */
@Getter
public class TicketResolvedEvent extends DomainEvent {
    private final String ticketId;
    
    public TicketResolvedEvent(String ticketId) {
        super("TicketResolved");
        this.ticketId = ticketId;
    }
    
    @Override
    public String getAggregateId() {
        return ticketId;
    }
}
