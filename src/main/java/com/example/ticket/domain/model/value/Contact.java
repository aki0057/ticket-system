package com.example.ticket.domain.model.value;

import lombok.NonNull;
import lombok.Value;

/**
 * 連絡先値オブジェクト
 * オペレーターが連絡を取る相手（ベンダー、申告者、現地対応者など）を表現
 */
@Value
public class Contact {
    @NonNull
    String contactName;

    public Contact(@NonNull String contactName) {
        if (contactName.isBlank()) {
            throw new IllegalArgumentException("連絡先名が空です");
        }
        this.contactName = contactName;
    }
}
