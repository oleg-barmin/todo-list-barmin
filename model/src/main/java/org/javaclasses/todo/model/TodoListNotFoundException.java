package org.javaclasses.todo.model;

import org.javaclasses.todo.model.entity.TodoListId;

/**
 * Occurs when try when {@code TodoList} with desired {@code TodoListId} was not found in the system.
 *
 * @author Oleg Barmin
 */
public class TodoListNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code TodoListNotFoundException} instance.
     *
     * @param todoListId ID of to-do list which was not found.
     */
    TodoListNotFoundException(TodoListId todoListId) {
        super(String.format("TodoList with ID: '%s' was not found.", todoListId));
    }
}
