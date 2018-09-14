package org.javaclasses.todo.model.impl;

import java.util.Objects;

/**
 * An entity which represents a list of tasks to-do.
 * <p>
 * Contains:
 * - `TodoListId` which unifies `TodoList`
 * - `UserId` ID of user who owns `TodoList`
 */
public class TodoList extends Entity<TodoListId> {
    private UserId owner;

    public UserId getOwner() {
        return owner;
    }

    public void setOwner(UserId owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoList)) return false;
        TodoList todoList = (TodoList) o;
        return Objects.equals(getId(), todoList.getId()) &&
                Objects.equals(getOwner(), todoList.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner());
    }
}
