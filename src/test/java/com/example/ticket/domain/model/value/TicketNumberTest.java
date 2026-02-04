package com.example.ticket.domain.model.value;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TicketNumber のテスト")
class TicketNumberTest {

    /**
     * テスト用固定日付
     */
    private static final LocalDate TEST_DATE = LocalDate.of(2026, 1, 1);

    // ========================================================================
    // generate テスト
    // ========================================================================
    @Nested
    @DisplayName("generate のテスト")
    class GenerateTest {

        // ====================================================================
        // 日付部分（8桁）のテスト
        // ====================================================================
        @Test
        @DisplayName("異常系: 日付が null で IllegalArgumentException をスロー")
        void generate_日付がnull() {
            // arrange
            LocalDate date = null;
            TicketType type = TicketType.INCIDENT;
            int sequenceNo = 1;

            // act & assert
            assertThatThrownBy(() -> TicketNumber.generate(date, type, sequenceNo))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        // ====================================================================
        // プレフィックス部分（1文字）のテスト
        // ====================================================================
        @Test
        @DisplayName("正常系: INCIDENT プレフィックスで正しい形式を生成")
        void generate_INCIDENTプレフィックス() {
            // arrange
            LocalDate date = TEST_DATE;
            TicketType type = TicketType.INCIDENT;
            int sequenceNo = 1;

            // act
            TicketNumber result = TicketNumber.generate(date, type, sequenceNo);

            // assert
            assertThat(result.getValue()).isEqualTo("20260101I001");
        }

        @Test
        @DisplayName("正常系: INQUIRY プレフィックスで正しい形式を生成")
        void generate_INQUIRYプレフィックス() {
            // arrange
            LocalDate date = TEST_DATE;
            TicketType type = TicketType.INQUIRY;
            int sequenceNo = 1;

            // act
            TicketNumber result = TicketNumber.generate(date, type, sequenceNo);

            // assert
            assertThat(result.getValue()).isEqualTo("20260101Q001");
        }

        @Test
        @DisplayName("正常系: MAINTENANCE プレフィックスで正しい形式を生成")
        void generate_MAINTENANCEプレフィックス() {
            // arrange
            LocalDate date = TEST_DATE;
            TicketType type = TicketType.MAINTENANCE;
            int sequenceNo = 1;

            // act
            TicketNumber result = TicketNumber.generate(date, type, sequenceNo);

            // assert
            assertThat(result.getValue()).isEqualTo("20260101M001");
        }

        @Test
        @DisplayName("正常系: OTHER プレフィックスで正しい形式を生成")
        void generate_OTHERプレフィックス() {
            // arrange
            LocalDate date = TEST_DATE;
            TicketType type = TicketType.OTHER;
            int sequenceNo = 1;

            // act
            TicketNumber result = TicketNumber.generate(date, type, sequenceNo);

            // assert
            assertThat(result.getValue()).isEqualTo("20260101O001");
        }

        // ====================================================================
        // 連番部分（3桁）のテスト
        // ====================================================================
        @Test
        @DisplayName("正常系: 連番が3桁ゼロ埋めされる（1 → 001）")
        void generate_連番1桁() {
            // arrange
            LocalDate date = TEST_DATE;
            TicketType type = TicketType.INCIDENT;
            int sequenceNo = 1;

            // act
            TicketNumber result = TicketNumber.generate(date, type, sequenceNo);

            // assert
            assertThat(result.getValue()).isEqualTo("20260101I001");
        }

        @Test
        @DisplayName("正常系: 連番が3桁ゼロ埋めされる（99 → 099）")
        void generate_連番2桁() {
            // arrange
            LocalDate date = TEST_DATE;
            TicketType type = TicketType.INCIDENT;
            int sequenceNo = 99;

            // act
            TicketNumber result = TicketNumber.generate(date, type, sequenceNo);

            // assert
            assertThat(result.getValue()).isEqualTo("20260101I099");
        }

        @Test
        @DisplayName("正常系: 連番が3桁（999）")
        void generate_連番最大値() {
            // arrange
            LocalDate date = TEST_DATE;
            TicketType type = TicketType.INCIDENT;
            int sequenceNo = 999;

            // act
            TicketNumber result = TicketNumber.generate(date, type, sequenceNo);

            // assert
            assertThat(result.getValue()).isEqualTo("20260101I999");
        }

        @Test
        @DisplayName("異常系: チケットタイプが null で IllegalArgumentException をスロー")
        void generate_チケットタイプがnull() {
            // arrange
            LocalDate date = TEST_DATE;
            TicketType type = null;
            int sequenceNo = 1;

            // act & assert
            assertThatThrownBy(() -> TicketNumber.generate(date, type, sequenceNo))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 連番が0で IllegalArgumentException をスロー")
        void generate_連番0() {
            // arrange
            LocalDate date = TEST_DATE;
            TicketType type = TicketType.INCIDENT;
            int sequenceNo = 0;

            // act & assert
            assertThatThrownBy(() -> TicketNumber.generate(date, type, sequenceNo))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("0");
        }

        @Test
        @DisplayName("異常系: 連番が1000で IllegalArgumentException をスロー")
        void generate_連番1000() {
            // arrange
            LocalDate date = TEST_DATE;
            TicketType type = TicketType.INCIDENT;
            int sequenceNo = 1000;

            // act & assert
            assertThatThrownBy(() -> TicketNumber.generate(date, type, sequenceNo))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("1000");
        }

        @Test
        @DisplayName("異常系: 連番が負の数で IllegalArgumentException をスロー")
        void generate_連番負の数() {
            // arrange
            LocalDate date = TEST_DATE;
            TicketType type = TicketType.INCIDENT;
            int sequenceNo = -1;

            // act & assert
            assertThatThrownBy(() -> TicketNumber.generate(date, type, sequenceNo))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("-1");
        }
    }

    // ========================================================================
    // コンストラクタテスト
    // ========================================================================
    @Nested
    @DisplayName("コンストラクタのテスト")
    class ConstructorTest {

        @Test
        @DisplayName("正常系: 全プレフィックス（I, Q, M, O）が受け入れられる")
        void constructor_全プレフィックス() {
            // act & assert
            assertThat(new TicketNumber("20260101I001").getValue()).isEqualTo("20260101I001");
            assertThat(new TicketNumber("20260101Q001").getValue()).isEqualTo("20260101Q001");
            assertThat(new TicketNumber("20260101M001").getValue()).isEqualTo("20260101M001");
            assertThat(new TicketNumber("20260101O001").getValue()).isEqualTo("20260101O001");
        }

        @Test
        @DisplayName("異常系: null で IllegalArgumentException をスロー")
        void constructor_null() {
            // act & assert
            assertThatThrownBy(() -> new TicketNumber(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 空文字で IllegalArgumentException をスロー")
        void constructor_空文字() {
            // act & assert
            assertThatThrownBy(() -> new TicketNumber(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        // ====================================================================
        // 日付部分（8桁）のテスト
        // ====================================================================
        @Test
        @DisplayName("異常系: 日付部分が数字以外で IllegalArgumentException をスロー")
        void constructor_日付部分が数字以外() {
            // act & assert
            assertThatThrownBy(() -> new TicketNumber("2026010AI001"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 日付部分が7桁で IllegalArgumentException をスロー")
        void constructor_日付部分が7桁() {
            // act & assert
            assertThatThrownBy(() -> new TicketNumber("202601I001"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 日付部分が9桁で IllegalArgumentException をスロー")
        void constructor_日付部分が9桁() {
            // act & assert
            assertThatThrownBy(() -> new TicketNumber("202601011I001"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        // ====================================================================
        // プレフィックス部分（1文字）のテスト
        // ====================================================================
        @Test
        @DisplayName("異常系: 不正なプレフィックスで IllegalArgumentException をスロー")
        void constructor_不正なプレフィックス() {
            // act & assert
            assertThatThrownBy(() -> new TicketNumber("20260101X001"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: プレフィックスが数字で IllegalArgumentException をスロー")
        void constructor_プレフィックスが数字() {
            // act & assert
            assertThatThrownBy(() -> new TicketNumber("202601010001"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        // ====================================================================
        // 連番部分（3桁）のテスト
        // ====================================================================
        @Test
        @DisplayName("異常系: 連番部分が数字以外で IllegalArgumentException をスロー")
        void constructor_連番部分が数字以外() {
            // act & assert
            assertThatThrownBy(() -> new TicketNumber("20260101I00A"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 連番部分が2桁で IllegalArgumentException をスロー")
        void constructor_連番部分が2桁() {
            // act & assert
            assertThatThrownBy(() -> new TicketNumber("20260101I01"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 連番部分が4桁で IllegalArgumentException をスロー")
        void constructor_連番部分が4桁() {
            // act & assert
            assertThatThrownBy(() -> new TicketNumber("20260101I0001"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
