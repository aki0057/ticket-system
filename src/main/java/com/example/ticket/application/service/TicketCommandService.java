package com.example.ticket.application.service;

import com.example.ticket.application.command.*;
import com.example.ticket.domain.event.DomainEvent;
import com.example.ticket.domain.exception.TicketNotFoundException;
import com.example.ticket.domain.model.entity.Comment;
import com.example.ticket.domain.model.entity.Ticket;
import com.example.ticket.domain.model.value.*;
import com.example.ticket.domain.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * チケットコマンドサービス（CQRS: Write側）
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TicketCommandService {
    
    private final TicketRepository ticketRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    /**
     * チケット作成
     */
    public String createTicket(CreateTicketCommand command) {
        log.info("Creating ticket: {}", command.getTitle());
        
        TicketId ticketId = TicketId.generate();
        DueDate dueDate = DueDate.fromNowPlusHours(command.getDueDateHours());
        
        Ticket ticket = new Ticket(
            ticketId,
            command.getTitle(),
            command.getDescription(),
            command.getPriority(),
            dueDate
        );
        
        ticketRepository.save(ticket);
        publishEvents(ticket);
        
        log.info("Ticket created: {}", ticketId);
        return ticketId.getValue();
    }
    
    /**
     * ステータス更新
     */
    public void updateStatus(UpdateTicketStatusCommand command) {
        log.info("Updating ticket status: {} -> {}", command.getTicketId(), command.getNewStatus());
        
        Ticket ticket = findTicket(command.getTicketId());
        ticket.changeStatus(command.getNewStatus());
        
        ticketRepository.save(ticket);
        publishEvents(ticket);
    }
    
    /**
     * チケット割当
     */
    public void assignTicket(AssignTicketCommand command) {
        log.info("Assigning ticket {} to {}", command.getTicketId(), command.getAssigneeId());
        
        Ticket ticket = findTicket(command.getTicketId());
        Assignee assignee = new Assignee(command.getAssigneeId(), command.getAssigneeName());
        
        ticket.assign(assignee);
        ticketRepository.save(ticket);
        publishEvents(ticket);
    }
    
    /**
     * 優先度更新
     */
    public void updatePriority(UpdatePriorityCommand command) {
        log.info("Updating ticket priority: {} -> {}", command.getTicketId(), command.getNewPriority());
        
        Ticket ticket = findTicket(command.getTicketId());
        ticket.changePriority(command.getNewPriority());
        
        ticketRepository.save(ticket);
        publishEvents(ticket);
    }
    
    /**
     * チケット解決
     */
    public void resolveTicket(ResolveTicketCommand command) {
        log.info("Resolving ticket: {}", command.getTicketId());
        
        Ticket ticket = findTicket(command.getTicketId());
        ticket.resolve();
        
        ticketRepository.save(ticket);
        publishEvents(ticket);
    }
    
    /**
     * コメント追加
     */
    public void addComment(AddCommentCommand command) {
        log.info("Adding comment to ticket: {}", command.getTicketId());
        
        Ticket ticket = findTicket(command.getTicketId());
        Comment comment = new Comment(
            command.getTicketId(),
            command.getAuthor(),
            command.getContent()
        );
        
        ticket.addComment(comment);
        ticketRepository.save(ticket);
        publishEvents(ticket);
    }
    
    /**
     * チケット再開
     */
    public void reopenTicket(String ticketId) {
        log.info("Reopening ticket: {}", ticketId);
        
        Ticket ticket = findTicket(ticketId);
        ticket.reopen();
        
        ticketRepository.save(ticket);
        publishEvents(ticket);
    }
    
    /**
     * 期限延長
     */
    public void extendDueDate(String ticketId, int hours) {
        log.info("Extending due date for ticket: {} by {} hours", ticketId, hours);
        
        Ticket ticket = findTicket(ticketId);
        ticket.extendDueDate(hours);
        
        ticketRepository.save(ticket);
        publishEvents(ticket);
    }
    
    /**
     * エスカレーション
     */
    public void escalateTicket(String ticketId) {
        log.warn("Escalating ticket: {}", ticketId);
        
        Ticket ticket = findTicket(ticketId);
        ticket.escalate();
        
        ticketRepository.save(ticket);
        publishEvents(ticket);
    }
    
    // ヘルパーメソッド
    private Ticket findTicket(String ticketId) {
        return ticketRepository.findById(TicketId.of(ticketId))
            .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }
    
    private void publishEvents(Ticket ticket) {
        List<DomainEvent> events = ticket.getDomainEvents();
        events.forEach(event -> {
            log.debug("Publishing event: {}", event.getEventType());
            eventPublisher.publishEvent(event);
        });
        ticket.clearDomainEvents();
    }
}
