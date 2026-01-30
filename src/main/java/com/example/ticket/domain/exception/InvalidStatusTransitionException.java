package com.example.ticket.domain.exception;

/**
 * 不正なステータス遷移例外
 */
public class InvalidStatusTransitionException extends DomainException {
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}
