package com.example.ticket.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * チケット割当コマンド
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignTicketCommand {
    private String ticketId;
    private String assigneeId;
    private String assigneeName;
}
