package com.example.ticket.infrastructure.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.ticket.domain.event.AssigneeChangedEvent;
import com.example.ticket.domain.event.CommentAddedEvent;
import com.example.ticket.domain.event.DueDateExtendedEvent;
import com.example.ticket.domain.event.StatusChangedEvent;
import com.example.ticket.domain.event.TicketCreatedEvent;
import com.example.ticket.domain.event.TicketReopenedEvent;
import com.example.ticket.domain.event.TicketResolvedEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * ドメインイベントハンドラー
 * イベント発行時に監査ログを記録
 */
@Slf4j
@Component
public class DomainEventHandler {

    @Async
    @EventListener
    public void handleTicketCreatedEvent(TicketCreatedEvent event) {
        log.info("【監査ログ】チケット作成: ID={}, タイトル={}, 期限={}, 発生時刻={}",
                event.getTicketId(),
                event.getTitle(),
                event.getDueDate(),
                event.getOccurredOn());
    }

    @Async
    @EventListener
    public void handleStatusChangedEvent(StatusChangedEvent event) {
        log.info("【監査ログ】ステータス変更: チケットID={}, {}→{}, 発生時刻={}",
                event.getTicketId(),
                event.getOldStatus(),
                event.getNewStatus(),
                event.getOccurredOn());
    }

    @Async
    @EventListener
    public void handleAssigneeChangedEvent(AssigneeChangedEvent event) {
        log.info("【監査ログ】担当者変更: チケットID={}, {}→{}, 発生時刻={}",
                event.getTicketId(),
                event.getOldAssigneeId(),
                event.getNewAssigneeId(),
                event.getOccurredOn());
    }

    @Async
    @EventListener

    public void handleCommentAddedEvent(CommentAddedEvent event) {
        log.info("【監査ログ】コメント追加: チケットID={}, 投稿者={}, 発生時刻={}",
                event.getTicketId(),
                event.getAuthor(),
                event.getOccurredOn());
    }

    @Async
    @EventListener
    public void handleTicketResolvedEvent(TicketResolvedEvent event) {
        log.info("【監査ログ】チケット解決: チケットID={}, 発生時刻={}",
                event.getTicketId(),
                event.getOccurredOn());
    }

    @Async
    @EventListener
    public void handleTicketReopenedEvent(TicketReopenedEvent event) {
        log.warn("【監査ログ】チケット再開: チケットID={}, 発生時刻={}",
                event.getTicketId(),
                event.getOccurredOn());
    }

    @Async
    @EventListener
    public void handleDueDateExtendedEvent(DueDateExtendedEvent event) {
        log.info("【監査ログ】期限延長: チケットID={}, {}→{}, 発生時刻={}",
                event.getTicketId(),
                event.getOldDueDate(),
                event.getNewDueDate(),
                event.getOccurredOn());
    }
}
