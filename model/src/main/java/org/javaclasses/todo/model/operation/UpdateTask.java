package org.javaclasses.todo.model.operation;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.Authorization;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.EmptyTaskDescriptionException;
import org.javaclasses.todo.model.TaskNotFoundException;
import org.javaclasses.todo.model.TodoListNotFoundException;
import org.javaclasses.todo.model.UpdateCompletedTaskException;
import org.javaclasses.todo.model.entity.Task;
import org.javaclasses.todo.model.entity.TaskId;
import org.javaclasses.todo.model.entity.UserId;
import org.javaclasses.todo.storage.impl.TaskStorage;

import java.util.Date;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.javaclasses.todo.model.entity.Descriptions.validate;

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
    private final Authorization authorization;
    private final TaskStorage taskStorage;
    private Task.TaskBuilder taskBuilder;

    /**
     * Creates {@code UpdateTask} instance.
     *
     * @param taskId         ID of the {@code Task} to update
     * @param taskStorage    storage to store {@code Task} changes
     * @param authorization  to validate access to {@code Task}
     * @param authentication to authenticate token
     */
    public UpdateTask(TaskId taskId, TaskStorage taskStorage, Authorization authorization,
                      Authentication authentication) {
        super(authentication);

        this.taskStorage = checkNotNull(taskStorage);
        this.taskId = checkNotNull(taskId);
        this.authorization = checkNotNull(authorization);
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
        validate(description);
        taskBuilder = taskBuilder.setDescription(description);
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
     * @throws TaskNotFoundException        if task to update was not found
     * @throws TodoListNotFoundException    if to-do list of task was not found
     * @throws UpdateCompletedTaskException if task to update is completed
     * @throws AuthorizationFailedException if user has no authority to update task with given ID.
     */
    public void execute() throws AuthorizationFailedException, TaskNotFoundException {
        UserId userId = validateToken();

        Optional<Task> optionalTask = taskStorage.read(taskId);

        if (!optionalTask.isPresent()) {
            throw new TaskNotFoundException(taskId);
        }

        Task taskToUpdate = optionalTask.get();

        authorization.validateAccess(userId, taskToUpdate.getTodoListId());

        if (taskToUpdate.isCompleted()) {
            throw new UpdateCompletedTaskException(taskToUpdate.getId());
        }

        Task build = taskBuilder.setTaskId(taskId)
                                .setTodoListId(taskToUpdate.getTodoListId())
                                .setCreationDate(taskToUpdate.getCreationDate())
                                .setLastUpdateDate(new Date())
                                .build();

        taskStorage.write(build);
    }
}
