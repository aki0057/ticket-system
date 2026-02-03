package com.example.ticket.application.query.dto;

import com.example.ticket.domain.model.value.Priority;
import com.example.ticket.domain.model.value.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * チケットDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private String id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private String assigneeId;
    private String assigneeName;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean slaViolated;
}
