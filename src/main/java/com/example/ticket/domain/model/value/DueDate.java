package com.example.ticket.domain.model.value;

import lombok.Value;

import java.time.LocalDateTime;

/**
 * 期限値オブジェクト
 */
@Value
public class DueDate {
    LocalDateTime value;

    public DueDate(LocalDateTime value) {
        if (value == null) {
            throw new IllegalArgumentException("DueDate cannot be null");
        }
        this.value = value;
    }

    public static DueDate of(LocalDateTime value) {
        return new DueDate(value);
    }

    public static DueDate fromNowPlusHours(int hours) {
        return new DueDate(LocalDateTime.now().plusHours(hours));
    }

    /**
     * 期限切れかどうか
     */
    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(value);
    }

    /**
     * 期限まであと何時間か
     */
    public long hoursUntilDue() {
        return java.time.Duration.between(LocalDateTime.now(), value).toHours();
    }

    /**
     * 期限を延長
     */
    public DueDate extend(int hours) {
        return new DueDate(value.plusHours(hours));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
