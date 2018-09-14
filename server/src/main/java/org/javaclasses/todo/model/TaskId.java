package org.javaclasses.todo.model;

/**
 * ID of {@link Task} which unifies `Task`s.
 *
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
}
