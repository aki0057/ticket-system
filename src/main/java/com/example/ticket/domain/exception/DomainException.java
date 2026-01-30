package com.example.ticket.domain.exception;

/**
 * ドメイン例外の基底クラス
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
