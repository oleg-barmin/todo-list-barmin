package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.AuthorizationFailedException;
import org.javaclasses.todo.storage.impl.TaskStorage;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides API which simplifies retrieving of all task which belongs to specified {@code TodoList} by its ID.
 */
@SuppressWarnings("WeakerAccess") // part of public API and its methods should be public.
public class TodoListTasks {
    private final TodoListId todoListId;
    private final TaskStorage taskStorage;

    /**
     * Creates {@code TodoListTasks} instance.
     *
     * @param todoListId  ID of the {@code TodoList} which task should be found
     * @param taskStorage storage to get desired tasks from
     */
    TodoListTasks(TodoListId todoListId, TaskStorage taskStorage) {
        this.taskStorage = taskStorage;
        checkNotNull(todoListId);

        this.todoListId = todoListId;
    }

    /**
     * Provides list of tasks which belongs to {@code TodoList} with given ID.
     *
     * @return list of task which belongs to specified {@code TodoList}
     */
    public List<Task> execute() throws AuthorizationFailedException {

        return taskStorage.getAllTaskOfTodoList(todoListId);
    }
}
