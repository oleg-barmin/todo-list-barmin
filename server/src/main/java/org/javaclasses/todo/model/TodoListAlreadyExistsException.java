package org.javaclasses.todo.model;

/**
 * Occurs when attempt to add TodoList with ID which already exists in storage
 */
class TodoListAlreadyExistsException extends RuntimeException {

    /**
     * Creates `TodoListAlreadyExistsException` instance.
     *
     * @param todoListId ID of `TodoList` which already exists
     */
    TodoListAlreadyExistsException(TodoListId todoListId) {
        super("TodoList with ID: '" + todoListId + "' already exists.");
    }
}
