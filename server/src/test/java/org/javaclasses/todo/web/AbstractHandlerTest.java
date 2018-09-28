package org.javaclasses.todo.web;

import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.javaclasses.todo.web.Configurations.getContentType;
import static org.javaclasses.todo.web.PortProvider.getAvailablePort;
import static org.javaclasses.todo.web.Routes.getCreateTodoListRoute;
import static org.javaclasses.todo.web.TestRoutesFormat.TASK_ROUTE_FORMAT;

//abstract test has nothing to test.
@SuppressWarnings("AbstractClassWithoutAbstractMethods")
abstract class AbstractHandlerTest {
    private final int port = getAvailablePort();

    private final TodoListApplication todoListApplication = new TestTodoListApplication(port);
    private final RequestSpecification specification = given().port(port)
                                                              .contentType(getContentType());

    RequestSpecification getRequestSpecification() {
        return specification;
    }

    @BeforeEach
    void startServer() {
        todoListApplication.start();
    }

    @AfterEach
    void stopServer() {
        todoListApplication.stop();
    }

    void addTodoList(TodoListId todoListId) {
        CreateListPayload payload = new CreateListPayload(todoListId);
        specification.body(payload);
        specification.post(getCreateTodoListRoute());
    }

    void addTask(TaskId taskId, TodoListId todoListId, String description) {
        CreateTaskPayload payload = new CreateTaskPayload(description);
        specification.body(payload)
                     .post(format(TASK_ROUTE_FORMAT, todoListId.getValue(), taskId.getValue()));
    }

    Task readTask(TodoListId todoListId, TaskId taskId) {
        return specification.get(format(
                TASK_ROUTE_FORMAT,
                todoListId.getValue(),
                taskId.getValue()))
                            .body()
                            .as(Task.class);
    }
}
