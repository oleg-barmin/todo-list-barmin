package org.javaclasses.todo.model;

import org.javaclasses.todo.storage.impl.TodoListStorage;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Validates if user has access to entity with given ID.
 *
 * @author Oleg Barmin
 */
class Authorization {

    private final TodoListStorage todoListStorage;

    /**
     * Creates {@code Authorization} instance.
     *
     * @param todoListStorage storage of to-do lists
     */
    Authorization(TodoListStorage todoListStorage) {
        this.todoListStorage = checkNotNull(todoListStorage);
    }

    /**
     * Validates if user with given ID has access to {@code TodoList} with given ID.
     *
     * @param userId     user which tries to access {@code TodoList}
     * @param todoListId ID of {@code TodoList}
     * @throws TodoListNotFoundException    if {@code TodoList} with given ID was not found
     * @throws AuthorizationFailedException if user with given ID has no access to {@code TodoList} with given ID
     */
    void validateAccess(UserId userId, TodoListId todoListId) {
        checkNotNull(userId);
        checkNotNull(todoListId);

        Optional<TodoList> optionalTodoList = todoListStorage.read(todoListId);

        if (!optionalTodoList.isPresent()) {
            throw new TodoListNotFoundException(todoListId);
        }

        UserId owner = optionalTodoList.get()
                                       .getOwner();

        if (!owner.equals(userId)) {
            throw new AuthorizationFailedException(userId, todoListId);
        }
    }
}
