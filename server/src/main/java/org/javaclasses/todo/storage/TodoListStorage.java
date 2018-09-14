package org.javaclasses.todo.storage;

import com.google.common.base.Preconditions;
import org.javaclasses.todo.model.TodoList;
import org.javaclasses.todo.model.TodoListId;

import java.util.Optional;

/**
 * Storage of `TodoList` entities by `TodoListId`.
 */
public class TodoListStorage extends InMemoryStorage<TodoListId, TodoList> {

    @Override
    public TodoList write(TodoList entity) {
        Optional<TodoList> todoListById = findById(entity.getId());

        if (todoListById.isPresent()) {
            update(todoListById.get());
            return entity;
        }

        return create(entity);
    }

    @Override
    public Optional<TodoList> read(TodoListId id) {
        Preconditions.checkNotNull(id, "ID of TodoList cannot be null");

        return findById(id);
    }
}
