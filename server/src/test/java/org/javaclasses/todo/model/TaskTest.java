package org.javaclasses.todo.model;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/* Duplicate literals of hashcode and equals test were left for better readability.*/
@SuppressWarnings("DuplicateStringLiteralInspection")
@Nested
@DisplayName("Task should")
class TaskTest {

    @Test
    @DisplayName("perform equals and hashcode methods properly.")
    void testEqualsAndHashCode() {
        String taskId = UUID.randomUUID().toString();
        String todoListId = UUID.randomUUID().toString();

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
                )
                .testEquals();
    }

    @Nested
    @DisplayName("TaskBuilder should")
    class TaskBuilderTest {
        private static final String description = "task description";
        private final TaskId taskId = new TaskId(UUID.randomUUID().toString());
        private final TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());
        private final Date creationDate = new Date();
        private final Date lastUpdateDate = new Date();

        @Test
        @DisplayName("build tasks.")
        void testBuildTask() {
            Task build = new Task.TaskBuilder()
                    .setTaskId(taskId)
                    .setTodoListId(todoListId)
                    .setDescription(description)
                    .completed()
                    .setCreationDate(creationDate)
                    .setLastUpdateDate(lastUpdateDate)
                    .build();

            assertEquals(taskId, build.getId());
            assertEquals(todoListId, build.getTodoListId());
            assertEquals(description, build.getDescription());
            assertTrue(build.isCompleted());
            assertEquals(creationDate, build.getCreationDate());
            assertEquals(lastUpdateDate, build.getLastUpdateDate());
        }


        /* Build should throw NullPointerException if non-optional field is absent, so return value won't needed. */
        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Test
        @DisplayName("throw NullPointerException if non-optional field is not set.")
        void testThrowNullPointerWithoutCreationDate() {
            //ID is absent
            assertThrows(NullPointerException.class, () -> new Task.TaskBuilder()
                    .setTodoListId(todoListId)
                    .setDescription(description)
                    .completed()
                    .setCreationDate(creationDate)
                    .setLastUpdateDate(lastUpdateDate)
                    .build());

            //TodoList ID is absent
            assertThrows(NullPointerException.class, () -> new Task.TaskBuilder()
                    .setTaskId(taskId)
                    .setDescription(description)
                    .setCreationDate(creationDate)
                    .completed()
                    .setLastUpdateDate(lastUpdateDate)
                    .build());

            //Description is absent
            assertThrows(NullPointerException.class, () -> new Task.TaskBuilder()
                    .setTaskId(taskId)
                    .setTodoListId(todoListId)
                    .setCreationDate(creationDate)
                    .completed()
                    .setLastUpdateDate(lastUpdateDate)
                    .build());

            //Creation date is absent
            assertThrows(NullPointerException.class, () -> new Task.TaskBuilder()
                    .setTaskId(taskId)
                    .setTodoListId(todoListId)
                    .setDescription(description)
                    .completed()
                    .setLastUpdateDate(lastUpdateDate)
                    .build());
        }

        @Test
        @DisplayName("set default values to optional fields.")
        void testSetDefaultValues() {
            Task build = new Task.TaskBuilder()
                    .setTaskId(taskId)
                    .setTodoListId(todoListId)
                    .setDescription(description)
                    .setCreationDate(creationDate)
                    .build();

            assertFalse(build.isCompleted());
            assertNotNull(build.getLastUpdateDate());
        }
    }
}

