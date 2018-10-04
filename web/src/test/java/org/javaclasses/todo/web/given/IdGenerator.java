package org.javaclasses.todo.web.given;

import org.javaclasses.todo.model.entity.TaskId;
import org.javaclasses.todo.model.entity.TodoListId;

import java.util.UUID;

/**
 * Generates unique IDs for {@code Entity} of to-do list application.
 *
 * @author Oleg Barmin
 */
public class IdGenerator {

    private IdGenerator() {
    }

    /**
     * Generates unique {@code TaskId}.
     *
     * @return unique {@code TaskId}
     */
    public static TaskId generateTaskId() {
        return new TaskId(UUID.randomUUID()
                              .toString());
    }

    /**
     * Generates unique {@code TodoListId}.
     *
     * @return unique {@code TodoListId}
     */
    public static TodoListId generateTodoListId() {
        return new TodoListId(UUID.randomUUID()
                                  .toString());
    }
}
