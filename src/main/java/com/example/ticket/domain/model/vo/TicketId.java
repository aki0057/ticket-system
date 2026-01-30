package com.example.ticket.domain.model.vo;

import lombok.Value;

import java.util.UUID;

/**
 * チケットID値オブジェクト
 */
@Value
public class TicketId {
    String value;

    public TicketId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TicketId cannot be null or empty");
        }
        this.value = value;
    }

    public static TicketId generate() {
        return new TicketId(UUID.randomUUID().toString());
    }

    public static TicketId of(String value) {
        return new TicketId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
