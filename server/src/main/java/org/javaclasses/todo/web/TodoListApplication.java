package org.javaclasses.todo.web;

import com.google.common.annotations.VisibleForTesting;
import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.auth.InvalidCredentialsException;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.ServiceFactory;
import org.javaclasses.todo.model.TaskNotFoundException;
import org.javaclasses.todo.model.TodoListNotFoundException;
import org.javaclasses.todo.model.TodoService;
import org.javaclasses.todo.model.UpdateCompletedTaskException;
import spark.Service;

import static org.javaclasses.todo.web.AuthenticationController.AuthenticationHandler;
import static org.javaclasses.todo.web.Configurations.getDefaultPort;
import static org.javaclasses.todo.web.ExceptionHandlers.AuthorizationFailedHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.InvalidCredentialsHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.TaskNotFoundHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.TodoListNotFoundHandler;
import static org.javaclasses.todo.web.ExceptionHandlers.UpdateCompletedTaskHandler;
import static org.javaclasses.todo.web.Routes.getAuthenticationRoute;
import static org.javaclasses.todo.web.Routes.getCreateTodoListRoute;
import static org.javaclasses.todo.web.Routes.getReadTasksRoute;
import static org.javaclasses.todo.web.Routes.getTasksRoute;
import static org.javaclasses.todo.web.TaskController.CreateTaskRequestHandler;
import static org.javaclasses.todo.web.TaskController.GetTaskRequestHandler;
import static org.javaclasses.todo.web.TaskController.RemoveTaskRequestHandler;
import static org.javaclasses.todo.web.TaskController.UpdateTaskRequestHandler;
import static org.javaclasses.todo.web.TodoListController.ReadTasksRequestHandler;
import static spark.Service.ignite;

/**
 * Runs server with TodoList application which provides access to {@link TodoService} functionality.
 *
 * <p>Application is based on REST a architectural style.
 *
 * <p>Services allows to:
 * - Authenticate users;
 * - Create new to-do lists;
 * - Read tasks from to-do lists;
 * - Find tasks by ID;
 * - Add new tasks to to-do lists;
 * - Update existing tasks;
 * - Remove existing tasks.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings({"OverlyCoupledClass", // TodoListApplication is REST API, it needs to use many dependencies to work.
        "WeakerAccess"}) // TodoListApplication is public API, so its methods and static field must be public.
public class TodoListApplication {

    //TodoListApplication REST API URL parameters names.
    static final String TODO_LIST_ID_PARAM = ":todolistid";
    static final String TASK_ID_PARAM = ":taskid";



    private final Service service = ignite();
    private final Authentication authentication;
    private final TodoService todoService;

    /**
     * Creates {@code TodoListApplication} instance.
     *
     * @param port port to start application on
     */
    public TodoListApplication(int port) {
        ServiceFactory serviceFactory = new ServiceFactory();
        this.authentication = serviceFactory.getAuthentication();
        this.todoService = serviceFactory.getTodoService();
        service.port(port);
    }

    public static void main(String[] args) {
        new TodoListApplication(getDefaultPort()).start();
    }

    /**
     * Starts {@code TodoListApplication} server on given port.
     */
    @SuppressWarnings("OverlyCoupledMethod") // start server method needs many dependencies to init all handlers.
    public void start() {
        service.exception(AuthorizationFailedException.class, new AuthorizationFailedHandler());
        service.exception(InvalidCredentialsException.class, new InvalidCredentialsHandler());
        service.exception(TodoListNotFoundException.class, new TodoListNotFoundHandler());
        service.exception(TaskNotFoundException.class, new TaskNotFoundHandler());
        service.exception(UpdateCompletedTaskException.class, new UpdateCompletedTaskHandler());

        service.post(getAuthenticationRoute(), new AuthenticationHandler(authentication));

        service.post(getCreateTodoListRoute(), new TodoListController.CreateTodoListRequestHandler(todoService));

        service.get(getReadTasksRoute(), new ReadTasksRequestHandler(todoService));

        service.get(getTasksRoute(), new GetTaskRequestHandler(todoService));
        service.post(getTasksRoute(), new CreateTaskRequestHandler(todoService));
        service.put(getTasksRoute(), new UpdateTaskRequestHandler(todoService));
        service.delete(getTasksRoute(), new RemoveTaskRequestHandler(todoService));
    }

    /**
     * Stops to-do list application.
     */
    public void stop() {
        service.stop();
    }

    @VisibleForTesting
    public Authentication getAuthentication() {
        return authentication;
    }
}
