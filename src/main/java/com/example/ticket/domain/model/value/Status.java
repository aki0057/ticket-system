package com.example.ticket.domain.model.value;

import com.example.ticket.domain.exception.InvalidStatusTransitionException;

/**
 * チケットの状態を表す値オブジェクト
 * 
 * 状態遷移ルールをドメイン層で定義し、不正な遷移を防止する。
 * 詳細な状態遷移ルール、ビジネスシナリオについては {@code USAGE.md} を参照。
 * 
 * @see InvalidStatusTransitionException 不正な遷移時の例外
 */
public enum Status {
    NEW("新規"),
    IN_PROGRESS("対応中"),
    ON_HOLD("保留"),
    RESOLVED("解決");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    /**
     * UI表示用の日本語名を返す。
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 指定された状態遷移の妥当性を検証する。
     * 
     * 不正な遷移が検出された場合は例外をスロー。
     * 
     * @param from 現在の状態
     * @param to   遷移先の状態
     * @throws InvalidStatusTransitionException 不正な遷移の場合
     */
    public static void validateTransition(Status from, Status to) {
        boolean isValid = switch (from) {
            case NEW -> to == IN_PROGRESS;
            case IN_PROGRESS -> to == IN_PROGRESS || to == ON_HOLD || to == RESOLVED;
            case ON_HOLD -> to == ON_HOLD || to == IN_PROGRESS;
            case RESOLVED -> to == RESOLVED; // 事後処理のため RESOLVED → RESOLVED のみ許可
        };

        if (!isValid) {
            throw new InvalidStatusTransitionException(
                    String.format("Invalid status transition: %s -> %s", from, to));
        }
    }

    /**
     * 指定された状態への遷移が可能かどうかを判定する。
     * 
     * {@link #validateTransition(Status, Status)} のラッパー。
     * 例外ではなくブール値で結果を返す。
     * 
     * @param to 遷移先の状態
     * @return 遷移が許可されている場合は true、不許可の場合は false
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
