package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.AuthorizationFailedException;
import org.javaclasses.todo.storage.impl.TaskStorage;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * API which simplifies retrieving of all task which belongs to specified {@code TodoList} by its ID.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of public API and its methods should be public.
public final class ReadTasks {

    private final TodoListId todoListId;
    private final TaskStorage taskStorage;
    private final AccessAuth accessAuth;
    private final UserId userId;

    /**
     * Creates {@code ReadTasks} instance.
     *
     * @param todoListId  ID of the {@code TodoList} which task should be found
     * @param taskStorage storage to get desired tasks from
     * @param accessAuth  to validate access
     * @param userId      ID of user who started operation
     */
    ReadTasks(TodoListId todoListId, TaskStorage taskStorage, AccessAuth accessAuth, UserId userId) {
        this.taskStorage = checkNotNull(taskStorage);
        this.todoListId = checkNotNull(todoListId);
        this.accessAuth = checkNotNull(accessAuth);
        this.userId = checkNotNull(userId);
    }

    /**
     * Provides list of tasks which belongs to {@code TodoList} with given ID.
     *
     * @return list of task which belongs to specified {@code TodoList}
     */
    public List<Task> execute() throws AuthorizationFailedException {
        accessAuth.validateAssess(userId, todoListId);
        return taskStorage.getAllTaskOfTodoList(todoListId);
    }
}
