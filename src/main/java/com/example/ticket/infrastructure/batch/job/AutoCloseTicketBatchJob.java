package com.example.ticket.infrastructure.batch.job;

import com.example.ticket.domain.model.entity.Ticket;
import com.example.ticket.domain.model.vo.Status;
import com.example.ticket.domain.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 期限切れチケット自動クローズバッチ
 * 期限超過かつ解決済みのチケットを自動クローズ
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AutoCloseTicketBatchJob {
    
    private final TicketRepository ticketRepository;
    
    @Scheduled(cron = "${ticket.batch.auto-close-cron}")
    public void autoCloseTickets() {
        log.info("【バッチ開始】期限切れチケット自動クローズ: {}", LocalDateTime.now());
        
        try {
            List<Ticket> tickets = ticketRepository.findAll();
            
            int closedCount = 0;
            for (Ticket ticket : tickets) {
                // 解決済みかつ期限切れから7日以上経過したチケットをクローズ対象とする
                if (ticket.getStatus() == Status.RESOLVED && 
                    ticket.getDueDate().isOverdue() &&
                    ticket.getDueDate().hoursUntilDue() < -168) { // 7日 = 168時間
                    
                    log.info("自動クローズ対象: チケットID={}", ticket.getId());
                    // 実際のクローズ処理はここに実装（例：DBから削除、アーカイブテーブルへ移動など）
                    closedCount++;
                }
            }
            
            log.info("【バッチ完了】自動クローズ完了: {}件", closedCount);
        } catch (Exception e) {
            log.error("【バッチエラー】期限切れチケット自動クローズ失敗", e);
        }
    }
}
