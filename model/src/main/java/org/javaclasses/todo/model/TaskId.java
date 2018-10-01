package org.javaclasses.todo.model;

/**
 * Unique ID of {@link Task}.
 *
 * @author Oleg Barmin
 * @implNote value of identifier should be a string with UUID.
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
}
