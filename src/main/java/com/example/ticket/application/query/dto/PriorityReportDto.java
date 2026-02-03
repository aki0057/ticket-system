package com.example.ticket.application.query.dto;

import com.example.ticket.domain.model.value.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 優先度別レポートDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriorityReportDto {
    private Priority priority;
    private long totalTickets;
    private long resolvedTickets;
    private long overdueTickets;
    private double averageResolutionHours;
}
