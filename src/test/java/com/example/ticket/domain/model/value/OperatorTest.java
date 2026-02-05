package com.example.ticket.domain.model.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Operator のテスト")
class OperatorTest {

    // ========================================================================
    // コンストラクタのテスト
    // ========================================================================
    @Nested
    @DisplayName("コンストラクタのテスト")
    class ConstructorTest {

        @Test
        @DisplayName("正常系: 有効なオペレーターIDと名前でオペレーターを作成できる")
        void constructor_有効なオペレーターIDと名前でオペレーターを作成できる() {
            // arrange
            OperatorId operatorId = new OperatorId("ID00001");
            String operatorName = "田中太郎";

            // act
            Operator operator = new Operator(operatorId, operatorName);

            // assert
            assertThat(operator.getOperatorId()).isEqualTo(new OperatorId("ID00001"));
            assertThat(operator.getOperatorName()).isEqualTo("田中太郎");
        }

        @Test
        @DisplayName("異常系: オペレーター名が空文字で IllegalArgumentException をスロー")
        void constructor_オペレーター名が空文字() {
            // act & assert
            assertThatThrownBy(() -> new Operator(new OperatorId("ID00001"), ""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: オペレーター名が空白のみで IllegalArgumentException をスロー")
        void constructor_オペレーター名が空白のみ() {
            // act & assert
            assertThatThrownBy(() -> new Operator(new OperatorId("ID00001"), " "))
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
        @DisplayName("正常系: 同じIDと名前のオペレーターは等しい")
        void equals_同じIDと名前のオペレーターは等しい() {
            // arrange
            Operator operator1 = new Operator(new OperatorId("ID00001"), "田中太郎");
            Operator operator2 = new Operator(new OperatorId("ID00001"), "田中太郎");

            // act & assert
            assertThat(operator1).isEqualTo(operator2);
            assertThat(operator1.hashCode()).isEqualTo(operator2.hashCode());
        }

        @Test
        @DisplayName("正常系: 同じIDで異なる名前のオペレーターは等しい（IDのみで判定）")
        void equals_同じIDで異なる名前のオペレーターは等しい() {
            // arrange
            Operator operator1 = new Operator(new OperatorId("ID00001"), "田中太郎");
            Operator operator2 = new Operator(new OperatorId("ID00001"), "田中次郎");

            // act & assert
            assertThat(operator1).isEqualTo(operator2);
            assertThat(operator1.hashCode()).isEqualTo(operator2.hashCode());
        }

        @Test
        @DisplayName("正常系: 異なるIDのオペレーターは等しくない")
        void equals_異なるIDのオペレーターは等しくない() {
            // arrange
            Operator operator1 = new Operator(new OperatorId("ID00001"), "田中太郎");
            Operator operator2 = new Operator(new OperatorId("ID00002"), "田中太郎");

            // act & assert
            assertThat(operator1).isNotEqualTo(operator2);
        }
    }

}
