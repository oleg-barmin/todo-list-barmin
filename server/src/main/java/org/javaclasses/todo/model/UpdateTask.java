package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.storage.impl.TaskStorage;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * API which simplifies task updating.
 *
 * <p>To update task ID of the task to update should be provided.
 *
 * <p>Fields to update:
 * - new description of the task (by default it will not change).
 * - new task status (by default false).
 *
 * <p>If non of them will be provided update won't be executed.
 *
 *
 * <p>Last update date sets automatically to current date.
 *
 * <p>Other task values remains the same as in stored task.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of public API and its methods should be public.
public final class UpdateTask extends Operation<UpdateTask> {

    private final TaskId taskId;
    private final Authorization operationAuth;
    private final TaskStorage taskStorage;
    private Task.TaskBuilder taskBuilder;

    private String taskDescription = null;

    /**
     * Creates {@code UpdateTask} instance.
     *
     * @param taskId         ID of the task to update
     * @param taskStorage    storage to store task changes
     * @param authorization  to validate task update
     * @param authentication to authenticate token
     */
    UpdateTask(TaskId taskId, TaskStorage taskStorage, Authorization authorization, Authentication authentication) {
        super(authentication);

        this.taskStorage = checkNotNull(taskStorage);
        this.taskId = checkNotNull(taskId);
        this.operationAuth = checkNotNull(authorization);
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
        taskDescription = description.trim();
        return this;
    }

    /**
     * Sets new status of task (is it completed or not).
     *
     * @return this {@code UpdateTask} instance to continue request building
     */
    public UpdateTask setStatus(boolean taskStatus) {
        taskBuilder = taskBuilder.setStatus(taskStatus);
        return this;
    }

    /**
     * Uploads previously modified task to storage.
     *
     * @throws TaskNotFoundException if task to update was not found.
     * @throws UpdateCompletedTaskException if task to update is completed
     * @throws AuthorizationFailedException if user try to update foreign task
     */
    public void execute() throws AuthorizationFailedException, TaskNotFoundException {
        Task taskToUpdate = operationAuth.validateAccess(validateToken(), taskId);

        if (taskToUpdate.isCompleted()) {
            throw new UpdateCompletedTaskException(taskToUpdate.getId());
        }

        if (taskDescription == null) {
            taskDescription = taskToUpdate.getDescription();
        }

        Task build = taskBuilder.setTaskId(taskId)
                .setTodoListId(taskToUpdate.getTodoListId())
                .setDescription(taskDescription)
                .setCreationDate(taskToUpdate.getCreationDate())
                .setLastUpdateDate(new Date())
                .build();

        taskStorage.write(build);
    }
}
