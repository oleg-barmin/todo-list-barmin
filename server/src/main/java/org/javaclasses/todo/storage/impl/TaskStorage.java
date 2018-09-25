package org.javaclasses.todo.storage.impl;

import com.google.common.annotations.VisibleForTesting;
import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;

import java.util.List;
import java.util.Map;

/**
 * Storage of {@code Task} entities by their {@code TaskId}.
 *
 * @author Oleg Barmin
 */
public class TaskStorage extends InMemoryStorage<TaskId, Task> {

    public TaskStorage() {
    }

    @VisibleForTesting
    TaskStorage(Map<TaskId, Task> map) {
        super(map);
    }

    /**
     * Provides list of tasks of {@code TodoList} with given ID.
     *
     * @param todoListId ID of {@code TodoList} which {@code Task}s required
     * @return list of {@code Task}s of {@code TodoList}
     */
    public List<Task> getAllTaskOfTodoList(TodoListId todoListId) {
        return findByField("todoListId", todoListId);
    }
}
