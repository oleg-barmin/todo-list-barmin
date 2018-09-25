package org.javaclasses.todo.model;

import org.javaclasses.todo.storage.impl.TaskStorage;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * API which simplifies task removing.
 *
 * <p>To remove task, ID of the task to remove should be given.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of public API and its methods should be public.
public final class RemoveTask {

    private final TaskId taskId;
    private final TaskStorage taskStorage;
    private final Authorization authorization;
    private final UserId userId;

    /**
     * Creates {@code RemoveTask} instance.
     *
     * @param taskId      ID of the task to remove
     * @param taskStorage storage to remove task from
     * @param authorization  to validate task removal
     * @param userId      ID of user who initialized remove operation
     */
    RemoveTask(TaskId taskId, TaskStorage taskStorage, Authorization authorization, UserId userId) {
        this.taskId = checkNotNull(taskId);
        this.taskStorage = checkNotNull(taskStorage);
        this.authorization = checkNotNull(authorization);
        this.userId = checkNotNull(userId);
    }


    /**
     * Removes task with given ID from storage.
     *
     * @throws TaskNotFoundException     if task with given ID was not found
     * @throws TodoListNotFoundException if try to remove task from list which doesn't exist
     * @throws AccessDeniedException     if user try to modify list which he doesn't own
     */
    public void execute() throws TaskNotFoundException {
        authorization.validateAccess(userId, taskId);
        taskStorage.remove(taskId);
    }
}
