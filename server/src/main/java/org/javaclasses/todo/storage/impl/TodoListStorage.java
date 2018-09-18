package org.javaclasses.todo.storage.impl;

import com.google.common.annotations.VisibleForTesting;
import org.javaclasses.todo.model.TodoList;
import org.javaclasses.todo.model.TodoListId;

import java.util.Map;

/**
 * Storage of `TodoList` entities by `TodoListId`.
 */
public class TodoListStorage extends InMemoryStorage<TodoListId, TodoList> {
    public TodoListStorage() {
    }

    @VisibleForTesting
    TodoListStorage(Map<TodoListId, TodoList> map) {
        super(map);
    }
}
