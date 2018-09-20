package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.AuthorizationFailedException;
import org.javaclasses.todo.storage.impl.TaskStorage;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides API which simplifies task removing.
 *
 * <p>To remove task ID of the task to remove should be given.
 */
@SuppressWarnings("WeakerAccess") // part of public API and its methods should be public.
public final class RemoveTask {
    private final TaskId taskId;
    private final TaskStorage taskStorage;

    /**
     * Creates {@code RemoveTask} instance.
     *
     * @param taskId      ID of the task to remove
     * @param taskStorage storage to remove task from
     */
    RemoveTask(TaskId taskId, TaskStorage taskStorage) {
        this.taskStorage = taskStorage;
        checkNotNull(taskId);

        this.taskId = taskId;
    }

    /**
     * Removes tas with given ID from storage.
     */
    public void execute() throws AuthorizationFailedException {
        taskStorage.remove(this.taskId);
    }
}
