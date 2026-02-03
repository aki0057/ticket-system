package com.example.ticket.application.command;

import com.example.ticket.domain.model.value.Priority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 優先度更新コマンド
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePriorityCommand {
    private String ticketId;
    private Priority newPriority;
}
