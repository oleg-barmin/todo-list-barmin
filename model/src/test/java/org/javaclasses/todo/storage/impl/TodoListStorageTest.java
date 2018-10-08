package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.entity.TodoList;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.model.entity.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.javaclasses.todo.model.entity.TodoList.TodoListBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing {@link TodoListStorage}.
 *
 * @author Oleg Barmin
 */
@DisplayName("TodoListStorage should")
class TodoListStorageTest extends InMemoryStorageTest<TodoListId, TodoList> {

    private final Map<TodoListId, TodoList> map = new HashMap<>();
    private final TodoListStorage storage = new TodoListStorage(map);

    @Override
    TodoListId createID() {
        return new TodoListId(UUID.randomUUID()
                                  .toString());
    }

    @Override
    // getMap should return same object for test needs
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    Map<TodoListId, TodoList> getMap() {
        return map;
    }

    @Override
    InMemoryStorage<TodoListId, TodoList> getStorage() {
        return storage;
    }

    @Override
    TodoList createEntityWithId(TodoListId entityId) {
        return new TodoListBuilder()
                .setTodoListId(entityId)
                .setOwner(new UserId(UUID.randomUUID()
                                         .toString()))
                .build();
    }

    @Override
    void testWriteEntityWithNullId() {
        assertThrows(NullPointerException.class, () -> {
                         TodoList entity = createEntityWithNullId();
                         storage.write(entity);
                     },
                     "throw NullPointerException if try to write todo list with null ID, but it don't.");
    }

    @Test
    void testFindUserTodoLists() {
        UserId owner = new UserId(UUID.randomUUID()
                                      .toString());

        TodoList firstTodoList = new TodoListBuilder()
                .setTodoListId(new TodoListId(UUID.randomUUID()
                                                  .toString()))
                .setOwner(owner)
                .build();
        TodoList secondTodoList = new TodoListBuilder()
                .setTodoListId(new TodoListId(UUID.randomUUID()
                                                  .toString()))
                .setOwner(owner)
                .build();

        storage.write(firstTodoList);
        storage.write(secondTodoList);

        Collection<TodoList> writtenTodoLists = new ArrayList<>();

        writtenTodoLists.add(firstTodoList);
        writtenTodoLists.add(secondTodoList);

        List<TodoList> userTodoLists = storage.readTodoListsOf(owner);

        assertEquals(2, userTodoLists.size(),
                     "find to-do list of user with given ID, but he don't.");
        assertTrue(writtenTodoLists.containsAll(userTodoLists),
                   "find all written to-do lists, but it don't.");

    }
}