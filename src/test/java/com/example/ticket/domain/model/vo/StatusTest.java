package com.example.ticket.domain.model.vo;

import com.example.ticket.domain.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ステータス値オブジェクトのテスト（TDD）
 */
class StatusTest {
    
    @Test
    void 新規から対応中への遷移は成功する() {
        assertDoesNotThrow(() -> 
            Status.validateTransition(Status.NEW, Status.IN_PROGRESS)
        );
    }
    
    @Test
    void 新規から保留への遷移は成功する() {
        assertDoesNotThrow(() -> 
            Status.validateTransition(Status.NEW, Status.ON_HOLD)
        );
    }
    
    @Test
    void 新規から解決への遷移は失敗する() {
        assertThrows(InvalidStatusTransitionException.class, () ->
            Status.validateTransition(Status.NEW, Status.RESOLVED)
        );
    }
    
    @Test
    void 対応中から保留への遷移は成功する() {
        assertDoesNotThrow(() -> 
            Status.validateTransition(Status.IN_PROGRESS, Status.ON_HOLD)
        );
    }
    
    @Test
    void 対応中から解決への遷移は成功する() {
        assertDoesNotThrow(() -> 
            Status.validateTransition(Status.IN_PROGRESS, Status.RESOLVED)
        );
    }
    
    @Test
    void 保留から対応中への遷移は成功する() {
        assertDoesNotThrow(() -> 
            Status.validateTransition(Status.ON_HOLD, Status.IN_PROGRESS)
        );
    }
    
    @Test
    void 保留から新規への遷移は失敗する() {
        assertThrows(InvalidStatusTransitionException.class, () ->
            Status.validateTransition(Status.ON_HOLD, Status.NEW)
        );
    }
    
    @Test
    void 解決から再開への遷移は成功する() {
        assertDoesNotThrow(() -> 
            Status.validateTransition(Status.RESOLVED, Status.REOPENED)
        );
    }
    
    @Test
    void 解決から新規への遷移は失敗する() {
        assertThrows(InvalidStatusTransitionException.class, () ->
            Status.validateTransition(Status.RESOLVED, Status.NEW)
        );
    }
    
    @Test
    void 再開から対応中への遷移は成功する() {
        assertDoesNotThrow(() -> 
            Status.validateTransition(Status.REOPENED, Status.IN_PROGRESS)
        );
    }
    
    @Test
    void 再開から解決への遷移は成功する() {
        assertDoesNotThrow(() -> 
            Status.validateTransition(Status.REOPENED, Status.RESOLVED)
        );
    }
    
    @Test
    void 同じステータスへの遷移は成功する() {
        assertDoesNotThrow(() -> 
            Status.validateTransition(Status.NEW, Status.NEW)
        );
    }
    
    @Test
    void canTransitionToメソッドは正しく判定する() {
        assertTrue(Status.NEW.canTransitionTo(Status.IN_PROGRESS));
        assertFalse(Status.NEW.canTransitionTo(Status.RESOLVED));
    }
}
