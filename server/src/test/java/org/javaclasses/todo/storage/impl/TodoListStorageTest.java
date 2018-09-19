package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TodoList;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.Assertions;
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
    TodoList createEntityWithId(TodoListId entityId) {
        return new TodoList.TodoListBuilder()
                .setTodoListId(entityId)
                .setOwner(new UserId(UUID.randomUUID().toString()))
                .build();
    }
    @Override
    void testWriteEntityWithNullId() {
        Assertions.assertThrows(NullPointerException.class, () -> {
                    TodoList entity = createEntityWithNullId();
                    storage.write(entity);
                },
                "should throw NullPointerException if try to write todo list with null ID, but it don't.");
    }
    @Override
    // getMap should return same object for test needs
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    Map<TodoListId, TodoList> getMap() {
        return map;
    }

    @Override
    TodoListId createID() {
        return new TodoListId(UUID.randomUUID().toString());
    }
}