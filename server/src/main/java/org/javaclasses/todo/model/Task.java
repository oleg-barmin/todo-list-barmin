package org.javaclasses.todo.model;

import java.util.Date;
import java.util.Objects;

import static com.google.common.base.Preconditions.*;

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

    private Task(TaskId taskId, TodoListId todoListId, String description, boolean completed, Date creationDate, Date lastUpdateDate) {
        setId(taskId);
        this.todoListId = todoListId;
        this.description = description;
        this.completed = completed;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    private TodoListId getTodoListId() {
        return todoListId;
    }

    private String getDescription() {
        return description;
    }

    private boolean isCompleted() {
        return completed;
    }

    private Date getCreationDate() {
        return creationDate;
    }

    private Date getLastUpdateDate() {
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

    public static class TaskBuilder {
        private TaskId taskId;
        private TodoListId todoListId;

        private String description;
        private boolean completed;
        private Date creationDate;
        private Date lastUpdateDate;

        public TaskBuilder setTaskId(TaskId taskId) {
            checkNotNull(taskId);

            this.taskId = taskId;
            return this;
        }

        public TaskBuilder setTodoListId(TodoListId todoListId) {
            checkNotNull(todoListId);

            this.todoListId = todoListId;
            return this;
        }

        public TaskBuilder setDescription(String description) {
            checkNotNull(description);
            checkArgument(!description.equals(""));

            this.description = description;
            return this;
        }

        public TaskBuilder setCompleted(boolean completed) {
            this.completed = completed;
            return this;
        }

        public TaskBuilder setCreationDate(Date creationDate) {
            checkNotNull(creationDate);

            this.creationDate = creationDate;
            return this;
        }

        public TaskBuilder setLastUpdateDate(Date lastUpdateDate) {
            checkNotNull(lastUpdateDate);

            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public Task build() {
            checkNotNull(taskId);
            checkNotNull(todoListId);
            checkNotNull(description);
            checkNotNull(creationDate);
            checkNotNull(lastUpdateDate);

            return new Task(taskId, todoListId, description, completed, creationDate, lastUpdateDate);
        }
    }
}
