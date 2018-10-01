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

    /**
     * Creates new {@link RequestSpecification} to test cases with multiple users.
     *
     * @return new {@code RequestSpecification} instance
     */
    RequestSpecification getNewSpecification() {
        return given().port(testEnvironment.getApplicationPort())
                      .contentType(getContentType());
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

    /**
     * Adds new {@code TodoList} with {@link AbstractHandlerTest#specification}.
     *
     * @param todoListId ID of {@code TodoList} to add.
     */
    void addTodoList(TodoListId todoListId) {
        CreateListPayload payload = new CreateListPayload(todoListId);
        specification.body(payload);
        specification.post(getTodoListRoute());
    }

    /**
     * Adds new {@code TodoList} with given {@code RequestSpecification}.
     *
     * @param todoListId           ID of {@code TodoList} to add
     * @param requestSpecification request specification to use
     */
    void addTodoList(TodoListId todoListId, RequestSpecification requestSpecification) {
        CreateListPayload payload = new CreateListPayload(todoListId);
        requestSpecification.body(payload);
        requestSpecification.post(getTodoListRoute());
    }

    /**
     * Adds new {@code Task} with {@link AbstractHandlerTest#specification}.
     *
     * @param taskId      ID of {@code Task} to add
     * @param todoListId  ID of {@code TodoList} to which tasks belongs
     * @param description description of {@code Task} to add
     */
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
