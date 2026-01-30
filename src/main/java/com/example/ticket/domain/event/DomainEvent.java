package com.example.ticket.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * ドメインイベントの基底クラス
 */
@Getter
public abstract class DomainEvent {
    private final LocalDateTime occurredOn;
    private final String eventType;
    
    protected DomainEvent(String eventType) {
        this.eventType = eventType;
        this.occurredOn = LocalDateTime.now();
    }
    
    public abstract String getAggregateId();
}
