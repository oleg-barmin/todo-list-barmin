package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TestRoutesFormat.TASK_ROUTE_FORMAT;

@DisplayName("UpdateTaskHandler should")
class UpdateTaskHandlerTest extends AbstractSecuredHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    private final Username username = USER_1.getUsername();
    private final Password password = USER_1.getPassword();

    @Test
    @DisplayName("update tasks in the system.")
    void testUpdateTask() {
        Token token = signIn(username, password);
        specification.header(X_TODO_TOKEN, token.getValue());

        TaskId taskId = new TaskId(UUID.randomUUID().toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        addTodoList(todoListId, getUserId());
        addTask(taskId, todoListId, "write tests on update task.");

        TaskUpdatePayload payload = new TaskUpdatePayload(false, "complete this test");

        Response response = specification.body(new Gson().toJson(payload))
                .put(format(TASK_ROUTE_FORMAT, todoListId.getValue(), taskId.getValue()));

        Assertions.assertEquals(HTTP_OK, response.getStatusCode());
    }


    @Override
    Response sendRequest(Token token, UserId userId) {
        TaskId taskId = new TaskId(UUID.randomUUID().toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        addTodoList(todoListId, userId);
        addTask(taskId, todoListId, "write negative cases tests on task update.");

        TaskUpdatePayload payload = new TaskUpdatePayload(false, "new task description");

        return specification.body(new Gson().toJson(payload))
                .put(format(TASK_ROUTE_FORMAT, todoListId.getValue(), taskId.getValue()));
    }
}
