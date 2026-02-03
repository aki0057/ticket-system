package com.example.ticket.presentation.controller;

import com.example.ticket.application.command.*;
import com.example.ticket.application.query.TicketSearchQuery;
import com.example.ticket.application.query.dto.*;
import com.example.ticket.application.service.TicketCommandService;
import com.example.ticket.application.service.TicketQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * チケットRESTコントローラー
 */
@Slf4j
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    
    private final TicketCommandService commandService;
    private final TicketQueryService queryService;
    
    /**
     * チケット作成
     */
    @PostMapping
    public ResponseEntity<String> createTicket(@RequestBody CreateTicketCommand command) {
        log.info("POST /api/tickets: {}", command);
        String ticketId = commandService.createTicket(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketId);
    }
    
    /**
     * ステータス更新
     */
    @PutMapping("/{ticketId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable String ticketId,
            @RequestBody UpdateTicketStatusCommand command) {
        log.info("PUT /api/tickets/{}/status: {}", ticketId, command);
        command.setTicketId(ticketId);
        commandService.updateStatus(command);
        return ResponseEntity.ok().build();
    }
    
    /**
     * チケット割当
     */
    @PutMapping("/{ticketId}/assign")
    public ResponseEntity<Void> assignTicket(
            @PathVariable String ticketId,
            @RequestBody AssignTicketCommand command) {
        log.info("PUT /api/tickets/{}/assign: {}", ticketId, command);
        command.setTicketId(ticketId);
        commandService.assignTicket(command);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 優先度更新
     */
    @PutMapping("/{ticketId}/priority")
    public ResponseEntity<Void> updatePriority(
            @PathVariable String ticketId,
            @RequestBody UpdatePriorityCommand command) {
        log.info("PUT /api/tickets/{}/priority: {}", ticketId, command);
        command.setTicketId(ticketId);
        commandService.updatePriority(command);
        return ResponseEntity.ok().build();
    }
    
    /**
     * チケット解決
     */
    @PutMapping("/{ticketId}/resolve")
    public ResponseEntity<Void> resolveTicket(@PathVariable String ticketId) {
        log.info("PUT /api/tickets/{}/resolve", ticketId);
        commandService.resolveTicket(new ResolveTicketCommand(ticketId));
        return ResponseEntity.ok().build();
    }
    
    /**
     * チケット再開
     */
    @PutMapping("/{ticketId}/reopen")
    public ResponseEntity<Void> reopenTicket(@PathVariable String ticketId) {
        log.info("PUT /api/tickets/{}/reopen", ticketId);
        commandService.reopenTicket(ticketId);
        return ResponseEntity.ok().build();
    }
    
    /**
     * コメント追加
     */
    @PostMapping("/{ticketId}/comments")
    public ResponseEntity<Void> addComment(
            @PathVariable String ticketId,
            @RequestBody AddCommentCommand command) {
        log.info("POST /api/tickets/{}/comments: {}", ticketId, command);
        command.setTicketId(ticketId);
        commandService.addComment(command);
        return ResponseEntity.ok().build();
    }
    
    /**
     * チケット検索
     */
    @GetMapping
    public ResponseEntity<List<TicketDto>> searchTickets(
            @RequestParam(required = false) String ticketId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String assigneeId,
            @RequestParam(required = false) Boolean overdue) {
        log.info("GET /api/tickets");
        
        TicketSearchQuery query = new TicketSearchQuery();
        query.setTicketId(ticketId);
        if (status != null) {
            query.setStatus(com.example.ticket.domain.model.value.Status.valueOf(status));
        }
        if (priority != null) {
            query.setPriority(com.example.ticket.domain.model.value.Priority.valueOf(priority));
        }
        query.setAssigneeId(assigneeId);
        query.setOverdue(overdue);
        
        List<TicketDto> tickets = queryService.searchTickets(query);
        return ResponseEntity.ok(tickets);
    }
    
    /**
     * チケット詳細取得
     */
    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable String ticketId) {
        log.info("GET /api/tickets/{}", ticketId);
        TicketDto ticket = queryService.getTicketById(ticketId);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket);
    }
    
    /**
     * ステータス別統計
     */
    @GetMapping("/statistics/status")
    public ResponseEntity<List<StatusStatisticsDto>> getStatusStatistics() {
        log.info("GET /api/tickets/statistics/status");
        List<StatusStatisticsDto> statistics = queryService.getStatusStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * 担当者別ワークロード
     */
    @GetMapping("/statistics/workload")
    public ResponseEntity<List<AssigneeWorkloadDto>> getAssigneeWorkload() {
        log.info("GET /api/tickets/statistics/workload");
        List<AssigneeWorkloadDto> workload = queryService.getAssigneeWorkload();
        return ResponseEntity.ok(workload);
    }
    
    /**
     * SLA違反チケット一覧
     */
    @GetMapping("/sla-violated")
    public ResponseEntity<List<TicketDto>> getSlaViolatedTickets() {
        log.info("GET /api/tickets/sla-violated");
        List<TicketDto> tickets = queryService.getSlaViolatedTickets();
        return ResponseEntity.ok(tickets);
    }
    
    /**
     * 優先度別レポート
     */
    @GetMapping("/statistics/priority")
    public ResponseEntity<List<PriorityReportDto>> getPriorityReport() {
        log.info("GET /api/tickets/statistics/priority");
        List<PriorityReportDto> report = queryService.getPriorityReport();
        return ResponseEntity.ok(report);
    }
}
