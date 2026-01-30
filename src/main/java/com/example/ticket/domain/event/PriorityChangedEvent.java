package com.example.ticket.domain.event;

import com.example.ticket.domain.model.vo.Priority;
import lombok.Getter;

/**
 * 優先度変更イベント
 */
@Getter
public class PriorityChangedEvent extends DomainEvent {
    private final String ticketId;
    private final Priority oldPriority;
    private final Priority newPriority;
    
    public PriorityChangedEvent(String ticketId, Priority oldPriority, Priority newPriority) {
        super("PriorityChanged");
        this.ticketId = ticketId;
        this.oldPriority = oldPriority;
        this.newPriority = newPriority;
    }
    
    @Override
    public String getAggregateId() {
        return ticketId;
    }
}
