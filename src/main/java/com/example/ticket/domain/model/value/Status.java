package com.example.ticket.domain.model.value;

import com.example.ticket.domain.exception.InvalidStatusTransitionException;

/**
 * ステータス値オブジェクト
 * 新規、対応中、保留、解決、再開
 */
public enum Status {
    NEW("新規"),
    IN_PROGRESS("対応中"),
    ON_HOLD("保留"),
    RESOLVED("解決"),
    REOPENED("再開");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * ステータス遷移の妥当性を検証
     * @param from 現在のステータス
     * @param to 遷移先のステータス
     * @throws InvalidStatusTransitionException 不正な遷移の場合
     */
    public static void validateTransition(Status from, Status to) {
        if (from == to) {
            return; // 同じステータスへの遷移は許可
        }

        boolean isValid = switch (from) {
            case NEW -> to == IN_PROGRESS || to == ON_HOLD;
            case IN_PROGRESS -> to == ON_HOLD || to == RESOLVED;
            case ON_HOLD -> to == IN_PROGRESS;
            case RESOLVED -> to == REOPENED;
            case REOPENED -> to == IN_PROGRESS || to == ON_HOLD || to == RESOLVED;
        };

        if (!isValid) {
            throw new InvalidStatusTransitionException(
                String.format("Invalid status transition: %s -> %s", from, to)
            );
        }
    }

    /**
     * 遷移可能かチェック
     */
    public boolean canTransitionTo(Status to) {
        try {
            validateTransition(this, to);
            return true;
        } catch (InvalidStatusTransitionException e) {
            return false;
        }
    }
}
