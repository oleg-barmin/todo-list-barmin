package org.javaclasses.todo.model;

/**
 * ID of {@link TodoList} which ensures uniqueness of {@code TodoList}.
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

    @Override
    public String toString() {
        return "TodoListId{} " + super.toString();
    }
}
