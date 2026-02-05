package com.example.ticket.domain.model.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Contact のテスト")
class ContactTest {

    // ========================================================================
    // コンストラクタのテスト
    // ========================================================================
    @Nested
    @DisplayName("コンストラクタのテスト")
    class ConstructorTest {

        @Test
        @DisplayName("正常系: 有効な連絡先名で連絡先を作成できる")
        void constructor_有効な連絡先名で連絡先を作成できる() {
            // arrange
            String contactName = "ABC社 山田太郎";

            // act
            Contact contact = new Contact(contactName);

            // assert
            assertThat(contact.getContactName()).isEqualTo("ABC社 山田太郎");
        }

        @Test
        @DisplayName("異常系: 連絡先名が空文字で IllegalArgumentException をスロー")
        void constructor_連絡先名が空文字() {
            // act & assert
            assertThatThrownBy(() -> new Contact(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 連絡先名が空白のみで IllegalArgumentException をスロー")
        void constructor_連絡先名が空白のみ() {
            // act & assert
            assertThatThrownBy(() -> new Contact("   "))
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
        @DisplayName("正常系: 同じ名前の連絡先は等しい")
        void equals_同じ名前の連絡先は等しい() {
            // arrange
            Contact contact1 = new Contact("ABC社 山田太郎");
            Contact contact2 = new Contact("ABC社 山田太郎");

            // act & assert
            assertThat(contact1).isEqualTo(contact2);
            assertThat(contact1.hashCode()).isEqualTo(contact2.hashCode());
        }

        @Test
        @DisplayName("正常系: 異なる名前の連絡先は等しくない")
        void equals_異なる名前の連絡先は等しくない() {
            // arrange
            Contact contact1 = new Contact("ABC社 山田太郎");
            Contact contact2 = new Contact("ABC社 山田次郎");

            // act & assert
            assertThat(contact1).isNotEqualTo(contact2);
        }
    }
}
