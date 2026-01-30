package com.example.ticket.domain.exception;

/**
 * チケットが見つからない例外
 */
public class TicketNotFoundException extends DomainException {
    public TicketNotFoundException(String ticketId) {
        super(String.format("Ticket not found: %s", ticketId));
    }
}
