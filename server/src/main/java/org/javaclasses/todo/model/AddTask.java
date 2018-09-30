package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.storage.impl.TaskStorage;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * API which simplifies task adding.
 *
 * <p>To add task these values should be provided:
 * - ID of the task
 * - ID of {@code TodoList} to which it belongs
 * - description of the task to add
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of public API and its methods should be public.
public final class AddTask extends Operation<AddTask> {

    private final TaskStorage taskStorage;
    private Task.TaskBuilder taskBuilder;
    private final Authorization authorization;

    /**
     * Creates {@code AddTask} instance.
     *
     * @param taskId         ID of the {@code Task} to add
     * @param taskStorage    storage to store new {@code Task}
     * @param authentication to authenticate user token
     * @param authorization  to validate task adding
     */
    AddTask(TaskId taskId, TaskStorage taskStorage, Authentication authentication,
            Authorization authorization) {
        super(authentication);
        this.taskStorage = checkNotNull(taskStorage);
        this.authorization = checkNotNull(authorization);

        taskBuilder = new Task.TaskBuilder()
                .setTaskId(taskId)
                .setCreationDate(new Date());
    }

    /**
     * Sets {@code TodoList} to which belongs {@code Task} to add.
     *
     * @param todoListId ID of {@code TodoList} to which belongs this task
     * @return this {@code AddTask} instance to continue request building
     */
    public AddTask withTodoListId(TodoListId todoListId) {
        taskBuilder = taskBuilder.setTodoListId(todoListId);
        return this;
    }

    /**
     * Sets description of task to add.
     *
     * @param description description of {@code Task} to add
     * @return this {@code AddTask} instance to continue request building
     * @throws EmptyTaskDescriptionException if given task description is null or empty
     */
    public AddTask withDescription(String description) throws EmptyTaskDescriptionException {
        Descriptions.validate(description);
        taskBuilder = taskBuilder.setDescription(description.trim());
        return this;
    }

    /**
     * Uploads task with previously set values.
     *
     * @throws AuthorizationFailedException if try to add task to {@link TodoList} of other user
     * @throws TodoListNotFoundException    if try to add task to non-existing {@code TodoList}
     */
    public void execute() throws AuthorizationFailedException {
        Task task = taskBuilder.build();

        UserId userId = validateToken();
        authorization.validateAccess(userId, task.getTodoListId());

        taskStorage.write(task);
    }
}
