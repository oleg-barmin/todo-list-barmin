package org.javaclasses.todo.model;

import com.google.common.base.Preconditions;
import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.auth.SessionDoesNotExistsException;
import org.javaclasses.todo.storage.impl.TaskStorage;
import org.javaclasses.todo.storage.impl.TodoListStorage;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.*;

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
    private Authentication authentication;
    private TodoListStorage todoListStorage;
    private TaskStorage taskStorage;

    public TodoService(Authentication authentication, TodoListStorage todoListStorage, TaskStorage taskStorage) {
        this.authentication = authentication;
        this.todoListStorage = todoListStorage;
        this.taskStorage = taskStorage;
    }

    /**
     * Provides `CreateList` instance to create new `TodoList`.
     *
     * @param todoListId ID of to-do list to create
     * @return `CreateList` instance to build `TodoList` to create and upload it
     */
    CreateList createList(TodoListId todoListId) {
        return new CreateList(todoListId);
    }

    /**
     * Provides `TodoListTasks` instance to retrieve list of Tasks of specified `TodoList`.
     *
     * @param todoListId ID of `TodoList` which tasks required
     * @return `TodoListTasks` instance to build request to retrieve list of Tasks of specified `TodoList`
     */
    TodoListTasks getTasksOfTodoList(TodoListId todoListId) {
        return new TodoListTasks(todoListId);
    }

    /**
     * Provides `AddTask` instance to add new `Task`.
     *
     * @param taskId ID of the task to add
     * @return `AddTask` instance to build request to add new Task
     */
    AddTask addTask(TaskId taskId) {
        return new AddTask(taskId);
    }

    /**
     * Provides `UpdateTask` instance to update existing `Task`.
     *
     * @param taskId ID of the task to update
     * @return `UpdateTask` instance to build task to update an upload changes
     */
    UpdateTask updateTask(TaskId taskId) {
        return new UpdateTask(taskId);
    }

    /**
     * Provides `RemoveTask` instance to remove existing `Task`.
     *
     * @param taskId ID of the task to remove
     * @return `RemoveTask` instance to build task to remove it
     */
    RemoveTask removeTask(TaskId taskId) {
        return new RemoveTask(taskId);
    }

    /**
     * Provides API which simplifies task adding.
     * <p>
     * To add task these fields should be provided:
     * - ID of the task
     * - ID of `TodoList` to which it belongs
     * - description of the task to add
     */
    public final class AddTask {
        private final Task.TaskBuilder taskBuilder;

        /**
         * Creates `AddTask` instance.
         *
         * @param taskId ID of the task to add
         * @throws NullPointerException if given `taskId` is null
         */
        private AddTask(TaskId taskId) throws NullPointerException {
            checkNotNull(taskId);

            taskBuilder = new Task.TaskBuilder();
            taskBuilder.setTaskId(taskId);
            taskBuilder.setCreationDate(new Date());
        }

        /**
         * Sets `TodoList` to which belongs task to add
         *
         * @param todoListId ID of `TodoList` to which belongs this task
         * @return this `AddTask` instance to continue request building
         * @throws NullPointerException if given `todoListId` is null
         */
        public AddTask withTodoListId(TodoListId todoListId) throws NullPointerException {
            taskBuilder.setTodoListId(todoListId);

            return this;
        }

        /**
         * Sets description of task to add.
         *
         * @param description description of task to add
         * @return this `AddTask` instance to continue request building
         * @throws EmptyTaskDescriptionException if given task description is null or empty
         */
        public AddTask withDescription(String description) throws EmptyTaskDescriptionException {
            Descriptions.validate(description);

            taskBuilder.setDescription(description);
            return this;
        }

        /**
         * Uploads task with previously set fields.
         *
         * @param token token of user who adds task
         * @throws NullPointerException          if task description, todoListId or taskId was not provided
         * @throws SessionDoesNotExistsException if token of user was expired
         */
        void execute(Token token) throws SessionDoesNotExistsException, NullPointerException {
            Task build = taskBuilder.build();

            authentication.validate(token);
            taskStorage.write(build);
        }
    }

    /**
     * Provides API which simplifies task updating.
     * <p>
     * To update task these fields should be provided:
     * - ID of the task to update
     * - new description of the task.
     * - optionally, new task status (by default false)
     * <p>
     * Last update date sets automatically to current date.
     * <p>
     * Other task fields remains the same as in stored task.
     */
    public final class UpdateTask {
        private final Task.TaskBuilder taskBuilder;
        private final TaskId taskId;


        /**
         * Creates `UpdateTask` instance.
         *
         * @param taskId ID of the task to update
         * @throws TaskNotFoundException if task with given ID doesn't exist in storage
         * @throws NullPointerException  if given `taskId` is null
         */
        private UpdateTask(TaskId taskId) throws NullPointerException, TaskNotFoundException {
            checkNotNull(taskId);
            this.taskId = taskId;
            taskBuilder = new Task.TaskBuilder();
        }

        /**
         * Sets new description of task.
         *
         * @param description new description of task
         * @return this `UpdateTask` instance to continue request building
         * @throws EmptyTaskDescriptionException if given task description is null or empty
         */
        public UpdateTask withDescription(String description) throws EmptyTaskDescriptionException {
            Descriptions.validate(description);
            taskBuilder.setDescription(description);
            return this;
        }

        /**
         * Sets new status of task (is it completed or not).
         *
         * @param completed new status of task.
         * @return this `UpdateTask` instance to continue request building
         */
        public UpdateTask withStatus(boolean completed) {
            taskBuilder.setStatus(completed);
            return this;
        }

        /**
         * Uploads previously modified task to storage.
         *
         * @param token token of user who updates task.
         * @throws SessionDoesNotExistsException if token of user was expired
         * @throws NullPointerException          if task description wasn't set.
         */
        public void execute(Token token) throws SessionDoesNotExistsException, NullPointerException {
            authentication.validate(token);

            Optional<Task> taskToUpdate = taskStorage.findById(taskId);
            if (!taskToUpdate.isPresent()) {
                throw new TaskNotFoundException();
            }
            Task task = taskToUpdate.get();

            taskBuilder.setTaskId(taskId);
            taskBuilder.setTodoListId(task.getTodoListId());
            taskBuilder.setCreationDate(task.getCreationDate());
            taskBuilder.setLastUpdateDate(new Date());

            Task build = taskBuilder.build();
            taskStorage.write(build);
        }
    }

    /**
     * Provides API which simplifies task removing.
     * <p>
     * To remove task ID of the task to remove should be given.
     */
    public final class RemoveTask {
        private final TaskId taskId;

        /**
         * Creates `RemoveTask` instance.
         *
         * @param taskId ID of the task to remove
         * @throws NullPointerException if given task ID is null
         */
        private RemoveTask(TaskId taskId) throws NullPointerException {
            checkNotNull(taskId);

            this.taskId = taskId;
        }

        /**
         * Removes tas with given ID from storage.
         *
         * @param token token of user who removes task
         * @throws SessionDoesNotExistsException if given token was expired
         */
        public void execute(Token token) throws SessionDoesNotExistsException {
            authentication.validate(token);

            taskStorage.remove(this.taskId);
        }
    }

    /**
     * Provides API which simplifies creation of new `TodoList`.
     * <p>
     * To create new `TodoList` these fields should be provided:
     * - ID of the `TodoList` to create
     * - ID of user who owns this `TodoList`
     */
    public final class CreateList {
        private final TodoList.TodoListBuilder todoListBuilder;

        /**
         * Creates `CreateList` instance.
         *
         * @param todoListId ID of `TodoList` to create
         * @throws NullPointerException if given `todoListId` is null
         */
        private CreateList(TodoListId todoListId) throws NullPointerException {
            checkNotNull(todoListId);

            todoListBuilder = new TodoList.TodoListBuilder();
            todoListBuilder.setTodoListId(todoListId);
        }

        /**
         * Sets owner of the `TodoList` to create.
         *
         * @param userId ID of user who owns `TodoList`
         * @return this `CreateList` instance to continue request building
         */
        public CreateList withOwner(UserId userId) {
            todoListBuilder.setOwner(userId);
            return this;
        }

        /**
         * Creates new `TodoList` with given fields in storage.
         *
         * @param token token of user who created `TodoList`;
         * @throws SessionDoesNotExistsException if given toke was expired
         * @throws NullPointerException          if ID of owner was not provided
         */
        public void execute(Token token) throws SessionDoesNotExistsException {
            authentication.validate(token);

            TodoList todoList = todoListBuilder.build();

            todoListStorage.write(todoList);
        }
    }

    /**
     * Provides API which simplifies retrieving of all task which belongs to specified `TodoList` by its ID.
     */
    public class TodoListTasks {
        private TodoListId todoListId;

        /**
         * Creates `TodoListTasks` instance.
         *
         * @param todoListId ID of the `TodoList` which task should be found
         * @throws NullPointerException if given `todoListId` is null
         */
        private TodoListTasks(TodoListId todoListId) throws NullPointerException {
            checkNotNull(todoListId);

            this.todoListId = todoListId;
        }

        /**
         * Provides list of tasks which belongs to `TodoList` with given ID.
         *
         * @param token token of user who requested task list.
         * @return list of task which belongs to specified `TodoList`
         * @throws SessionDoesNotExistsException if given token was expired
         */
        public List<Task> execute(Token token) throws SessionDoesNotExistsException {
            authentication.validate(token);

            return taskStorage.getAllTaskOfTodoList(todoListId);
        }
    }
}
