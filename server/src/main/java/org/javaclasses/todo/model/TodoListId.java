package org.javaclasses.todo.model;

/**
 * ID of {@link TodoList} which unifies `TodoList`.
 * <p>
 * Wraps a string which contains uuid ID of `TodoList`.
 */
public class TodoListId {
    private final String id;

    /**
     * Creates `TodoListId` instance.
     *
     * @param id string which contains uuid to unify `TodoList`s
     */
    public TodoListId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
