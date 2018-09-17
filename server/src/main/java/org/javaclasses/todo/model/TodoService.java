package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.storage.impl.TaskStorage;
import org.javaclasses.todo.storage.impl.TodoListStorage;

import java.util.Date;
import java.util.List;

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

    public final class AddTask {
        private final Task.TaskBuilder taskBuilder;

        private AddTask(TaskId taskId) {
            taskBuilder = new Task.TaskBuilder();
            taskBuilder.setTaskId(taskId);
            taskBuilder.setCreationDate(new Date());
        }

        public AddTask withTodoListId(TodoListId todoListId) {
            taskBuilder.setTodoListId(todoListId);

            return this;
        }

        public AddTask withDescription(String description) {
            Descriptions.validate(description);
            taskBuilder.setDescription(description);
            return this;
        }

        void execute(Token token) {
            authentication.validate(token);

            Task build = taskBuilder.build();
            taskStorage.write(build);
        }
    }

    public final class UpdateTask {
        private final Task.TaskBuilder taskBuilder;

        private UpdateTask(TaskId taskId) {
            taskBuilder = new Task.TaskBuilder();
            taskBuilder.setTaskId(taskId);
        }

        public UpdateTask withTodoListId(TodoListId todoListId) {
            taskBuilder.setTodoListId(todoListId);
            return this;
        }

        public UpdateTask withDescription(String description) {
            Descriptions.validate(description);
            taskBuilder.setDescription(description);
            return this;
        }

        public UpdateTask withStatus(boolean completed) {
            taskBuilder.setStatus(completed);
            return this;
        }

        public UpdateTask withCreationDate(Date creationDate) {
            taskBuilder.setCreationDate(creationDate);
            return this;
        }

        public UpdateTask withLastUpdateDate(Date lastUpdateDate) {
            taskBuilder.setLastUpdateDate(lastUpdateDate);
            return this;
        }

        public void execute(Token token) {
            authentication.validate(token);

            Task build = taskBuilder.build();
            taskStorage.write(build);
        }
    }

    public final class RemoveTask {
        private final TaskId taskId;

        private RemoveTask(TaskId taskId) {
            this.taskId = taskId;
        }

        public void execute(Token token) {
            authentication.validate(token);

            taskStorage.remove(this.taskId);
        }
    }

    public final class CreateList {
        private final TodoList.TodoListBuilder todoListBuilder;

        private CreateList(TodoListId todoListId) {
            todoListBuilder = new TodoList.TodoListBuilder();
            todoListBuilder.setTodoListId(todoListId);
        }

        public CreateList withOwner(UserId userId) {
            todoListBuilder.setOwner(userId);
            return this;
        }

        public void execute(Token token) {
            authentication.validate(token);

            TodoList todoList = todoListBuilder.build();

            todoListStorage.write(todoList);
        }
    }

    public class TodoListTasks {
        private TodoListId todoListId;

        private TodoListTasks(TodoListId todoListId) {
            this.todoListId = todoListId;
        }

        public List<Task> execute(Token token) {
            authentication.validate(token);

            return taskStorage.getAllTaskOfTodoList(todoListId);
        }
    }
}
