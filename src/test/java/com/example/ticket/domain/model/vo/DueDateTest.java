package com.example.ticket.domain.model.vo;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 期限値オブジェクトのテスト
 */
class DueDateTest {
    
    @Test
    void 有効な日時で期限を作成できる() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
        DueDate dueDate = DueDate.of(dateTime);
        
        assertEquals(dateTime, dueDate.getValue());
    }
    
    @Test
    void nullの場合は例外が発生する() {
        assertThrows(IllegalArgumentException.class, () ->
            new DueDate(null)
        );
    }
    
    @Test
    void fromNowPlusHoursで現在時刻から指定時間後の期限を作成できる() {
        DueDate dueDate = DueDate.fromNowPlusHours(24);
        
        assertNotNull(dueDate.getValue());
        assertTrue(dueDate.getValue().isAfter(LocalDateTime.now()));
    }
    
    @Test
    void 過去の日時の場合は期限切れと判定される() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        DueDate dueDate = DueDate.of(pastDate);
        
        assertTrue(dueDate.isOverdue());
    }
    
    @Test
    void 未来の日時の場合は期限切れではないと判定される() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        DueDate dueDate = DueDate.of(futureDate);
        
        assertFalse(dueDate.isOverdue());
    }
    
    @Test
    void 期限まであと何時間かを計算できる() {
        DueDate dueDate = DueDate.fromNowPlusHours(24);
        long hours = dueDate.hoursUntilDue();
        
        assertTrue(hours >= 23 && hours <= 24);
    }
    
    @Test
    void 期限を延長できる() {
        LocalDateTime originalDate = LocalDateTime.now().plusDays(1);
        DueDate dueDate = DueDate.of(originalDate);
        
        DueDate extendedDueDate = dueDate.extend(24);
        
        assertEquals(originalDate.plusHours(24), extendedDueDate.getValue());
    }
}
