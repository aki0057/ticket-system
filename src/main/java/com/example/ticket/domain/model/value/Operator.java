package com.example.ticket.domain.model.value;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * オペレーター値オブジェクト
 * ヘルプデスクでチケット操作を行う担当者を表現
 */
@Value
@EqualsAndHashCode(of = "operatorId")
public class Operator {
    @NonNull
    OperatorId operatorId;
    String operatorName;

    public Operator(@NonNull OperatorId operatorId, @NonNull String operatorName) {
        if (operatorName.isBlank()) {
            throw new IllegalArgumentException("オペレーター名が空です");
        }
        this.operatorId = operatorId;
        this.operatorName = operatorName;
    }
}
