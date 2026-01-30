package com.example.ticket.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * チケット解決コマンド
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResolveTicketCommand {
    private String ticketId;
}
