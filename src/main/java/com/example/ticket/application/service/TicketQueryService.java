package com.example.ticket.application.service;

import com.example.ticket.application.query.TicketSearchQuery;
import com.example.ticket.application.query.dto.*;
import com.example.ticket.domain.model.entity.Ticket;
import com.example.ticket.domain.model.value.Priority;
import com.example.ticket.domain.model.value.Status;
import com.example.ticket.domain.model.value.TicketId;
import com.example.ticket.domain.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * チケットクエリサービス（CQRS: Read側）
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketQueryService {
    
    private final TicketRepository ticketRepository;
    
    /**
     * チケット検索
     */
    public List<TicketDto> searchTickets(TicketSearchQuery query) {
        log.debug("Searching tickets with query: {}", query);
        
        List<Ticket> tickets = ticketRepository.findAll();
        
        // フィルタリング
        if (query.getTicketId() != null) {
            tickets = tickets.stream()
                .filter(t -> t.getId().getValue().equals(query.getTicketId()))
                .collect(Collectors.toList());
        }
        if (query.getStatus() != null) {
            tickets = tickets.stream()
                .filter(t -> t.getStatus() == query.getStatus())
                .collect(Collectors.toList());
        }
        if (query.getPriority() != null) {
            tickets = tickets.stream()
                .filter(t -> t.getPriority() == query.getPriority())
                .collect(Collectors.toList());
        }
        if (query.getAssigneeId() != null) {
            tickets = tickets.stream()
                .filter(t -> t.getAssignee() != null && 
                            t.getAssignee().getUserId().equals(query.getAssigneeId()))
                .collect(Collectors.toList());
        }
        if (Boolean.TRUE.equals(query.getOverdue())) {
            tickets = tickets.stream()
                .filter(Ticket::isSlaViolated)
                .collect(Collectors.toList());
        }
        
        return tickets.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    /**
     * ステータス別集計
     */
    public List<StatusStatisticsDto> getStatusStatistics() {
        log.debug("Getting status statistics");
        
        List<Ticket> tickets = ticketRepository.findAll();
        
        return tickets.stream()
            .collect(Collectors.groupingBy(Ticket::getStatus, Collectors.counting()))
            .entrySet().stream()
            .map(e -> new StatusStatisticsDto(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }
    
    /**
     * 担当者別ワークロード
     */
    public List<AssigneeWorkloadDto> getAssigneeWorkload() {
        log.debug("Getting assignee workload");
        
        List<Ticket> tickets = ticketRepository.findAll();
        
        Map<String, List<Ticket>> ticketsByAssignee = tickets.stream()
            .filter(t -> t.getAssignee() != null)
            .collect(Collectors.groupingBy(t -> t.getAssignee().getUserId()));
        
        return ticketsByAssignee.entrySet().stream()
            .map(entry -> {
                String assigneeId = entry.getKey();
                List<Ticket> assigneeTickets = entry.getValue();
                String assigneeName = assigneeTickets.get(0).getAssignee().getUserName();
                long totalTickets = assigneeTickets.size();
                long overdueTickets = assigneeTickets.stream()
                    .filter(Ticket::isSlaViolated)
                    .count();
                
                return new AssigneeWorkloadDto(assigneeId, assigneeName, totalTickets, overdueTickets);
            })
            .collect(Collectors.toList());
    }
    
    /**
     * SLA違反チケット一覧
     */
    public List<TicketDto> getSlaViolatedTickets() {
        log.debug("Getting SLA violated tickets");
        
        List<Ticket> tickets = ticketRepository.findSlaViolatedTickets();
        return tickets.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    /**
     * 優先度別レポート
     */
    public List<PriorityReportDto> getPriorityReport() {
        log.debug("Getting priority report");
        
        List<Ticket> tickets = ticketRepository.findAll();
        
        return tickets.stream()
            .collect(Collectors.groupingBy(Ticket::getPriority))
            .entrySet().stream()
            .map(entry -> {
                Priority priority = entry.getKey();
                List<Ticket> priorityTickets = entry.getValue();
                
                long totalTickets = priorityTickets.size();
                long resolvedTickets = priorityTickets.stream()
                    .filter(t -> t.getStatus() == Status.RESOLVED)
                    .count();
                long overdueTickets = priorityTickets.stream()
                    .filter(Ticket::isSlaViolated)
                    .count();
                
                // 簡易的な平均解決時間計算（実際は解決時刻-作成時刻）
                double averageResolutionHours = priority.getSlaHours() * 0.8;
                
                return new PriorityReportDto(
                    priority, totalTickets, resolvedTickets, overdueTickets, averageResolutionHours
                );
            })
            .collect(Collectors.toList());
    }
    
    /**
     * チケットID検索
     */
    public TicketDto getTicketById(String ticketId) {
        log.debug("Getting ticket by id: {}", ticketId);
        
        return ticketRepository.findById(TicketId.of(ticketId))
            .map(this::toDto)
            .orElse(null);
    }
    
    // DTOへの変換
    private TicketDto toDto(Ticket ticket) {
        return new TicketDto(
            ticket.getId().getValue(),
            ticket.getTitle(),
            ticket.getDescription(),
            ticket.getStatus(),
            ticket.getPriority(),
            ticket.getAssignee() != null ? ticket.getAssignee().getUserId() : null,
            ticket.getAssignee() != null ? ticket.getAssignee().getUserName() : null,
            ticket.getDueDate().getValue(),
            ticket.getCreatedAt(),
            ticket.getUpdatedAt(),
            ticket.isSlaViolated()
        );
    }
}
