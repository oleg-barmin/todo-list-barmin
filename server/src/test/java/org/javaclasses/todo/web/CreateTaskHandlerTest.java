package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.TestUsers.USER_1;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test of {@link Task} creation with REST API.
 *
 * @author Oleg Barmin
 */
@DisplayName("CreateTaskHandlerTest should")
class CreateTaskHandlerTest extends AbstractPayloadHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    @Test
    @DisplayName("create tasks.")
    void testTaskCreation() {
        specification.header(X_TODO_TOKEN, USER_1.getToken()
                                                 .getValue());

        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());
        String taskDescription = "implement task creation";

        CreateTaskPayload payload = new CreateTaskPayload(taskDescription);

        addTodoList(todoListId);

        Response response = specification.body(payload)
                                         .post(getTaskUrl(todoListId, taskId));
        response.then()
                .statusCode(
                        describedAs("response with status code 200, but it don't.", is(HTTP_OK)));

        Task task = readTask(todoListId, taskId);
        assertEquals(taskId, task.getId(), "add task with uploaded ID, but it don't.");
        assertEquals(todoListId, task.getTodoListId(),
                     "add task with uploaded to-do list ID, but it don't.");
        assertEquals(taskDescription, taskDescription,
                     "add task with uploaded description, but it don't.");
    }

    @Test
    @DisplayName("response with 500 status code if given task description is empty.")
    void testCreationTaskWithEmptyDescription() {
        specification.header(X_TODO_TOKEN, USER_1.getToken()
                                                 .getValue());

        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());
        String taskDescription = "";

        CreateTaskPayload payload = new CreateTaskPayload(taskDescription);

        addTodoList(todoListId);

        Response response = specification.body(payload)
                                         .post(getTaskUrl(todoListId, taskId));
        response.then()
                .statusCode(describedAs("response with status code 500, but it don't.",
                                        is(HTTP_INTERNAL_ERROR)));

    }

    @Test
    @DisplayName("response with 500 status code if given task description is filled only with spaces.")
    void testCreationTaskWithOnlySpacesDescription() {
        specification.header(X_TODO_TOKEN, USER_1.getToken()
                                                 .getValue());

        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());
        String taskDescription = "    ";

        CreateTaskPayload payload = new CreateTaskPayload(taskDescription);

        addTodoList(todoListId);

        Response response = specification.body(payload)
                                         .post(getTaskUrl(todoListId, taskId));
        response.then()
                .statusCode(describedAs("response with status code 500 " +
                                                "when description is only spaces, but it don't.",
                                        is(HTTP_INTERNAL_ERROR)));
    }

    @Test
    @DisplayName("response with 403 status code when create task with existing ID.")
    void testCreationTaskWithExistingId() {
        specification.header(X_TODO_TOKEN, USER_1.getToken()
                                                 .getValue());

        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());
        String taskDescription = "buy milk";

        CreateTaskPayload payload = new CreateTaskPayload(taskDescription);

        addTodoList(todoListId);
        addTask(taskId, todoListId, taskDescription);

        Response response = specification.body(payload)
                                         .post(getTaskUrl(todoListId, taskId));

        response.then()
                .statusCode(describedAs("response with status code 403 " +
                                                "when try to add task with existing ID.",
                                        is(HTTP_FORBIDDEN)));
    }

    @Test
    @DisplayName("response with 403 status code when create task with non-existing to-do-list ID.")
    void testCreationTaskWithNonExistingId() {
        specification.header(X_TODO_TOKEN, USER_1.getToken()
                                                 .getValue());

        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());
        String taskDescription = "watch \"Predator\"";

        CreateTaskPayload payload = new CreateTaskPayload(taskDescription);

        Response response = specification.body(payload)
                                         .post(getTaskUrl(todoListId, taskId));

        response.then()
                .statusCode(describedAs("response with status code 403 when try to add " +
                                                "task with non-existing to-do list ID.",
                                        is(HTTP_FORBIDDEN)));
    }

    @Override
    Response sendRequest(UserId userId) {
        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());

        addTodoList(todoListId);

        CreateTaskPayload payload = new CreateTaskPayload("implement task creation tests");

        return specification.body(payload)
                            .post(getTaskUrl(todoListId, taskId));
    }

    @Override
    Response sendEmptyPayloadRequest() {
        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());

        addTodoList(todoListId);

        return specification.body("")
                            .post(getTaskUrl(todoListId, taskId));
    }
}
