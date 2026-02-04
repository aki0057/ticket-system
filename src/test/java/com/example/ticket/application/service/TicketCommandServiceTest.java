package com.example.ticket.application.service;

import com.example.ticket.application.command.UpdateTicketStatusCommand;
import com.example.ticket.domain.exception.TicketNotFoundException;
import com.example.ticket.domain.model.entity.Ticket;
import com.example.ticket.domain.model.value.*;
import com.example.ticket.domain.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * チケットコマンドサービスのテスト
 */
@ExtendWith(MockitoExtension.class)
class TicketCommandServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TicketCommandService commandService;

    private Ticket ticket;
    private TicketId ticketId;

    @BeforeEach
    void setUp() {
        ticketId = TicketId.generate();
        DueDate dueDate = DueDate.fromNowPlusHours(24);
        ticket = new Ticket(ticketId, "テストチケット", "説明", dueDate);
    }

    @Test
    void ステータス更新が成功する() {
        UpdateTicketStatusCommand command = new UpdateTicketStatusCommand(
                ticketId.getValue(),
                Status.IN_PROGRESS);

        when(ticketRepository.findById(any(TicketId.class))).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        assertDoesNotThrow(() -> commandService.updateStatus(command));

        verify(ticketRepository, times(1)).findById(any(TicketId.class));
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void 存在しないチケットのステータス更新は例外が発生する() {
        UpdateTicketStatusCommand command = new UpdateTicketStatusCommand(
                "non-existent-id",
                Status.IN_PROGRESS);

        when(ticketRepository.findById(any(TicketId.class))).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> commandService.updateStatus(command));
    }
}
