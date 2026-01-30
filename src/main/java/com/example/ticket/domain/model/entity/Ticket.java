package com.example.ticket.domain.model.entity;

import com.example.ticket.domain.event.*;
import com.example.ticket.domain.exception.InvalidStatusTransitionException;
import com.example.ticket.domain.model.vo.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * チケットエンティティ（集約ルート）
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {
    
    private TicketId id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private Assignee assignee;
    private DueDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private List<Comment> comments = new ArrayList<>();
    private List<DomainEvent> domainEvents = new ArrayList<>();
    
    // コンストラクタ
    public Ticket(TicketId id, String title, String description, Priority priority, DueDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        // イベント発行
        this.addDomainEvent(new TicketCreatedEvent(id.getValue(), title, priority, dueDate.getValue()));
    }
    
    /**
     * ステータス変更
     */
    public void changeStatus(Status newStatus) {
        Status oldStatus = this.status;
        Status.validateTransition(this.status, newStatus);
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
        
        // イベント発行
        this.addDomainEvent(new StatusChangedEvent(id.getValue(), oldStatus, newStatus));
        
        // 解決イベント
        if (newStatus == Status.RESOLVED) {
            this.addDomainEvent(new TicketResolvedEvent(id.getValue()));
        }
        
        // 再開イベント
        if (newStatus == Status.REOPENED) {
            this.addDomainEvent(new TicketReopenedEvent(id.getValue()));
        }
    }
    
    /**
     * 担当者割当
     */
    public void assign(Assignee assignee) {
        Assignee oldAssignee = this.assignee;
        this.assignee = assignee;
        this.updatedAt = LocalDateTime.now();
        
        // イベント発行
        this.addDomainEvent(new AssigneeChangedEvent(
            id.getValue(), 
            oldAssignee != null ? oldAssignee.getUserId() : null,
            assignee.getUserId()
        ));
    }
    
    /**
     * 優先度変更
     */
    public void changePriority(Priority newPriority) {
        Priority oldPriority = this.priority;
        this.priority = newPriority;
        this.updatedAt = LocalDateTime.now();
        
        // 期限も調整
        this.dueDate = DueDate.fromNowPlusHours(newPriority.getSlaHours());
        
        // イベント発行
        this.addDomainEvent(new PriorityChangedEvent(id.getValue(), oldPriority, newPriority));
    }
    
    /**
     * 期限延長
     */
    public void extendDueDate(int hours) {
        DueDate oldDueDate = this.dueDate;
        this.dueDate = this.dueDate.extend(hours);
        this.updatedAt = LocalDateTime.now();
        
        // イベント発行
        this.addDomainEvent(new DueDateExtendedEvent(
            id.getValue(), 
            oldDueDate.getValue(), 
            this.dueDate.getValue()
        ));
    }
    
    /**
     * コメント追加
     */
    public void addComment(Comment comment) {
        this.comments.add(comment);
        this.updatedAt = LocalDateTime.now();
        
        // イベント発行
        this.addDomainEvent(new CommentAddedEvent(
            id.getValue(), 
            comment.getAuthor(), 
            comment.getContent()
        ));
    }
    
    /**
     * チケット解決
     */
    public void resolve() {
        changeStatus(Status.RESOLVED);
    }
    
    /**
     * チケット再開
     */
    public void reopen() {
        if (this.status != Status.RESOLVED) {
            throw new InvalidStatusTransitionException("Only resolved tickets can be reopened");
        }
        changeStatus(Status.REOPENED);
    }
    
    /**
     * SLA違反チェック
     */
    public boolean isSlaViolated() {
        return dueDate.isOverdue() && status != Status.RESOLVED;
    }
    
    /**
     * エスカレーション
     */
    public void escalate() {
        changePriority(priority.escalate());
    }
    
    // ドメインイベント管理
    private void addDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }
    
    public List<DomainEvent> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }
    
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
