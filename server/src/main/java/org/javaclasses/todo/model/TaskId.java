package org.javaclasses.todo.model;

/**
 * ID of {@link Task} which ensures uniqueness of {@code Task}s.
 */
public final class TaskId extends EntityId<String> {

    /**
     * Creates {@code TaskId} instance.
     *
     * @param value value of ID
     */
    public TaskId(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return "TaskId{} " + super.toString();
    }
}
