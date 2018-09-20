package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.AuthorizationFailedException;
import org.javaclasses.todo.storage.impl.TaskStorage;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides API which simplifies task adding.
 *
 * <p>To add task these values should be provided:
 * - ID of the task
 * - ID of {@code TodoList} to which it belongs
 * - description of the task to add
 */
/*
 * WeakerAccess - part of public API and its methods should be public.
 */
@SuppressWarnings("WeakerAccess")
public final class AddTask {
    private final TaskStorage taskStorage;
    private Task.TaskBuilder taskBuilder;

    /**
     * Creates {@code AddTask} instance.
     *
     * @param taskId      ID of the task to add
     * @param taskStorage storage to store new task
     */
    AddTask(TaskId taskId, TaskStorage taskStorage) {
        this.taskStorage = taskStorage;
        checkNotNull(taskId);

        taskBuilder = new Task.TaskBuilder()
                .setTaskId(taskId)
                .setCreationDate(new Date());
    }

    /**
     * Sets {@code TodoList} to which belongs task to add.
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
     * @param description description of task to add
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
     */
    public void execute() throws AuthorizationFailedException {
        Task build = taskBuilder.build();
        taskStorage.write(build);
    }
}
