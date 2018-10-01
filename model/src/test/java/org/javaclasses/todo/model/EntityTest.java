package org.javaclasses.todo.model;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing equals and hashcode methods of {@link Entity} sub-classes.
 *
 * @author Oleg Barmin
 */
/* display name of equals and hashcode test left for better readability.*/
@SuppressWarnings("DuplicateStringLiteralInspection")
class EntityTest {

    @Nested
    @DisplayName("Token should")
    class TokenTest {

        @Test
        @DisplayName("perform equals and hashcode methods properly.")
        void testEqualsAndHashCode() {
            String token = UUID.randomUUID()
                               .toString();

            new EqualsTester()
                    .addEqualityGroup(
                            new Token(token),
                            new Token(token))
                    .testEquals();
        }
    }

    @Nested
    @DisplayName("Password should")
    class PasswordTest {

        @Test
        @DisplayName("perform equals and hashcode methods properly.")
        void testEqualsAndHashCode() {
            String password = "qwerty1234";

            new EqualsTester()
                    .addEqualityGroup(
                            new Password(password),
                            new Password(password))
                    .testEquals();
        }

        @Test
        @DisplayName("store given value of password.")
        void testGetValue() {
            String passwordStr = "securedPassword";

            Password password = new Password(passwordStr);

            assertEquals(passwordStr, password.getValue());
        }
    }

    @Nested
    @DisplayName("Username should")
    class UsernameTest {

        @Test
        @DisplayName("perform equals and hashcode methods properly.")
        void testEqualsAndHashCode() {
            String username = "oleg@mail.ua";

            new EqualsTester()
                    .addEqualityGroup(
                            new Username(username),
                            new Username(username))
                    .testEquals();
        }
    }
}