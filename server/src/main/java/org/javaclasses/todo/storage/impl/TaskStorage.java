package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;

import java.util.List;

/**
 * Storage of `Task` entities by their `TaskId`.
 */
public class TaskStorage extends InMemoryStorage<TaskId, Task> {

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
