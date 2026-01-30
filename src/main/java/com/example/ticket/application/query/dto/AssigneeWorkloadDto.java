package com.example.ticket.application.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 担当者別ワークロードDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssigneeWorkloadDto {
    private String assigneeId;
    private String assigneeName;
    private long totalTickets;
    private long overdueTickets;
}
