package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

@DisplayName("TaskStorage should")
class TaskStorageTest extends InMemoryStorageTest<TaskId, Task> {
    private final Map<TaskId, Task> map = new HashMap<>();
    private final TaskStorage storage = new TaskStorage(map);

    @Override
    TaskId createID() {
        return new TaskId(UUID.randomUUID().toString());
    }

    @Override
    InMemoryStorage<TaskId, Task> getStorage() {
        return storage;
    }

    @Override
    // getMap should return same object for test needs
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    Map<TaskId, Task> getMap() {
        return map;
    }

    @Override
    Task createEntityWithId(TaskId entityId) {
        return new Task.TaskBuilder()
                .setTaskId(entityId)
                .setTodoListId(new TodoListId(UUID.randomUUID().toString()))
                .setDescription("task")
                .setCreationDate(new Date())
                .build();
    }

    private Task createTaskWith(TodoListId todoListId) {
        return new Task.TaskBuilder()
                .setTaskId(createID())
                .setTodoListId(todoListId)
                .setDescription("task")
                .setCreationDate(new Date())
                .build();
    }


    @Test
    @DisplayName("should return all tasks with specified todoListId")
    void testGetAllTaskOfTodoList() {
        TodoListId firstId = new TodoListId(UUID.randomUUID().toString());
        TodoListId secondId = new TodoListId(UUID.randomUUID().toString());

        Collection<Task> writtenTaskFirstTodoList = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Task task = createTaskWith(firstId);
            storage.write(task);
            writtenTaskFirstTodoList.add(task);
        }

        storage.write(createTaskWith(secondId));
        storage.write(createTaskWith(secondId));

        List<Task> allTaskOfTodoList = storage.getAllTaskOfTodoList(firstId);

        Assertions.assertEquals(writtenTaskFirstTodoList.size(), allTaskOfTodoList.size(),
                "Size of received tasks list should be equals " +
                        "to number of written tasks with same todolistId, but it don't.");
        Assertions.assertTrue(allTaskOfTodoList.containsAll(writtenTaskFirstTodoList));
    }

    @Override
    void testWriteEntityWithNullId() {
        Assertions.assertThrows(NullPointerException.class, () -> {
                    Task entity = createEntityWithNullId();
                    storage.write(entity);
                },
                "should throw NullPointerException if try to write task with null ID, but it don't.");
    }

    @Test
    @DisplayName("should return empty list if try to get all task of not existing todoList")
    void testGetAllTaskOfNonExistingTodoList() {
        TodoListId firstId = new TodoListId(UUID.randomUUID().toString());
        TodoListId secondId = new TodoListId(UUID.randomUUID().toString());

        storage.write(createTaskWith(secondId));
        storage.write(createTaskWith(secondId));

        List<Task> allTaskOfTodoList = storage.getAllTaskOfTodoList(firstId);

        Assertions.assertEquals(0, allTaskOfTodoList.size(),
                "Size of received tasks list should be zero if TodoList doesn't exists, but it don't.");
    }
}