package org.javaclasses.todo.model;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

/**
 * @author Oleg Barmin
 */
@DisplayName("Entity should")
class EntityTest {

    @DisplayName("perform equals and hashcode methods properly.")
    @Test
    void testEntitiesEquals() {
        String username = "oleg@mail.ua";
        String password = "qwerty1234";
        String token = UUID.randomUUID().toString();

        String todoListId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        new EqualsTester().addEqualityGroup(new Password(password),
                new Password(password))
                .addEqualityGroup(
                        new Token(token),
                        new Token(token))
                .addEqualityGroup(
                        new Username(username),
                        new Username(username))
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

        String taskId = UUID.randomUUID().toString();
        String description = "description";
        Date creationDate = new Date();
        Date lastUpdateDate = new Date();

        new EqualsTester()
                .addEqualityGroup(
                        new Task.TaskBuilder()
                                .setTaskId(new TaskId(taskId))
                                .setTodoListId(new TodoListId(todoListId))
                                .setDescription(description)
                                .setCreationDate(creationDate)
                                .completed()
                                .setLastUpdateDate(lastUpdateDate)
                                .build(),
                        new Task.TaskBuilder()
                                .setTaskId(new TaskId(taskId))
                                .setTodoListId(new TodoListId(todoListId))
                                .setDescription(description)
                                .setCreationDate(creationDate)
                                .completed()
                                .setLastUpdateDate(lastUpdateDate)
                                .build()
                );
    }
}