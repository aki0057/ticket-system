package com.example.ticket.domain.model.value;

/**
 * 優先度値オブジェクト
 * 高、中、低
 */
public enum Priority {
    HIGH("高", 4),
    MEDIUM("中", 24),
    LOW("低", 72);

    private final String displayName;
    private final int slaHours;

    Priority(String displayName, int slaHours) {
        this.displayName = displayName;
        this.slaHours = slaHours;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getSlaHours() {
        return slaHours;
    }

    /**
     * 優先度を一つ上げる
     */
    public Priority escalate() {
        return switch (this) {
            case LOW -> MEDIUM;
            case MEDIUM, HIGH -> HIGH;
        };
    }

    /**
     * 優先度を一つ下げる
     */
    public Priority deEscalate() {
        return switch (this) {
            case HIGH -> MEDIUM;
            case MEDIUM, LOW -> LOW;
        };
    }
}
