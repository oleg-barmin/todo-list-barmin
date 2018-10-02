package org.javaclasses.todo.model.operation;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.Authorization;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.TaskNotFoundException;
import org.javaclasses.todo.model.TodoListNotFoundException;
import org.javaclasses.todo.model.entity.Task;
import org.javaclasses.todo.model.entity.TaskId;
import org.javaclasses.todo.model.entity.UserId;
import org.javaclasses.todo.storage.impl.TaskStorage;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Finds task by ID.
 *
 * @author Oleg Barmin
 */
public final class FindTask extends Operation<FindTask> {

    private final TaskId taskId;
    private final Authorization authorization;
    private final TaskStorage taskStorage;

    /**
     * Creates {@code FindTask} instance.
     *
     * @param taskId         ID of the {@code Task} to find
     * @param taskStorage    storage to get tasks from
     * @param authorization  to validate if user has access to {@code Task} with given ID
     * @param authentication to validate user token
     */
    public FindTask(TaskId taskId, TaskStorage taskStorage,
                    Authorization authorization, Authentication authentication) {
        super(authentication);
        this.taskId = checkNotNull(taskId);
        this.taskStorage = checkNotNull(taskStorage);
        this.authorization = checkNotNull(authorization);
    }

    /**
     * Finds task with given ID.
     *
     * @return task with given ID
     * @throws TaskNotFoundException        if task to find doesn't exist
     * @throws TodoListNotFoundException    if {@code TodoList} with ID specified in task doesn't exist.
     * @throws AuthorizationFailedException if user has no authority to read {@code Task} with given ID
     */
    public Task execute() {
        UserId userId = validateToken();

        Optional<Task> optionalTask = taskStorage.read(taskId);

        if (!optionalTask.isPresent()) {
            throw new TaskNotFoundException(taskId);
        }

        Task task = optionalTask.get();

        authorization.validateAccess(userId, task.getTodoListId());

        return task;
    }
}
