package org.javaclasses.todo.model;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * @author Oleg Barmin
 */
class EntityTest {

    @Nested
    @DisplayName("TodoList should")
    class TodoListTest {

        @Test
        @DisplayName("perform equals and hashcode methods properly.")
        void testEqualsAndHashCode() {
            String todoListId = UUID.randomUUID().toString();
            String userId = UUID.randomUUID().toString();


            new EqualsTester()
                    .addEqualityGroup(
                            new TodoList.TodoListBuilder()
                                    .setTodoListId(new TodoListId(todoListId))
                                    .setOwner(new UserId(userId))
                                    .build(),
                            new TodoList.TodoListBuilder()
                                    .setTodoListId(new TodoListId(todoListId))
                                    .setOwner(new UserId(userId))
                                    .build())
                    .testEquals();
        }
    }

    @Nested
    @DisplayName("Token should")
    class TokenTest {

        @Test
        @DisplayName("perform equals and hashcode methods properly.")
        void testEqualsAndHashCode() {
            String token = UUID.randomUUID().toString();

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