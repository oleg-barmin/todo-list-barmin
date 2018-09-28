package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TestRoutesProvider.getTaskUrl;
import static org.javaclasses.todo.web.TestUsers.USER_1;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Oleg Barmin
 */
@DisplayName("CreateTaskHandlerTest should")
class CreateTaskHandlerTest extends AbstractSecuredHandlerTest {

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
                .statusCode(describedAs("response with status code 200, but it don't.", is(HTTP_OK)));

        Task task = readTask(todoListId, taskId);
        assertEquals(taskId, task.getId(), "add task with uploaded ID, but it don't.");
        assertEquals(todoListId, task.getTodoListId(), "add task with uploaded to-do list ID, but it don't.");
        assertEquals(taskDescription, taskDescription, "add task with uploaded description, but it don't.");
    }

    @Override
    Response sendRequest(Token token, UserId userId) {
        TaskId taskId = new TaskId(UUID.randomUUID()
                                       .toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID()
                                                   .toString());

        addTodoList(todoListId);

        CreateTaskPayload payload = new CreateTaskPayload("implement task creation tests");

        return specification.body(payload)
                            .post(getTaskUrl(todoListId, taskId));
    }
}
