package org.javaclasses.todo.model;

/**
 * Unique ID of {@link TodoList}.
 *
 * @author Oleg Barmin
 */
public final class TodoListId extends EntityId<String> {

    /**
     * Creates {@code TodoListId} instance.
     *
     * @param value string which contains uuid to ensure uniqueness {@code TodoList}s
     */
    public TodoListId(String value) {
        super(value);
    }
}
