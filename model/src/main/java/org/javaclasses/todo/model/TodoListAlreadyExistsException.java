package org.javaclasses.todo.model;

import org.javaclasses.todo.model.entity.TodoListId;

/**
 * Occurs when try to add TodoList with ID which already exists in the storage.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of Public API should be public
public class TodoListAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code TodoListAlreadyExistsException} instance.
     *
     * @param todoListId ID of {@code TodoList} which already exists
     */
    TodoListAlreadyExistsException(TodoListId todoListId) {
        super(String.format("TodoList with ID: '%s' already exists.", todoListId));
    }
}
