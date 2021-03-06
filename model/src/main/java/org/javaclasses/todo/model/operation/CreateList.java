package org.javaclasses.todo.model.operation;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.entity.TodoList;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.model.entity.UserId;
import org.javaclasses.todo.storage.impl.TodoListStorage;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides API which simplifies creation of new {@code TodoList}.
 *
 * <p>To create new {@code TodoList} these values should be provided:
 * - ID of the {@code TodoList} to create
 * - ID of user who owns this {@code TodoList}
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of public API and its methods should be public.
public final class CreateList extends Operation<CreateList> {

    private final TodoListStorage todoListStorage;
    private final TodoList.TodoListBuilder todoListBuilder;

    /**
     * Creates {@code CreateList} instance.
     *
     * @param todoListId      ID of {@code TodoList} to create
     * @param todoListStorage to store newly created list
     */
    public CreateList(TodoListId todoListId, TodoListStorage todoListStorage,
                      Authentication authentication) {
        super(authentication);
        this.todoListStorage = todoListStorage;
        checkNotNull(todoListId);

        todoListBuilder = new TodoList.TodoListBuilder()
                .setTodoListId(todoListId);
    }

    /**
     * Creates new {@code TodoList} with given values in storage.
     */
    public void execute() throws AuthorizationFailedException {
        UserId userId = validateToken();

        TodoList todoList = todoListBuilder
                .setOwner(userId)
                .build();

        todoListStorage.write(todoList);
    }
}
