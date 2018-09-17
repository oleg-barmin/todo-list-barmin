package org.javaclasses.todo.model;

import java.util.Date;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An entity which represents task to do.
 * <p>
 * It stores task ID, its description, status (is it completed or not),
 * date of creation and date of last update (if task wasn't updated
 * the date of creation equals to last update date).
 */
public class Task extends Entity<TaskId> {
    private final TodoListId todoListId;
    private final String description;
    private final boolean completed;
    private final Date creationDate;
    private final Date lastUpdateDate;

    private Task(TaskId taskId,
                 TodoListId todoListId,
                 String description,
                 boolean completed,
                 Date creationDate,
                 Date lastUpdateDate) {

        setId(taskId);
        this.todoListId = todoListId;
        this.description = description;
        this.completed = completed;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public TodoListId getTodoListId() {
        return todoListId;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return isCompleted() == task.isCompleted() &&
                Objects.equals(getTodoListId(), task.getTodoListId()) &&
                Objects.equals(getDescription(), task.getDescription()) &&
                Objects.equals(getCreationDate(), task.getCreationDate()) &&
                Objects.equals(getLastUpdateDate(), task.getLastUpdateDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTodoListId(), getDescription(), isCompleted(), getCreationDate(), getLastUpdateDate());
    }

    @Override
    public String toString() {
        return "Task{" +
                "todoListId=" + todoListId +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                ", creationDate=" + creationDate +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }

    /**
     * Allows to call chain of methods to create `Task` instance.
     * <p>
     * Every task <b>must</b> have:
     * - ID
     * - ID of TodoList to which it relate
     * - description of task
     * - creation date
     * <p>
     * Optional fields:
     * - status (uncompleted by default)
     * - last update date (equals to creation date by default)
     * <p>
     * After necessary fields was set, {@link TaskBuilder#build()} method should be called.
     * <p>
     * Implementation of <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>.
     */
    public static class TaskBuilder {
        private TaskId taskId;
        private TodoListId todoListId;

        private String description;
        private boolean completed;
        private Date creationDate;
        private Date lastUpdateDate;

        TaskBuilder setTaskId(TaskId taskId) {
            checkNotNull(taskId);
            this.taskId = taskId;
            return this;
        }

        TaskBuilder setTodoListId(TodoListId todoListId) {
            checkNotNull(todoListId);

            this.todoListId = todoListId;
            return this;
        }

        TaskBuilder setDescription(String description) {
            checkNotNull(description);
            checkArgument(!description.equals(""));

            this.description = description;
            return this;
        }

        TaskBuilder setStatus(boolean completed) {
            this.completed = completed;
            return this;
        }

        TaskBuilder setCreationDate(Date creationDate) {
            checkNotNull(creationDate);

            this.creationDate = creationDate;
            return this;
        }

        TaskBuilder setLastUpdateDate(Date lastUpdateDate) {
            checkNotNull(lastUpdateDate);

            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        Task build() {
            checkNotNull(taskId);
            checkNotNull(todoListId);
            checkNotNull(description);
            checkNotNull(creationDate);

            if (lastUpdateDate == null) {
                lastUpdateDate = creationDate;
            }

            return new Task(taskId, todoListId, description, completed, creationDate, lastUpdateDate);
        }
    }
}
