package org.javaclasses.todo.storage.impl;

import com.google.common.annotations.VisibleForTesting;
import org.javaclasses.todo.model.entity.TodoList;
import org.javaclasses.todo.model.entity.TodoListId;

import java.util.Map;

/**
 * Storage of {@code TodoList} entity by {@code TodoListId}.
 *
 * @author Oleg Barmin
 */
public class TodoListStorage extends InMemoryStorage<TodoListId, TodoList> {

    public TodoListStorage() {
    }

    @VisibleForTesting
    TodoListStorage(Map<TodoListId, TodoList> map) {
        super(map);
    }
}
