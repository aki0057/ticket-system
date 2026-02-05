package com.example.ticket.domain.model.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TicketType のテスト")
class TicketTypeTest {

    // ========================================================================
    // fromPrefix テスト
    // ========================================================================
    @Nested
    @DisplayName("fromPrefix のテスト")
    class FromPrefixTest {

        @Test
        @DisplayName("正常系: fromPrefix('I') は INCIDENT を返す")
        void fromPrefix_INCIDENTプレフィックス() {
            // arrange
            String prefix = "I";

            // act
            TicketType result = TicketType.fromPrefix(prefix);

            // assert
            assertThat(result).isEqualTo(TicketType.INCIDENT);
        }

        @Test
        @DisplayName("正常系: fromPrefix('Q') は INQUIRY を返す")
        void fromPrefix_INQUIRYプレフィックス() {
            // arrange
            String prefix = "Q";

            // act
            TicketType result = TicketType.fromPrefix(prefix);

            // assert
            assertThat(result).isEqualTo(TicketType.INQUIRY);
        }

        @Test
        @DisplayName("正常系: fromPrefix('M') は MAINTENANCE を返す")
        void fromPrefix_MAINTENANCEプレフィックス() {
            // arrange
            String prefix = "M";

            // act
            TicketType result = TicketType.fromPrefix(prefix);

            // assert
            assertThat(result).isEqualTo(TicketType.MAINTENANCE);
        }

        @Test
        @DisplayName("正常系: fromPrefix('O') は OTHER を返す")
        void fromPrefix_OTHERプレフィックス() {
            // arrange
            String prefix = "O";

            // act
            TicketType result = TicketType.fromPrefix(prefix);

            // assert
            assertThat(result).isEqualTo(TicketType.OTHER);
        }

        @Test
        @DisplayName("異常系: 不正なプレフィックスで IllegalArgumentException をスロー")
        void fromPrefix_不正なプレフィックス() {
            // arrange
            String invalidPrefix = "INVALID";

            // act & assert
            assertThatThrownBy(() -> TicketType.fromPrefix(invalidPrefix))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(invalidPrefix);
        }

        @Test
        @DisplayName("異常系: 空文字プレフィックスで IllegalArgumentException をスロー")
        void fromPrefix_空文字プレフィックス() {
            // act & assert
            assertThatThrownBy(() -> TicketType.fromPrefix(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ========================================================================
    // getDisplayName テスト
    // ========================================================================
    @Nested
    @DisplayName("getDisplayName のテスト")
    class GetDisplayNameTest {

        @Test
        @DisplayName("INCIDENT の表示名は '障害'")
        void getDisplayName_INCIDENT() {
            // act & assert
            assertThat(TicketType.INCIDENT.getDisplayName()).isEqualTo("障害");
        }

        @Test
        @DisplayName("INQUIRY の表示名は '問い合わせ'")
        void getDisplayName_INQUIRY() {
            // act & assert
            assertThat(TicketType.INQUIRY.getDisplayName()).isEqualTo("問い合わせ");
        }

        @Test
        @DisplayName("MAINTENANCE の表示名は '保守'")
        void getDisplayName_MAINTENANCE() {
            // act & assert
            assertThat(TicketType.MAINTENANCE.getDisplayName()).isEqualTo("保守");
        }

        @Test
        @DisplayName("OTHER の表示名は 'その他'")
        void getDisplayName_OTHER() {
            // act & assert
            assertThat(TicketType.OTHER.getDisplayName()).isEqualTo("その他");
        }
    }

    // ========================================================================
    // getPrefix テスト
    // ========================================================================
    @Nested
    @DisplayName("getPrefix のテスト")
    class GetPrefixTest {

        @Test
        @DisplayName("INCIDENT のプレフィックスは 'I'")
        void getPrefix_INCIDENT() {
            // act & assert
            assertThat(TicketType.INCIDENT.getPrefix()).isEqualTo("I");
        }

        @Test
        @DisplayName("INQUIRY のプレフィックスは 'Q'")
        void getPrefix_INQUIRY() {
            // act & assert
            assertThat(TicketType.INQUIRY.getPrefix()).isEqualTo("Q");
        }

        @Test
        @DisplayName("MAINTENANCE のプレフィックスは 'M'")
        void getPrefix_MAINTENANCE() {
            // act & assert
            assertThat(TicketType.MAINTENANCE.getPrefix()).isEqualTo("M");
        }

        @Test
        @DisplayName("OTHER のプレフィックスは 'O'")
        void getPrefix_OTHER() {
            // act & assert
            assertThat(TicketType.OTHER.getPrefix()).isEqualTo("O");
        }
    }
}
