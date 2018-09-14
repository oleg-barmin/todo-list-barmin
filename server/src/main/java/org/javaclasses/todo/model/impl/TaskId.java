package org.javaclasses.todo.model.impl;

import java.util.Objects;

/**
 * ID of {@link Task} which unifies `Task`s.
 * <p>
 * Wraps a string which contains uuid ID of `Task`.
 */
public class TaskId {
    private String id;

    /**
     * Creates `TaskId` instance.
     *
     * @param id string with uuid.
     */
    public TaskId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskId)) return false;
        TaskId taskId = (TaskId) o;
        return Objects.equals(getId(), taskId.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
