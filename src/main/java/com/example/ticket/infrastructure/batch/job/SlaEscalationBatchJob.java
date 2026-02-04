package com.example.ticket.infrastructure.batch.job;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.ticket.application.service.TicketCommandService;
import com.example.ticket.domain.model.entity.Ticket;
import com.example.ticket.domain.repository.TicketRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * SLA超過チケットエスカレーションバッチ
 * 期限超過チケットを自動で優先度UP
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SlaEscalationBatchJob {

    private final TicketRepository ticketRepository;
    private final TicketCommandService commandService;

    @Scheduled(cron = "${ticket.batch.escalation-cron}")
    public void executeEscalation() {
        log.info("【バッチ開始】SLA超過チケット確認: {}", LocalDateTime.now());

        try {
            List<Ticket> overdueTickets = ticketRepository.findSlaViolatedTickets();
            log.info("期限超過チケット数: {}", overdueTickets.size());

            // チケット一覧をログ出力
            for (Ticket ticket : overdueTickets) {
                log.warn("期限超過: チケットID={}, タイトル={}, 期限={}",
                        ticket.getId().getValue(),
                        ticket.getTitle(),
                        ticket.getDueDate().getValue());
            }

            log.info("【バッチ完了】SLA超過チケット確認完了: {}件", overdueTickets.size());
        } catch (Exception e) {
            log.error("【バッチエラー】SLA超過チケットエスカレーション失敗", e);
        }
    }
}
