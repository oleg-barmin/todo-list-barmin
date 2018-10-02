package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.entity.Task;
import org.javaclasses.todo.model.entity.TaskId;
import org.javaclasses.todo.model.entity.TodoList;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.model.operation.AddTask;
import org.javaclasses.todo.model.operation.CreateList;
import org.javaclasses.todo.model.operation.FindTask;
import org.javaclasses.todo.model.operation.ReadTasks;
import org.javaclasses.todo.model.operation.RemoveTask;
import org.javaclasses.todo.model.operation.UpdateTask;
import org.javaclasses.todo.storage.impl.TaskStorage;
import org.javaclasses.todo.storage.impl.TodoListStorage;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Service which manages operations with to-do list and tasks in the to-do lists.
 *
 * <p>Allows users to:
 * - Create new to-do list;
 * - Add tasks to created to-do list;
 * - Update already created tasks;
 * - Delete created tasks.
 *
 * @author Oleg Barmin
 */
public class TodoService {

    private final Authentication authentication;
    private final TodoListStorage todoListStorage;
    private final TaskStorage taskStorage;
    private final Authorization authorization;

    public TodoService(Authentication authentication, TodoListStorage todoListStorage,
                       TaskStorage taskStorage) {
        this.authentication = checkNotNull(authentication);
        this.todoListStorage = checkNotNull(todoListStorage);
        this.taskStorage = checkNotNull(taskStorage);
        this.authorization = new Authorization(todoListStorage);
    }

    /**
     * Creates instance of {@link CreateList} to create new list.
     *
     * @param todoListId ID of to-do list to create
     * @return {@code CreateList} instance to build {@code TodoList} to create and upload it
     * @throws TodoListAlreadyExistsException if {@code TodoList} with given ID already exists
     */
    public CreateList createList(TodoListId todoListId) throws TodoListAlreadyExistsException {
        checkNotNull(todoListId);

        Optional<TodoList> todoList = todoListStorage.read(todoListId);
        if (todoList.isPresent()) {
            throw new TodoListAlreadyExistsException(todoListId);
        }

        return new CreateList(todoListId, todoListStorage, authentication);
    }

    /**
     * Creates instance of {@link ReadTasks} to read all task of specified to-do list.
     *
     * @param todoListId ID of {@code TodoList} which tasks required
     * @return {@code ReadTasks} instance to build request to retrieve list of Tasks of specified {@code TodoList}
     */
    public ReadTasks readTasksFrom(TodoListId todoListId) {
        return new ReadTasks(todoListId, taskStorage, authorization, authentication);
    }

    /**
     * Creates instance of {@link FindTask} to find task by ID.
     *
     * @param taskId ID of the task to find
     * @return {@code FindTask} instance
     */
    public FindTask findTask(TaskId taskId) {
        return new FindTask(taskId, taskStorage, authorization, authentication);
    }

    /**
     * Creates instance of {@link AddTask} to add new task.
     *
     * @param taskId ID of the task to add
     * @return {@code AddTask} instance to build request to add new Task
     * @throws TaskAlreadyExistsException if {@code Task} with given Id already exists
     */
    public AddTask addTask(TaskId taskId) throws TaskAlreadyExistsException {
        checkNotNull(taskId);

        Optional<Task> task = taskStorage.read(taskId);

        if (task.isPresent()) {
            throw new TaskAlreadyExistsException(taskId);
        }

        return new AddTask(taskId, taskStorage, authentication, authorization);
    }

    /**
     * Creates instance of {@link UpdateTask} to update task.
     *
     * @param taskId ID of the task to update
     * @return {@code UpdateTask} instance to build task to update an upload changes
     */
    public UpdateTask updateTask(TaskId taskId) {
        return new UpdateTask(taskId, taskStorage, authorization, authentication);
    }

    /**
     * Creates instance of {@link RemoveTask} to remove task.
     *
     * @param taskId ID of the task to remove
     * @return {@code RemoveTask} instance to build task to remove it
     */
    public RemoveTask removeTask(TaskId taskId) throws TaskNotFoundException {
        checkNotNull(taskId);

        return new RemoveTask(taskId, taskStorage, authorization, authentication);
    }

}
