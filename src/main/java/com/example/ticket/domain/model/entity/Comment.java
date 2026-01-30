package com.example.ticket.domain.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * コメントエンティティ
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    
    private String id;
    private String ticketId;
    private String author;
    private String content;
    private LocalDateTime createdAt;
    
    public Comment(String ticketId, String author, String content) {
        this.id = UUID.randomUUID().toString();
        this.ticketId = ticketId;
        this.author = author;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        
        validateContent();
    }
    
    private void validateContent() {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        if (content.length() > 5000) {
            throw new IllegalArgumentException("Comment content too long (max 5000 characters)");
        }
    }
}
