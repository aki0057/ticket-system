package com.example.ticket.application.command;

import com.example.ticket.domain.model.value.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * チケットステータス更新コマンド
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTicketStatusCommand {
    private String ticketId;
    private Status newStatus;
}
