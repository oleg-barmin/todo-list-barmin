package org.javaclasses.todo.model;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * @author Oleg Barmin
 */
/* Duplicate literals of hashcode and equals test were left for better readability.*/
@SuppressWarnings("DuplicateStringLiteralInspection")
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

    @Nested
    @DisplayName("TodoListBuilder should")
    class TodoListBuilder {

        @Test
        void tesBuildTodoList() {
            TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());
            UserId ownerId = new UserId(UUID.randomUUID().toString());

            TodoList build = new TodoList.TodoListBuilder()
                    .setTodoListId(todoListId)
                    .setOwner(ownerId)
                    .build();

            Assertions.assertEquals(todoListId, build.getId());
            Assertions.assertEquals(ownerId, build.getOwner());
        }
    }

}