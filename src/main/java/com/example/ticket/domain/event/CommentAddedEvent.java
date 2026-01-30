package com.example.ticket.domain.event;

import lombok.Getter;

/**
 * コメント追加イベント
 */
@Getter
public class CommentAddedEvent extends DomainEvent {
    private final String ticketId;
    private final String author;
    private final String content;
    
    public CommentAddedEvent(String ticketId, String author, String content) {
        super("CommentAdded");
        this.ticketId = ticketId;
        this.author = author;
        this.content = content;
    }
    
    @Override
    public String getAggregateId() {
        return ticketId;
    }
}
