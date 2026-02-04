package com.example.ticket.application.query;

import com.example.ticket.domain.model.value.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * チケット検索クエリ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketSearchQuery {
    private String ticketId;
    private Status status;
    private String assigneeId;
    private Boolean overdue;
}
