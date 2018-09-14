package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.impl.TodoList;
import org.javaclasses.todo.model.impl.TodoListId;

/**
 * Storage of `TodoList` entities by `TodoListId`.
 */
public class TodoListStorage extends InMemoryStorage<TodoListId, TodoList> {
}
