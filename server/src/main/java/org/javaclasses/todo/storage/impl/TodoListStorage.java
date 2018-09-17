package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.TodoList;
import org.javaclasses.todo.model.TodoListId;

/**
 * Storage of `TodoList` entities by `TodoListId`.
 */
public class TodoListStorage extends InMemoryStorage<TodoListId, TodoList> {
}
