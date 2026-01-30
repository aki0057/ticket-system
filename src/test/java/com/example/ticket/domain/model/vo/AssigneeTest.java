package com.example.ticket.domain.model.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 担当者値オブジェクトのテスト
 */
class AssigneeTest {
    
    @Test
    void 有効なユーザーIDと名前で担当者を作成できる() {
        Assignee assignee = Assignee.of("user123", "山田太郎");
        
        assertEquals("user123", assignee.getUserId());
        assertEquals("山田太郎", assignee.getUserName());
    }
    
    @Test
    void ユーザーIDがnullの場合は例外が発生する() {
        assertThrows(IllegalArgumentException.class, () ->
            new Assignee(null, "山田太郎")
        );
    }
    
    @Test
    void ユーザーIDが空文字の場合は例外が発生する() {
        assertThrows(IllegalArgumentException.class, () ->
            new Assignee("", "山田太郎")
        );
    }
    
    @Test
    void ユーザー名がnullの場合は例外が発生する() {
        assertThrows(IllegalArgumentException.class, () ->
            new Assignee("user123", null)
        );
    }
    
    @Test
    void ユーザー名が空文字の場合は例外が発生する() {
        assertThrows(IllegalArgumentException.class, () ->
            new Assignee("user123", "")
        );
    }
    
    @Test
    void toStringは名前とIDを含む文字列を返す() {
        Assignee assignee = Assignee.of("user123", "山田太郎");
        String expected = "山田太郎 (user123)";
        
        assertEquals(expected, assignee.toString());
    }
}
