package org.javaclasses.todo.model;

import org.javaclasses.todo.storage.impl.TaskStorage;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Finds task by ID.
 */
public class FindTask {
    private final TaskId taskId;
    private final TaskStorage taskStorage;
    private final Authorization authorization;
    private final UserId userId;

    /**
     * Creates {@code FindTask} instance.
     *
     * @param taskId        ID of the task to find
     * @param taskStorage   storage to find task in
     * @param authorization to validate find task request
     * @param userId        user who requested task
     */
    FindTask(TaskId taskId, TaskStorage taskStorage, Authorization authorization, UserId userId) {
        this.taskId = checkNotNull(taskId);
        this.taskStorage = checkNotNull(taskStorage);
        this.authorization = checkNotNull(authorization);
        this.userId = checkNotNull(userId);
    }

    /**
     * Finds task with given ID.
     *
     * @return task with given ID
     */
    public Task execute() {
        authorization.validateAccess(userId, taskId);

        Optional<Task> optionalTask = taskStorage.read(taskId);

        if (!optionalTask.isPresent()) {
            throw new TaskNotFoundException(taskId);
        }

        return optionalTask.get();
    }
}
