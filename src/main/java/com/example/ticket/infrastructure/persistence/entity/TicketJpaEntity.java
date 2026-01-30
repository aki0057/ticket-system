package com.example.ticket.infrastructure.persistence.entity;

import com.example.ticket.domain.model.vo.Priority;
import com.example.ticket.domain.model.vo.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * チケットJPAエンティティ
 */
@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketJpaEntity {
    
    @Id
    private String id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 5000)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;
    
    private String assigneeId;
    private String assigneeName;
    
    @Column(nullable = false)
    private LocalDateTime dueDate;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
