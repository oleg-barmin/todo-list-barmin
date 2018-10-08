package org.javaclasses.todo.storage.impl;

import com.google.common.annotations.VisibleForTesting;
import org.javaclasses.todo.model.entity.TodoList;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.model.entity.UserId;

import java.util.List;
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

    /**
     * Reads all {@link TodoList}s of user with given ID.
     *
     * <p>If returned list is empty then user with given ID has no {@code TodoList}s.
     *
     * @param userId ID of user whose to-do list is needed
     * @return list of {@link TodoList}s of user with given ID
     */
    public List<TodoList> readTodoListsOf(UserId userId) {
        return findByField("owner", userId);
    }
}
