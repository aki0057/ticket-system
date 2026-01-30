package com.example.ticket.application.command;

import com.example.ticket.domain.model.vo.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * チケット作成コマンド
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketCommand {
    private String title;
    private String description;
    private Priority priority;
    private int dueDateHours;
}
