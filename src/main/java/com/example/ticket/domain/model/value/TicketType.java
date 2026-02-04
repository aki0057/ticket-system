package com.example.ticket.domain.model.value;

import java.util.Arrays;

/**
 * チケット種別値オブジェクト
 */
public enum TicketType {

    /** 障害 */
    INCIDENT("障害", "I"),

    /** 問い合わせ */
    INQUIRY("問い合わせ", "Q"),

    /** 保守 */
    MAINTENANCE("保守", "M"),

    /** その他 */
    OTHER("その他", "O");

    private final String displayName;
    private final String prefix;

    TicketType(String displayName, String prefix) {
        this.displayName = displayName;
        this.prefix = prefix;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPrefix() {
        return prefix;
    }

    /**
     * プレフィックスからチケット種別を取得
     * 
     * 【使用例】
     * チケット番号 "20260203I001" から "I" を抽出し、
     * TicketType.fromPrefix("I") でINCIDENTを取得
     * 
     * @param prefix プレフィックス（例: "I"）
     * @return 対応するチケット種別
     * @throws IllegalArgumentException 不正なプレフィックスの場合
     */
    public static TicketType fromPrefix(String prefix) {
        // Java Stream APIで全てのenum値を検索
        return Arrays.stream(values())
                .filter(type -> type.prefix.equals(prefix))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "プレフィックスが不正です（有効な値: I, Q, M, O）: " + prefix));
    }
}
