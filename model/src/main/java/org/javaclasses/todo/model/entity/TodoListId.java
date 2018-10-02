package org.javaclasses.todo.model.entity;

/**
 * Unique ID of {@link TodoList}.
 *
 * @author Oleg Barmin
 * @implNote value of identifier should be a string with UUID.
 */
public final class TodoListId extends EntityId<String> {

    /**
     * Creates {@code TodoListId} instance.
     *
     * @param value value of ID
     */
    public TodoListId(String value) {
        super(value);
    }
}
