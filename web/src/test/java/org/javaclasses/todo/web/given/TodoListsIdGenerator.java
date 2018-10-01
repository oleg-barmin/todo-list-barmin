package org.javaclasses.todo.web.given;

import org.javaclasses.todo.model.TodoListId;

import java.util.UUID;

/**
 * Generates unique {@link TodoListId}.
 *
 * @author Oleg Barmin
 * @implNote ID generation is based on {@link UUID}
 */
public class TodoListsIdGenerator {

    private TodoListsIdGenerator() {
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
