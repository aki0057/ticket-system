package com.example.ticket.application.query.dto;

import com.example.ticket.domain.model.value.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ステータス別集計DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusStatisticsDto {
    private Status status;
    private long count;
}
