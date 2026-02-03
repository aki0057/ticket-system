package com.example.ticket.domain.model.value;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * チケットID値オブジェクトのテスト
 */
class TicketIdTest {
    
    @Test
    void 有効な文字列からTicketIdを生成できる() {
        String validId = "ticket-123";
        TicketId ticketId = TicketId.of(validId);
        
        assertEquals(validId, ticketId.getValue());
    }
    
    @Test
    void nullの場合は例外が発生する() {
        assertThrows(IllegalArgumentException.class, () ->
            new TicketId(null)
        );
    }
    
    @Test
    void 空文字の場合は例外が発生する() {
        assertThrows(IllegalArgumentException.class, () ->
            new TicketId("")
        );
    }
    
    @Test
    void 空白のみの場合は例外が発生する() {
        assertThrows(IllegalArgumentException.class, () ->
            new TicketId("   ")
        );
    }
    
    @Test
    void generateメソッドで一意なIDが生成される() {
        TicketId id1 = TicketId.generate();
        TicketId id2 = TicketId.generate();
        
        assertNotNull(id1.getValue());
        assertNotNull(id2.getValue());
        assertNotEquals(id1.getValue(), id2.getValue());
    }
    
    @Test
    void toStringは値を返す() {
        String value = "test-id";
        TicketId ticketId = TicketId.of(value);
        
        assertEquals(value, ticketId.toString());
    }
}
