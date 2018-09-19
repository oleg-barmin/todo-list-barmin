package org.javaclasses.todo.model;

/**
 * Occurs when attempt to add TodoList with ID which already exists in the storage.
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
        super("TodoList with ID: '" + todoListId + "' already exists.");
    }
}
