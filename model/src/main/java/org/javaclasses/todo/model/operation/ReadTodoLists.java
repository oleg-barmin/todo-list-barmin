package org.javaclasses.todo.model.operation;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.entity.TodoList;
import org.javaclasses.todo.model.entity.UserId;
import org.javaclasses.todo.storage.impl.TodoListStorage;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Reads all {@link TodoList TodoLists} of user.
 *
 * @author Oleg Barmin
 */
public class ReadTodoLists extends Operation<ReadTodoLists> {

    private final TodoListStorage todoListStorage;

    /**
     * Creates {@code ReadTodoLists} instance.
     *
     * @param todoListStorage storage to get desired {@code TodoList}s from
     * @param authentication  service to validate user token.
     */
    public ReadTodoLists(TodoListStorage todoListStorage, Authentication authentication) {
        super(authentication);
        this.todoListStorage = checkNotNull(todoListStorage);
    }

    /**
     * Reads all {@code TodoList}s of user.
     *
     * @return list of {@code TodoList}s of user whose token is
     * @throws AuthorizationFailedException if given token is not valid
     */
    public List<TodoList> execute() throws AuthorizationFailedException {
        UserId userId = validateToken();
        return todoListStorage.readTodoListsOf(userId);
    }

}
