package com.example.ticket.domain.model.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OperatorId のテスト")
class OperatorIdTest {

    // ========================================================================
    // コンストラクタのテスト
    // ========================================================================
    @Nested
    @DisplayName("コンストラクタのテスト")
    class ConstructorTest {

        @Test
        @DisplayName("正常系: ID00001（最小値）で作成できる")
        void constructor_最小値() {
            // arrange
            String value = "ID00001";

            // act
            OperatorId operatorId = new OperatorId(value);

            // assert
            assertThat(operatorId.getValue()).isEqualTo("ID00001");
        }

        @Test
        @DisplayName("正常系: ID99999（最大値）で作成できる")
        void constructor_最大値() {
            // arrange
            String value = "ID99999";

            // act
            OperatorId operatorId = new OperatorId(value);

            // assert
            assertThat(operatorId.getValue()).isEqualTo("ID99999");
        }

        @Test
        @DisplayName("異常系: 空文字で IllegalArgumentException をスロー")
        void constructor_空文字() {
            // act & assert
            assertThatThrownBy(() -> new OperatorId(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 空白のみで IllegalArgumentException をスロー")
        void constructor_空白のみ() {
            // act & assert
            assertThatThrownBy(() -> new OperatorId(" "))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        // ====================================================================
        // プレフィックス部分のテスト
        // ====================================================================
        @Test
        @DisplayName("異常系: プレフィックスが'ID'でない場合に IllegalArgumentException をスロー")
        void constructor_不正なプレフィックス() {
            // act & assert
            assertThatThrownBy(() -> new OperatorId("AA00001"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: プレフィックスが小文字の場合に IllegalArgumentException をスロー")
        void constructor_小文字プレフィックス() {
            // act & assert
            assertThatThrownBy(() -> new OperatorId("id00001"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        // ====================================================================
        // 数字部分（5桁）のテスト
        // ====================================================================
        @Test
        @DisplayName("異常系: ID00000で IllegalArgumentException をスロー")
        void constructor_ID00000は許容しない() {
            // act & assert
            assertThatThrownBy(() -> new OperatorId("ID00000"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 数字部分が4桁以下で IllegalArgumentException をスロー")
        void constructor_数字部分が4桁以下() {
            // act & assert
            assertThatThrownBy(() -> new OperatorId("ID0001"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 数字部分が6桁以上で IllegalArgumentException をスロー")
        void constructor_数字部分が6桁以上() {
            // act & assert
            assertThatThrownBy(() -> new OperatorId("ID100000"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 数字部分に数字以外が含まれる場合に IllegalArgumentException をスロー")
        void constructor_数字部分に数字以外() {
            // act & assert
            assertThatThrownBy(() -> new OperatorId("ID0000A"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ========================================================================
    // 等価性のテスト
    // ========================================================================
    @Nested
    @DisplayName("等価性のテスト")
    class EqualityTest {

        @Test
        @DisplayName("正常系: 同じ値のOperatorIdは等しい")
        void equals_同じ値のOperatorIdは等しい() {
            // arrange
            OperatorId operatorId1 = new OperatorId("ID00001");
            OperatorId operatorId2 = new OperatorId("ID00001");

            // act & assert
            assertThat(operatorId1).isEqualTo(operatorId2);
            assertThat(operatorId1.hashCode()).isEqualTo(operatorId2.hashCode());
        }

        @Test
        @DisplayName("正常系: 異なる値のOperatorIdは等しくない")
        void equals_異なる値のOperatorIdは等しくない() {
            // arrange
            OperatorId operatorId1 = new OperatorId("ID00001");
            OperatorId operatorId2 = new OperatorId("ID00002");

            // act & assert
            assertThat(operatorId1).isNotEqualTo(operatorId2);
        }
    }
}
