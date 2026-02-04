package com.example.ticket.domain.model.value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * チケット番号値オブジェクト
 */
@Getter
@EqualsAndHashCode
@ToString
public class TicketNumber {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int MIN_SEQUENCE_NO = 1;
    private static final int MAX_SEQUENCE_NO = 999;
    private static final int TOTAL_LENGTH = 12;

    private final String value;

    /**
     * チケット番号を生成
     * 
     * @param date       日付
     * @param type       チケット種別
     * @param sequenceNo 連番(1-999)
     * @return 生成されたチケット番号
     */
    public static TicketNumber generate(LocalDate date, TicketType type, int sequenceNo) {
        if (date == null) {
            throw new IllegalArgumentException("日付がnullです");
        }
        if (type == null) {
            throw new IllegalArgumentException("チケット種別がnullです");
        }
        if (sequenceNo < MIN_SEQUENCE_NO || sequenceNo > MAX_SEQUENCE_NO) {
            throw new IllegalArgumentException("連番は1から999の範囲で指定してください: " + sequenceNo);
        }

        String datePart = date.format(DATE_FORMAT);
        String prefix = type.getPrefix();
        String seqPart = String.format("%03d", sequenceNo);
        return new TicketNumber(datePart + prefix + seqPart);
    }

    public TicketNumber(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("チケット番号がnullまたは空文字です");
        }
        validateFormat(value);
        this.value = value;
    }

    private static void validateFormat(String value) {
        if (value.length() != TOTAL_LENGTH) {
            throw new IllegalArgumentException("チケット番号の長さが不正です（12文字である必要があります）: " + value.length());
        }

        String datePart = value.substring(0, 8);
        String prefix = value.substring(8, 9);
        String seqPart = value.substring(9, 12);

        if (!datePart.matches("\\d{8}")) {
            throw new IllegalArgumentException("日付部分の形式が不正です: " + datePart);
        }

        TicketType.fromPrefix(prefix);

        if (!seqPart.matches("\\d{3}")) {
            throw new IllegalArgumentException("連番部分の形式が不正です: " + seqPart);
        }
    }
}
