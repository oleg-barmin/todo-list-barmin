package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.javaclasses.todo.web.PortProvider.getAvailablePort;
import static org.javaclasses.todo.web.Routes.getCreateTodoListRoute;
import static org.javaclasses.todo.web.TestRoutesFormat.TASK_ROUTE_FORMAT;

//abstract test has nothing to test.
@SuppressWarnings("AbstractClassWithoutAbstractMethods")
abstract class AbstractHandlerTest {
    private final int port = getAvailablePort();

    private final TodoListApplication todoListApplication = new TestTodoListApplication(port);
    private final RequestSpecification specification = given().port(port);

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
        String requestBody = new Gson().toJson(payload);

        specification.body(requestBody);
        specification.post(getCreateTodoListRoute());
    }

    void addTask(TaskId taskId, TodoListId todoListId, String description) {
        String payload = new Gson().toJson(new CreateTaskPayload(description));
        specification.body(payload);
        specification.post(format(TASK_ROUTE_FORMAT, todoListId.getValue(), taskId.getValue()));
    }

    Task readTask(TodoListId todoListId, TaskId taskId) {
        Response response = specification.get(format(
                TASK_ROUTE_FORMAT,
                todoListId.getValue(),
                taskId.getValue()));

        String taskJson = response.body()
                                  .asString();

        return new Gson().fromJson(taskJson, Task.class);
    }
}
