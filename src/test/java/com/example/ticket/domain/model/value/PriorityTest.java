package com.example.ticket.domain.model.value;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 優先度値オブジェクトのテスト
 */
class PriorityTest {
    
    @Test
    void 高優先度のSLA時間は4時間である() {
        assertEquals(4, Priority.HIGH.getSlaHours());
    }
    
    @Test
    void 中優先度のSLA時間は24時間である() {
        assertEquals(24, Priority.MEDIUM.getSlaHours());
    }
    
    @Test
    void 低優先度のSLA時間は72時間である() {
        assertEquals(72, Priority.LOW.getSlaHours());
    }
    
    @Test
    void 低優先度をエスカレートすると中優先度になる() {
        assertEquals(Priority.MEDIUM, Priority.LOW.escalate());
    }
    
    @Test
    void 中優先度をエスカレートすると高優先度になる() {
        assertEquals(Priority.HIGH, Priority.MEDIUM.escalate());
    }
    
    @Test
    void 高優先度をエスカレートしても高優先度のまま() {
        assertEquals(Priority.HIGH, Priority.HIGH.escalate());
    }
    
    @Test
    void 高優先度をデエスカレートすると中優先度になる() {
        assertEquals(Priority.MEDIUM, Priority.HIGH.deEscalate());
    }
    
    @Test
    void 中優先度をデエスカレートすると低優先度になる() {
        assertEquals(Priority.LOW, Priority.MEDIUM.deEscalate());
    }
    
    @Test
    void 低優先度をデエスカレートしても低優先度のまま() {
        assertEquals(Priority.LOW, Priority.LOW.deEscalate());
    }
}
