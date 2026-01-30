package com.example.ticket.domain.event;

import lombok.Getter;

/**
 * 担当者変更イベント
 */
@Getter
public class AssigneeChangedEvent extends DomainEvent {
    private final String ticketId;
    private final String oldAssigneeId;
    private final String newAssigneeId;
    
    public AssigneeChangedEvent(String ticketId, String oldAssigneeId, String newAssigneeId) {
        super("AssigneeChanged");
        this.ticketId = ticketId;
        this.oldAssigneeId = oldAssigneeId;
        this.newAssigneeId = newAssigneeId;
    }
    
    @Override
    public String getAggregateId() {
        return ticketId;
    }
}
