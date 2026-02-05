package com.example.ticket.domain.model.value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.NonNull;
import lombok.Value;

/**
 * オペレーターID値オブジェクト
 */
@Value
public class OperatorId {

    private static final Pattern FORMAT = Pattern.compile("^ID(\\d{5})$");
    private static final int MIN_SEQUENCE = 1;
    private static final int MAX_SEQUENCE = 99999;

    @NonNull
    String value;

    public OperatorId(@NonNull String value) {
        if (value.isBlank()) {
            throw new IllegalArgumentException("オペレーターIDが空文字です");
        }
        validateFormat(value);
        this.value = value;
    }

    private static void validateFormat(String value) {
        Matcher matcher = FORMAT.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("オペレーターIDの形式が不正です: " + value);
        }

        int sequence = Integer.parseInt(matcher.group(1));
        if (sequence < MIN_SEQUENCE || sequence > MAX_SEQUENCE) {
            throw new IllegalArgumentException("オペレーターIDの連番が範囲外です: " + value);
        }
    }
}
