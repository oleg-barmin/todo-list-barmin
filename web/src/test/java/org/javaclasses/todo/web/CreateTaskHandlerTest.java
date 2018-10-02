package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.entity.Task;
import org.javaclasses.todo.model.entity.TaskId;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.model.entity.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.given.TasksIdGenerator.generateTaskId;
import static org.javaclasses.todo.web.given.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.given.TodoListsIdGenerator.generateTodoListId;
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
        setTokenToRequestSpecification(specification);

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();
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
        setTokenToRequestSpecification(specification);

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();
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
        setTokenToRequestSpecification(specification);

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();
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
        setTokenToRequestSpecification(specification);

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();
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
        setTokenToRequestSpecification(specification);

        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();
        String taskDescription = "watch \"Predator\"";

        CreateTaskPayload payload = new CreateTaskPayload(taskDescription);

        Response response = specification.body(payload)
                                         .post(getTaskUrl(todoListId, taskId));

        response.then()
                .statusCode(describedAs("response with status code 403 when try to add " +
                                                "task with non-existing to-do list ID.",
                                        is(HTTP_FORBIDDEN)));
    }

    @Test
    @DisplayName("response with 403 status code when create task in other user to-do list.")
    void testCreationTaskInOtherUserTodoList() {
        setTokenToRequestSpecification(specification);

        TodoListId firstUserTodoListId = generateTodoListId();
        addTodoList(firstUserTodoListId);

        RequestSpecification secondUserSpec = getNewSpecification();

        TaskId secondUserTaskId = generateTaskId();
        TodoListId secondUserTodoListId = generateTodoListId();

        addTodoList(secondUserTodoListId, secondUserSpec);

        CreateTaskPayload payload = new CreateTaskPayload("win the race");

        Response response = specification.body(payload)
                                         .post(getTaskUrl(secondUserTodoListId, secondUserTaskId));

        response.then()
                .statusCode(HTTP_FORBIDDEN);
    }

    @Override
    Response sendRequest(UserId userId) {
        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

        addTodoList(todoListId);

        CreateTaskPayload payload = new CreateTaskPayload("implement task creation tests");

        return specification.body(payload)
                            .post(getTaskUrl(todoListId, taskId));
    }

    @Override
    Response sendWithBodyRequest(String requestBody) {
        TaskId taskId = generateTaskId();
        TodoListId todoListId = generateTodoListId();

        addTodoList(todoListId);

        return specification.body(requestBody)
                            .post(getTaskUrl(todoListId, taskId));
    }
}
