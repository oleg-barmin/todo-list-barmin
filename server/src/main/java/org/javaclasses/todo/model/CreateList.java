package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.AuthorizationFailedException;
import org.javaclasses.todo.storage.impl.TodoListStorage;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides API which simplifies creation of new {@code TodoList}.
 *
 * <p>To create new {@code TodoList} these values should be provided:
 * - ID of the {@code TodoList} to create
 * - ID of user who owns this {@code TodoList}
 */
@SuppressWarnings("WeakerAccess") // part of public API and its methods should be public.
public final class CreateList {
    private final TodoListStorage todoListStorage;
    private TodoList.TodoListBuilder todoListBuilder;

    /**
     * Creates {@code CreateList} instance.
     *
     * @param todoListId      ID of {@code TodoList} to create
     * @param todoListStorage to store newly created list
     */
    CreateList(TodoListId todoListId, TodoListStorage todoListStorage) {
        this.todoListStorage = todoListStorage;
        checkNotNull(todoListId);

        todoListBuilder = new TodoList.TodoListBuilder()
                .setTodoListId(todoListId);
    }

    /**
     * Sets owner of the {@code TodoList} to create.
     *
     * @param userId ID of user who owns {@code TodoList}
     * @return this {@code CreateList} instance to continue request building
     */
    public CreateList withOwner(UserId userId) {
        todoListBuilder = todoListBuilder.setOwner(userId);
        return this;
    }

    /**
     * Creates new {@code TodoList} with given values in storage.
     */
    public void execute() throws AuthorizationFailedException {
        TodoList todoList = todoListBuilder.build();
        todoListStorage.write(todoList);
    }
}
