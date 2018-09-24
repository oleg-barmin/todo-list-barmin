package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.AuthorizationFailedException;
import org.javaclasses.todo.storage.impl.TaskStorage;

import java.util.Date;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * API which simplifies task updating.
 *
 * <p>To update task these values should be provided:
 * - ID of the task to update
 * - new description of the task.
 * - optionally, new task status (by default false)
 *
 * <p>Last update date sets automatically to current date.
 *
 * <p>Other task values remains the same as in stored task.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of public API and its methods should be public.
public final class UpdateTask {
    private final TaskId taskId;
    private final AccessAuth operationAuth;
    private final UserId userId;
    private final TaskStorage taskStorage;
    private Task.TaskBuilder taskBuilder;

    /**
     * Creates {@code UpdateTask} instance.
     *
     * @param taskId      ID of the task to update
     * @param taskStorage storage to store task changes
     * @param accessAuth  to validate task update
     * @param userId      ID of user who performs operation
     */
    UpdateTask(TaskId taskId, TaskStorage taskStorage, AccessAuth accessAuth, UserId userId) {
        this.taskStorage = checkNotNull(taskStorage);
        this.taskId = checkNotNull(taskId);
        this.operationAuth = checkNotNull(accessAuth);
        this.userId = checkNotNull(userId);
        taskBuilder = new Task.TaskBuilder();
    }

    /**
     * Sets new description of task.
     *
     * @param description new description of task
     * @return this {@code UpdateTask} instance to continue request building
     * @throws EmptyTaskDescriptionException if given task description is null or empty
     */
    public UpdateTask withDescription(String description) throws EmptyTaskDescriptionException {
        Descriptions.validate(description);
        taskBuilder = taskBuilder.setDescription(description.trim());
        return this;
    }

    /**
     * Sets new status of task (is it completed or not).
     *
     * @return this {@code UpdateTask} instance to continue request building
     */
    public UpdateTask completed() {
        taskBuilder = taskBuilder.completed();
        return this;
    }

    /**
     * Uploads previously modified task to storage.
     *
     * @throws TaskNotFoundException if task to update was not found.
     */
    public void execute()
            throws AuthorizationFailedException, TaskNotFoundException {

        operationAuth.validateAssess(userId, taskId);

        Optional<Task> optionalTask = taskStorage.read(taskId);

        //task existing is already checked by AccessAuth.
        @SuppressWarnings("OptionalGetWithoutIsPresent") Task taskToUpdate = optionalTask.get();

        Task build = taskBuilder.setTaskId(taskId)
                .setTodoListId(taskToUpdate.getTodoListId())
                .setCreationDate(taskToUpdate.getCreationDate())
                .setLastUpdateDate(new Date())
                .build();

        taskStorage.write(build);
    }
}
