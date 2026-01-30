package com.example.ticket.domain.model.entity;

import com.example.ticket.domain.model.vo.Assignee;
import com.example.ticket.domain.model.vo.TicketId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 割当エンティティ
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment {
    
    private String id;
    private TicketId ticketId;
    private Assignee assignee;
    private LocalDateTime assignedAt;
    private String assignedBy;
    
    public Assignment(TicketId ticketId, Assignee assignee, String assignedBy) {
        this.id = UUID.randomUUID().toString();
        this.ticketId = ticketId;
        this.assignee = assignee;
        this.assignedBy = assignedBy;
        this.assignedAt = LocalDateTime.now();
    }
    
    public boolean isAssignedTo(String userId) {
        return assignee.getUserId().equals(userId);
    }
}
