package com.example.ticket.domain.model.vo;

import lombok.Value;

/**
 * 担当者値オブジェクト
 */
@Value
public class Assignee {
    String userId;
    String userName;

    public Assignee(String userId, String userName) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("UserId cannot be null or empty");
        }
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("UserName cannot be null or empty");
        }
        this.userId = userId;
        this.userName = userName;
    }

    public static Assignee of(String userId, String userName) {
        return new Assignee(userId, userName);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", userName, userId);
    }
}
