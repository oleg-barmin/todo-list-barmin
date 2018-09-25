package org.javaclasses.todo.model;

import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An entity which represents task to do.
 *
 * @author Oleg Barmin
 */
/* Task is part of public API so all methods should be public.*/
@SuppressWarnings("WeakerAccess")
public final class Task extends Entity<TaskId> {

    private final TodoListId todoListId;

    private final String description;
    private final boolean completed;
    private final Date creationDate;
    private final Date lastUpdateDate;

    private Task(TaskBuilder taskBuilder) {
        super(taskBuilder.taskId);
        this.todoListId = taskBuilder.todoListId;
        this.description = taskBuilder.description;
        this.completed = taskBuilder.completed;
        this.creationDate = taskBuilder.creationDate;
        this.lastUpdateDate = taskBuilder.lastUpdateDate;
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
        return (Date) creationDate.clone();
    }

    public Date getLastUpdateDate() {
        return (Date) lastUpdateDate.clone();
    }

    /**
     * Allows to build {@code Task} instance.
     *
     * <p>Every task <b>must</b> have:
     * - ID
     * - ID of TodoList to which it relate
     * - description of task
     * - creation date
     *
     * <p>Optional values:
     * - status (uncompleted by default)
     * - last update date (equals to creation date by default)
     *
     * <p>After necessary fields was set, {@link TaskBuilder#build()} method should be called.
     *
     * <p>Implementation of <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>.
     */
    /* Builder methods should be public */
    @SuppressWarnings("WeakerAccess")
    public static class TaskBuilder {

        private TaskId taskId;
        private TodoListId todoListId;

        private String description;
        private boolean completed;
        private Date creationDate;
        private Date lastUpdateDate;

        /**
         * Sets ID of task to build.
         *
         * @param taskId ID of task to build
         * @return this builder
         */
        public TaskBuilder setTaskId(TaskId taskId) {
            checkNotNull(taskId);
            this.taskId = taskId;
            return this;
        }

        /**
         * Sets ID of to-do list to which belongs task to build.
         *
         * @param todoListId ID of to-do list
         * @return this builder
         */
        public TaskBuilder setTodoListId(TodoListId todoListId) {
            checkNotNull(todoListId);

            this.todoListId = todoListId;
            return this;
        }

        /**
         * Sets description of task to build.
         *
         * @param description description of task to build
         * @return this builder
         */
        public TaskBuilder setDescription(String description) {
            checkNotNull(description);
            checkArgument(!description.isEmpty());

            this.description = description;
            return this;
        }

        /**
         * Sets status of task as completed.
         *
         * @return this builder
         */
        public TaskBuilder completed() {
            this.completed = true;
            return this;
        }

        /**
         * Sets creationDate of task to build.
         *
         * @param creationDate creation date of task to build
         * @return this build
         */
        public TaskBuilder setCreationDate(Date creationDate) {
            checkNotNull(creationDate);

            this.creationDate = (Date) creationDate.clone();
            return this;
        }

        /**
         * Sets lastUpdateDate of task to build.
         *
         * @param lastUpdateDate last update date of task to build
         * @return this builder
         */
        public TaskBuilder setLastUpdateDate(Date lastUpdateDate) {
            checkNotNull(lastUpdateDate);

            this.lastUpdateDate = (Date) lastUpdateDate.clone();
            return this;
        }

        /**
         * Creates {@code Task} instance with previously set values.
         *
         * @return task
         */
        public Task build() {
            checkNotNull(taskId);
            checkNotNull(todoListId);
            checkNotNull(description);
            checkNotNull(creationDate);

            if (lastUpdateDate == null) {
                lastUpdateDate = creationDate;
            }

            return new Task(this);
        }
    }
}
