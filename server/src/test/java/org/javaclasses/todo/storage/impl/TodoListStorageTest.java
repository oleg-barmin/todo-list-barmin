package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.TodoList;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@DisplayName("TodoListStorage should")
class TodoListStorageTest extends InMemoryStorageTest<TodoListId, TodoList> {
    private Map<TodoListId, TodoList> map = new HashMap<>();
    private TodoListStorage storage = new TodoListStorage(map);

    @Override
    InMemoryStorage<TodoListId, TodoList> getStorage() {
        return storage;
    }

    @Override
    Map<TodoListId, TodoList> getMap() {
        return map;
    }

    @Override
    TodoListId createID() {
        return new TodoListId(UUID.randomUUID().toString());
    }

    @Override
    TodoList createEntity() {
        return new TodoList.TodoListBuilder()
                .setOwner(new UserId(UUID.randomUUID().toString()))
                .setTodoListId(createID())
                .build();
    }
}