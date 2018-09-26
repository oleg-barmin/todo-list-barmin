package org.javaclasses.todo.web;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.model.*;
import spark.Service;

import java.util.Date;
import java.util.UUID;

import static org.javaclasses.todo.web.AuthenticationController.AuthenticationHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.*;
import static org.javaclasses.todo.web.ListController.ListCreationHandler;
import static org.javaclasses.todo.web.ListController.ReadTasksHandler;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;
import static org.javaclasses.todo.web.TaskController.CreateTaskHandler;
import static org.javaclasses.todo.web.TaskController.GetTaskHandler;
import static spark.Service.ignite;

/**
 * TodoList application.
 */
public class TodoListApplication {

    static final String CREATE_LIST_PATH = "/lists";
    static final String READ_TASKS_PATH = CREATE_LIST_PATH + "/:todolistid";
    private final Service service = ignite();

    static final String AUTHENTICATION_PATH = "/auth";
    private final ServiceFactory serviceFactory;

    /**
     * Creates {@code TodoListApplication} instance.
     *
     * @param port           port to start application on
     * @param serviceFactory factory of services
     */
    TodoListApplication(int port, ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        service.port(port);
    }

    public static void main(String[] args) {
        StorageFactory storageFactory = new StorageFactory();
        ServiceFactory serviceFactory = new ServiceFactory(storageFactory);

        Authentication authentication = serviceFactory.getAuthentication();
        UserId userId = authentication.createUser(USER_1.getUsername(), USER_1.getPassword());

        TodoListId todoListId = new TodoListId("1234");

        TodoList build = new TodoList.TodoListBuilder()
                .setTodoListId(todoListId)
                .setOwner(userId)
                .build();
        storageFactory.getTodoListStorage().write(build);

        storageFactory.getTaskStorage().write(
                new Task.TaskBuilder()
                        .setTaskId(new TaskId("123"))
                        .setTodoListId(todoListId)
                        .setDescription("implement read tasks by todo list ID")
                        .setCreationDate(new Date())
                        .build()
        );
        storageFactory.getTaskStorage().write(
                new Task.TaskBuilder()
                        .setTaskId(new TaskId(UUID.randomUUID().toString()))
                        .setTodoListId(todoListId)
                        .setDescription("second task")
                        .setCreationDate(new Date())
                        .build()
        );


        new TodoListApplication(4567, serviceFactory).start();
    }

    /**
     * Starts to-do list application on given port.
     */
    void start() {
        Authentication authentication = serviceFactory.getAuthentication();
        TodoService todoService = serviceFactory.getTodoService();

        service.exception(AuthorizationFailedException.class, new AuthorizationFailedHandler());
        service.exception(InvalidCredentialsException.class, new InvalidCredentialsHandler());
        service.exception(TodoListNotFoundException.class, new TodoListNotFoundHandler());
        service.exception(TaskNotFoundException.class, new TaskNotFoundHandler());

        service.post(AUTHENTICATION_PATH, new AuthenticationHandler(authentication));
        service.post(CREATE_LIST_PATH, new ListCreationHandler(todoService));
        service.get(READ_TASKS_PATH, new ReadTasksHandler(todoService));
        service.get("/lists/:todolistid/:taskid", new GetTaskHandler(todoService));
        service.post("/lists/:todolistid/:taskid", new CreateTaskHandler(todoService));
    }

    /**
     * Stops to-do list application.
     */
    void stop() {
        service.stop();
    }

}
