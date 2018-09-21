package org.javaclasses.todo.model;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An entity which represents a list of tasks to do.
 *
 * @author Oleg Barmin
 */
public final class TodoList extends Entity<TodoListId> {
    private final UserId owner;

    private TodoList(TodoListBuilder todoListBuilder) {
        super(todoListBuilder.todoListId);
        this.owner = todoListBuilder.owner;
    }

    public UserId getOwner() {
        return owner;
    }

    /**
     * Allows to call chain of methods to create `Task` instance.
     *
     * <p>Every task must have:
     * - ID
     * - ID of user who created TodoList
     *
     * <p>After necessary fields was set, {@link TodoListBuilder#build()} method should be called.
     *
     * <p>Implementation of <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>.
     */
    public static class TodoListBuilder {
        private TodoListId todoListId;
        private UserId owner;

        public TodoListBuilder setOwner(UserId owner) {
            checkNotNull(owner);

            this.owner = owner;

            return this;
        }

        public TodoListBuilder setTodoListId(TodoListId todoListId) {
            checkNotNull(todoListId);

            this.todoListId = todoListId;

            return this;
        }

        /**
         * Creates instance of `TodoList`.
         *
         * @return TodoList with fields that were previously set
         * @throws NullPointerException if ID of owner or ID of TodoList was not provided.
         */
        public TodoList build() {
            checkNotNull(this.owner);
            checkNotNull(this.todoListId);

            return new TodoList(this);
        }

    }
}
