package org.javaclasses.todo.model;

import java.util.Objects;

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
     *
     */
    public TodoListId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoListId)) return false;
        TodoListId that = (TodoListId) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
