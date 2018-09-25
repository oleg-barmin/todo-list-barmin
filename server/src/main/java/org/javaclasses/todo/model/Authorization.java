package org.javaclasses.todo.model;

import org.javaclasses.todo.storage.impl.TaskStorage;
import org.javaclasses.todo.storage.impl.TodoListStorage;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Validates if user has access to entity with given ID.
 *
 * @author Oleg Barmin
 */
class Authorization {

    private final TodoListStorage todoListStorage;
    private final TaskStorage taskStorage;

    /**
     * Creates {@code Authorization} instance.
     *
     * @param taskStorage     storage of tasks
     * @param todoListStorage storage of to-do lists
     */
    Authorization(TaskStorage taskStorage, TodoListStorage todoListStorage) {
        this.taskStorage = checkNotNull(taskStorage);
        this.todoListStorage = checkNotNull(todoListStorage);
    }

    /**
     * Validates if user with given ID has access to {@code TodoList} with given ID.
     *
     * @param userId     user which tries to access {@code TodoList}
     * @param todoListId ID of {@code TodoList}
     * @throws TodoListNotFoundException if {@code TodoList} with given ID was not found
     * @throws AuthorizationFailedException     if user with given ID has no access to {@code TodoList} with given ID
     */
    void validateAccess(UserId userId, TodoListId todoListId) {
        checkNotNull(userId);
        checkNotNull(todoListId);

        Optional<TodoList> optionalTodoList = todoListStorage.read(todoListId);

        if (!optionalTodoList.isPresent()) {
            throw new TodoListNotFoundException(todoListId);
        }

        UserId owner = optionalTodoList.get().getOwner();

        if (!owner.equals(userId)) {
            throw new AuthorizationFailedException(userId, todoListId);
        }
    }

    /**
     * Validates if user with given ID has access to {@code Task} with given ID.
     *
     * @param userId user which tries to access {@code Task}
     * @param taskId ID of {@code Task}
     * @throws TaskNotFoundException if {@code Task} with given ID was not found
     * @throws AuthorizationFailedException if user with given ID has no access to {@code Task} with given ID
     */
    void validateAccess(UserId userId, TaskId taskId) {
        checkNotNull(userId);
        checkNotNull(taskId);

        Optional<Task> optionalTask = taskStorage.read(taskId);

        if (!optionalTask.isPresent()) {
            throw new TaskNotFoundException(taskId);
        }

        TodoListId todoListId = optionalTask.get().getTodoListId();

        validateAccess(userId, todoListId);
    }
}
