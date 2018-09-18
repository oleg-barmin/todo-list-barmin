package org.javaclasses.todo.storage.impl;

import com.google.common.annotations.VisibleForTesting;
import org.javaclasses.todo.model.*;

import java.util.List;
import java.util.Map;

/**
 * Storage of `Task` entities by their `TaskId`.
 */
public class TaskStorage extends InMemoryStorage<TaskId, Task> {
    public TaskStorage() {
    }

    @VisibleForTesting
    TaskStorage(Map<TaskId, Task> map) {
        super(map);
    }


    /**
     * Provides list of tasks of `TodoList` with given ID.
     *
     * @param todoListId ID of `TodoList` which `Task`s required
     * @return list of `Task`s of `TodoList`
     */
    public List<Task> getAllTaskOfTodoList(TodoListId todoListId) {
        return findEntitiesWithField("todoListId", todoListId);
    }
}
