package org.javaclasses.todo.web;

import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.web.given.TestEnvironment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;
import static org.javaclasses.todo.web.Configurations.getContentType;
import static org.javaclasses.todo.web.Routes.getTodoListRoute;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTaskUrl;

/**
 * An abstract integration test of all handlers, which starts and stops server on each test method,
 * provides methods to:
 * - read {@link Task TasksIdGenerator} {@link AbstractHandlerTest#readTask(TodoListId, TaskId)};
 * - add {@link Task TasksIdGenerator} {@link AbstractHandlerTest#addTask(TaskId, TodoListId, String)};
 * - add {@code TodoList} {@link AbstractHandlerTest#addTodoList(TodoListId)}.
 *
 * @author Oleg Barmin
 */
//abstract test has nothing to test.
@SuppressWarnings("AbstractClassWithoutAbstractMethods")
abstract class AbstractHandlerTest {

    private final TestEnvironment testEnvironment = new TestEnvironment();
    private final RequestSpecification specification = given().port(testEnvironment.getApplicationPort())
                                                              .contentType(getContentType());

    RequestSpecification getRequestSpecification() {
        return specification;
    }

    TestEnvironment getTestEnvironment() {
        return testEnvironment;
    }

    @BeforeEach
    void startServer() {
        testEnvironment.startServer();
    }

    @AfterEach
    void stopServer() {
        testEnvironment.stopServer();
    }

    void addTodoList(TodoListId todoListId) {
        CreateListPayload payload = new CreateListPayload(todoListId);
        specification.body(payload);
        specification.post(getTodoListRoute());
    }

    void addTask(TaskId taskId, TodoListId todoListId, String description) {
        CreateTaskPayload payload = new CreateTaskPayload(description);
        specification.body(payload)
                     .post(getTaskUrl(todoListId, taskId));
    }

    Task readTask(TodoListId todoListId, TaskId taskId) {
        return specification.get(getTaskUrl(todoListId, taskId))
                            .body()
                            .as(Task.class);
    }
}
