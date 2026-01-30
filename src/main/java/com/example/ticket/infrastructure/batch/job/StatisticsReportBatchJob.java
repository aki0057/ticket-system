package com.example.ticket.infrastructure.batch.job;

import com.example.ticket.application.service.TicketQueryService;
import com.example.ticket.application.query.dto.AssigneeWorkloadDto;
import com.example.ticket.application.query.dto.PriorityReportDto;
import com.example.ticket.application.query.dto.StatusStatisticsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 統計レポート生成バッチ
 * 日次でステータス別・担当者別の統計を生成
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsReportBatchJob {
    
    private final TicketQueryService queryService;
    
    @Scheduled(cron = "${ticket.batch.statistics-cron}")
    public void generateStatistics() {
        log.info("【バッチ開始】統計レポート生成: {}", LocalDateTime.now());
        
        try {
            // ステータス別集計
            List<StatusStatisticsDto> statusStats = queryService.getStatusStatistics();
            log.info("=== ステータス別統計 ===");
            statusStats.forEach(stat -> 
                log.info("  {}: {}件", stat.getStatus().getDisplayName(), stat.getCount())
            );
            
            // 担当者別ワークロード
            List<AssigneeWorkloadDto> workloads = queryService.getAssigneeWorkload();
            log.info("=== 担当者別ワークロード ===");
            workloads.forEach(workload -> 
                log.info("  {}: 総数{}件, 期限切れ{}件", 
                    workload.getAssigneeName(), 
                    workload.getTotalTickets(), 
                    workload.getOverdueTickets())
            );
            
            // 優先度別レポート
            List<PriorityReportDto> priorityReports = queryService.getPriorityReport();
            log.info("=== 優先度別レポート ===");
            priorityReports.forEach(report -> 
                log.info("  {}: 総数{}件, 解決{}件, 期限切れ{}件", 
                    report.getPriority().getDisplayName(),
                    report.getTotalTickets(),
                    report.getResolvedTickets(),
                    report.getOverdueTickets())
            );
            
            log.info("【バッチ完了】統計レポート生成完了");
        } catch (Exception e) {
            log.error("【バッチエラー】統計レポート生成失敗", e);
        }
    }
}
