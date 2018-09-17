package org.javaclasses.todo.model;

import com.google.common.base.Preconditions;

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

    private TodoList(TodoListId todoListId, UserId owner) {
        setId(todoListId);
        this.owner = owner;
    }

    public UserId getOwner() {
        return owner;
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

    @Override
    public String toString() {
        return "TodoList{" +
                "ID=" + getId() + ", "
                + "owner=" + owner +
                '}';
    }

    /**
     * Allows to call chain of methods to create `Task` instance.
     * <p>
     * Every task <b>must</b> have:
     * - ID
     * - ID of user who created TodoList
     * <p>
     * After necessary fields was set, {@link TodoListBuilder#build()} method should be called.
     * <p>
     * Implementation of <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>.
     */
    public static class TodoListBuilder {
        private TodoListId todoListId;
        private UserId owner;

        TodoListBuilder setOwner(UserId owner) throws NullPointerException {
            Preconditions.checkNotNull(owner);

            this.owner = owner;

            return this;
        }

        TodoListBuilder setTodoListId(TodoListId todoListId) throws NullPointerException {
            Preconditions.checkNotNull(todoListId);

            this.todoListId = todoListId;

            return this;
        }

        /**
         * Creates instance of `TodoList`.
         *
         * @return TodoList with fields that were previously set
         * @throws NullPointerException if ID of owner or ID of TodoList was not provided.
         */
        TodoList build() {
            Preconditions.checkNotNull(this.owner);
            Preconditions.checkNotNull(this.todoListId);
            return new TodoList(todoListId, owner);
        }

    }
}
