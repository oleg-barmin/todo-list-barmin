package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.auth.SessionDoesNotExistsException;
import org.javaclasses.todo.storage.impl.TaskStorage;
import org.javaclasses.todo.storage.impl.TodoListStorage;

import java.util.Date;
import java.util.Optional;

/**
 * Service which manages operations with to-do list and tasks in the to-do lists.
 * <p>
 * Allows to:
 * - users to create new todoLists.
 * - add tasks to created todoLists
 * - update already created tasks.
 * - deleted created tasks
 */
public class TodoService {
    private Authentication authentication = new Authentication();
    private TodoListStorage todoListStorage = new TodoListStorage();
    private TaskStorage taskStorage = new TaskStorage();

    /**
     * Creates new to-do list.
     *
     * @param token      `Token` of users who creates to-do list
     * @param todoListId ID of to-do list to create
     * @throws SessionDoesNotExistsException if session with given token doesn't exist
     */
    public void createList(Token token, TodoListId todoListId) throws SessionDoesNotExistsException {
        UserId userId = authentication.validate(token);

        TodoList todoList = new TodoList();
        todoList.setId(todoListId);
        todoList.setOwner(userId);

        todoListStorage.write(todoList);
    }

    /**
     * Creates new task in to-do list with given ID.
     *
     * @param token           `Token` of user who adds new task
     * @param taskId          ID of the task to add
     * @param todoListId      ID of to-do list to which task should be added
     * @param taskDescription description of task to add
     * @param completed       status of task to add
     * @param creationDate    date of creation of task to add
     * @param lastUpdateDate  date of last task update of task to add
     * @throws SessionDoesNotExistsException if session with given token doesn't exist
     */
    public void addTask(Token token,
                        TaskId taskId,
                        TodoListId todoListId,
                        String taskDescription,
                        boolean completed,
                        Date creationDate,
                        Date lastUpdateDate) throws SessionDoesNotExistsException {

        authentication.validate(token);

        Task task = new Task.TaskBuilder()
                .setTaskId(taskId)
                .setTodoListId(todoListId)
                .setDescription(taskDescription)
                .setCompleted(completed)
                .setCreationDate(creationDate)
                .setLastUpdateDate(lastUpdateDate)
                .build();

        taskStorage.write(task);
    }

    /**
     * Updates task with given ID.
     *
     * @param token           `Token` of user who updates task
     * @param taskId          ID of task to update
     * @param todoListId      ID of to-do list of the task
     * @param taskDescription description of updated task
     * @param completed       status of updated task
     * @param creationDate    date of creation of updated task
     * @param lastUpdateDate  date of last update of updated task
     * @throws SessionDoesNotExistsException if session with given token doesn't exist
     */
    public void updateTasks(Token token,
                            TaskId taskId,
                            TodoListId todoListId,
                            String taskDescription,
                            boolean completed,
                            Date creationDate,
                            Date lastUpdateDate) throws SessionDoesNotExistsException {

        authentication.validate(token);

        Task task = new Task.TaskBuilder()
                .setTaskId(taskId)
                .setTodoListId(todoListId)
                .setDescription(taskDescription)
                .setCompleted(completed)
                .setCreationDate(creationDate)
                .setLastUpdateDate(lastUpdateDate)
                .build();

        taskStorage.write(task);
    }

    /**
     * Deletes task by its ID.
     *
     * @param token  `Token` of user who deletes task
     * @param taskId ID of the task to delete.
     * @throws SessionDoesNotExistsException if session with given token doesn't exist
     * @throws TaskNotFoundException         if task with given ID was not found
     */
    public void deleteTask(Token token, TaskId taskId) throws SessionDoesNotExistsException, TaskNotFoundException {
        authentication.validate(token);

        Optional<Task> removedTask = taskStorage.remove(taskId);

        if (!removedTask.isPresent()) {
            throw new TaskNotFoundException();
        }
    }


}
