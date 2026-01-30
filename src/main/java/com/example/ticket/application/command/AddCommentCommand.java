package com.example.ticket.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * コメント追加コマンド
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentCommand {
    private String ticketId;
    private String author;
    private String content;
}
