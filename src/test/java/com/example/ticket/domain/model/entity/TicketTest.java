package com.example.ticket.domain.model.entity;

import com.example.ticket.domain.exception.InvalidStatusTransitionException;
import com.example.ticket.domain.model.value.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * チケットエンティティのテスト
 */
class TicketTest {

    private Ticket ticket;
    private TicketId ticketId;

    @BeforeEach
    void setUp() {
        ticketId = TicketId.generate();
        DueDate dueDate = DueDate.fromNowPlusHours(24);
        ticket = new Ticket(ticketId, "テストチケット", "説明", dueDate);
    }

    @Test
    void チケット作成時のステータスは新規である() {
        assertEquals(Status.NEW, ticket.getStatus());
    }

    @Test
    void チケット作成時のイベントが記録される() {
        assertFalse(ticket.getDomainEvents().isEmpty());
        assertEquals("TicketCreated", ticket.getDomainEvents().get(0).getEventType());
    }

    @Test
    void 新規から対応中へのステータス変更が成功する() {
        ticket.changeStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, ticket.getStatus());
    }

    @Test
    void 不正なステータス遷移の場合は例外が発生する() {
        assertThrows(InvalidStatusTransitionException.class, () -> ticket.changeStatus(Status.RESOLVED));
    }

    @Test
    void 担当者を割り当てることができる() {
        Assignee assignee = Assignee.of("user123", "山田太郎");
        ticket.assign(assignee);

        assertEquals(assignee, ticket.getAssignee());
    }

    @Test
    void 期限を延長できる() {
        DueDate originalDueDate = ticket.getDueDate();
        ticket.extendDueDate(24);

        assertNotEquals(originalDueDate, ticket.getDueDate());
    }

    @Test
    void コメントを追加できる() {
        Comment comment = new Comment(ticketId.getValue(), "user123", "テストコメント");
        ticket.addComment(comment);

        assertEquals(1, ticket.getComments().size());
    }

    @Test
    void チケットを解決できる() {
        ticket.changeStatus(Status.IN_PROGRESS);
        ticket.resolve();

        assertEquals(Status.RESOLVED, ticket.getStatus());
    }

    @Test
    void 解決済みチケットを再開できる() {
        ticket.changeStatus(Status.IN_PROGRESS);
        ticket.resolve();
        ticket.reopen();

        assertEquals(Status.REOPENED, ticket.getStatus());
    }

    @Test
    void 解決済み以外のチケットを再開しようとすると例外が発生する() {
        assertThrows(InvalidStatusTransitionException.class, () -> ticket.reopen());
    }

    @Test
    void 期限切れかつ未解決の場合はSLA違反と判定される() {
        DueDate pastDueDate = DueDate.of(java.time.LocalDateTime.now().minusDays(1));
        Ticket overdueTicket = new Ticket(
                TicketId.generate(),
                "期限切れチケット",
                "説明",
                Priority.HIGH,
                pastDueDate);

        assertTrue(overdueTicket.isSlaViolated());
    }

    @Test
    void エスカレーションで優先度が上がる() {
        ticket.escalate();

        assertEquals(Priority.HIGH, ticket.getPriority());
    }

    @Test
    void イベントをクリアできる() {
        ticket.clearDomainEvents();

        assertTrue(ticket.getDomainEvents().isEmpty());
    }
}
