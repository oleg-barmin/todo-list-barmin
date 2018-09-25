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

    UserId getOwner() {
        return owner;
    }

    /**
     * Allows to call chain of methods to create {@code Task} instance.
     *
     * <p>Every task must have:
     * - ID
     * - ID of user who created TodoList
     *
     * <p>After necessary values was set, {@link TodoListBuilder#build()} method should be called.
     *
     * <p>Implementation of <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>.
     */
    public static class TodoListBuilder {

        private TodoListId todoListId;
        private UserId owner;

        /**
         * Sets ID of owner of {@code TodoList} to build.
         *
         * @param ownerId ID of user who owns {@code TodoList}
         * @return this builder
         */
        public TodoListBuilder setOwner(UserId ownerId) {
            checkNotNull(ownerId);

            this.owner = ownerId;

            return this;
        }

        /**
         * Sets ID of {@code TodoList} to build.
         *
         * @param todoListId ID of {@code TodoList} to build
         * @return this builder
         */
        public TodoListBuilder setTodoListId(TodoListId todoListId) {
            checkNotNull(todoListId);

            this.todoListId = todoListId;

            return this;
        }

        /**
         * Creates instance of {@code TodoList}.
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
