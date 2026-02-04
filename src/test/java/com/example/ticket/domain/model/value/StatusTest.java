package com.example.ticket.domain.model.value;

import com.example.ticket.domain.exception.InvalidStatusTransitionException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatNoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Status のテスト")
class StatusTest {

    // ========================================================================
    // getDisplayName のテスト
    // ========================================================================
    @Nested
    @DisplayName("getDisplayName のテスト")
    class GetDisplayNameTest {

        @ParameterizedTest
        @CsvSource({
                "NEW,新規",
                "IN_PROGRESS,対応中",
                "ON_HOLD,保留",
                "RESOLVED,解決"
        })
        @DisplayName("各状態の表示名が正しく返される")
        void getDisplayName(String statusName, String expectedDisplayName) {
            // Arrange
            Status status = Status.valueOf(statusName);

            // Act
            String result = status.getDisplayName();

            // Assert
            assertThat(result).isEqualTo(expectedDisplayName);
        }
    }

    // ========================================================================
    // validateTransition のテスト
    // ========================================================================
    @Nested
    @DisplayName("validateTransition のテスト")
    class ValidateTransitionTest {

        @ParameterizedTest
        @CsvSource({
                "NEW,NEW,false",
                "NEW,IN_PROGRESS,true",
                "NEW,ON_HOLD,false",
                "NEW,RESOLVED,false",
                "IN_PROGRESS,NEW,false",
                "IN_PROGRESS,IN_PROGRESS,true",
                "IN_PROGRESS,ON_HOLD,true",
                "IN_PROGRESS,RESOLVED,true",
                "ON_HOLD,NEW,false",
                "ON_HOLD,IN_PROGRESS,true",
                "ON_HOLD,ON_HOLD,true",
                "ON_HOLD,RESOLVED,false",
                "RESOLVED,NEW,false",
                "RESOLVED,IN_PROGRESS,false",
                "RESOLVED,ON_HOLD,false",
                "RESOLVED,RESOLVED,true"
        })
        @DisplayName("状態遷移が遷移ルールに従う")
        void validateTransition(String fromStatusName, String toStatusName, boolean shouldSucceed) {
            // Arrange
            Status from = Status.valueOf(fromStatusName);
            Status to = Status.valueOf(toStatusName);

            // Act & Assert
            if (shouldSucceed) {
                assertThatNoException()
                        .isThrownBy(() -> Status.validateTransition(from, to));
            } else {
                assertThatThrownBy(() -> Status.validateTransition(from, to))
                        .isInstanceOf(InvalidStatusTransitionException.class);
            }
        }
    }

    // ========================================================================
    // canTransitionTo のテスト
    // ========================================================================
    @Nested
    @DisplayName("canTransitionTo のテスト")
    class CanTransitionToTest {

        @ParameterizedTest
        @CsvSource({
                "NEW,NEW,false",
                "NEW,IN_PROGRESS,true",
                "NEW,ON_HOLD,false",
                "NEW,RESOLVED,false",
                "IN_PROGRESS,NEW,false",
                "IN_PROGRESS,IN_PROGRESS,true",
                "IN_PROGRESS,ON_HOLD,true",
                "IN_PROGRESS,RESOLVED,true",
                "ON_HOLD,NEW,false",
                "ON_HOLD,IN_PROGRESS,true",
                "ON_HOLD,ON_HOLD,true",
                "ON_HOLD,RESOLVED,false",
                "RESOLVED,NEW,false",
                "RESOLVED,IN_PROGRESS,false",
                "RESOLVED,ON_HOLD,false",
                "RESOLVED,RESOLVED,true"
        })
        @DisplayName("遷移の可否が正しく判定される")
        void canTransitionTo(String fromStatusName, String toStatusName, boolean expected) {
            // Arrange
            Status from = Status.valueOf(fromStatusName);
            Status to = Status.valueOf(toStatusName);

            // Act
            boolean result = from.canTransitionTo(to);

            // Assert
            assertThat(result).isEqualTo(expected);
        }
    }
}
