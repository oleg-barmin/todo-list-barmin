package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.storage.impl.TaskStorage;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * API which simplifies retrieving of all task which belongs to specified {@code TodoList} by its ID.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of public API and its methods should be public.
public final class ReadTasks extends Operation<ReadTasks> {

    private final TodoListId todoListId;
    private final TaskStorage taskStorage;
    private final Authorization authorization;

    /**
     * Creates {@code ReadTasks} instance.
     *
     * @param todoListId     ID of the {@code TodoList} which task should be found
     * @param taskStorage    storage to get desired tasks from
     * @param authorization  to validate access to {@code TodoList}
     * @param authentication to authenticate user token
     */
    ReadTasks(TodoListId todoListId, TaskStorage taskStorage, Authorization authorization, Authentication authentication) {
        super(authentication);
        this.taskStorage = checkNotNull(taskStorage);
        this.todoListId = checkNotNull(todoListId);
        this.authorization = checkNotNull(authorization);
    }

    /**
     * Provides list of tasks which belongs to {@code TodoList} with given ID.
     *
     * @return list of task which belongs to specified {@code TodoList}
     * @throws TodoListNotFoundException    if {@code TodoList} with given ID does not exist
     * @throws AuthorizationFailedException if user has no authority to read tasks from {@code TodoList} with given ID
     */
    public List<Task> execute() throws AuthorizationFailedException {
        authorization.validateAccess(validateToken(), todoListId);
        return taskStorage.getAllTaskOfTodoList(todoListId);
    }
}
