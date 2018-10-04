package org.javaclasses.todo.web.given;

import org.javaclasses.todo.model.entity.TaskId;
import org.javaclasses.todo.model.entity.TodoListId;

import java.util.Objects;

/**
 * Represents {@code Task} data which can be received from client side of to-do list application.
 *
 * @author Oleg Barmin
 */
public class SampleTask {

    private final TaskId taskId;
    private final TodoListId todoListId;
    private final String description;

    /**
     * Creates {@code SampleTask} instance.
     *
     * @param taskId      ID of task
     * @param todoListId  ID of {@code TodoList} to which task belongs
     * @param description description of task
     */
    public SampleTask(TaskId taskId, TodoListId todoListId, String description) {
        this.taskId = taskId;
        this.todoListId = todoListId;
        this.description = description;
    }

    public TaskId getTaskId() {
        return taskId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, todoListId, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SampleTask)) {
            return false;
        }
        SampleTask that = (SampleTask) o;
        return Objects.equals(taskId, that.taskId) &&
                Objects.equals(todoListId, that.todoListId) &&
                Objects.equals(description, that.description);
    }
}
