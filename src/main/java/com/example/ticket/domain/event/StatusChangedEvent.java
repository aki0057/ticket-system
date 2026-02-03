package com.example.ticket.domain.event;

import com.example.ticket.domain.model.value.Status;
import lombok.Getter;

/**
 * ステータス変更イベント
 */
@Getter
public class StatusChangedEvent extends DomainEvent {
    private final String ticketId;
    private final Status oldStatus;
    private final Status newStatus;
    
    public StatusChangedEvent(String ticketId, Status oldStatus, Status newStatus) {
        super("StatusChanged");
        this.ticketId = ticketId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
    
    @Override
    public String getAggregateId() {
        return ticketId;
    }
}
