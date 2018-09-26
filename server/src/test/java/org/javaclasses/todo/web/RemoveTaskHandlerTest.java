package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;

@Nested
class RemoveTaskHandlerTest extends AbstractSecuredHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    private final Username username = USER_1.getUsername();
    private final Password password = USER_1.getPassword();

    @Test
    @DisplayName("remove tasks by ID.")
    void testFindTaskById() {
        Token token = signIn(username, password);
        specification.header(X_TODO_TOKEN, token.getValue());

        UserId userId = getUserId();

        String todoListUuid = UUID.randomUUID().toString();
        TodoListId todoListId = new TodoListId(todoListUuid);

        String taskUuid = UUID.randomUUID().toString();
        TaskId taskId = new TaskId(taskUuid);

        addTodoList(todoListId, userId);
        addTask(taskId, todoListId, "write tests on remove task by ID.");

        Response response = specification.delete(String.format("/lists/%s/%s", todoListUuid, taskUuid));

        Assertions.assertEquals(HTTP_OK, response.getStatusCode(),
                "return status code 200, when signed in user removes task by ID from his to-do list.");
    }

    @Override
    Response performOperation(Token token, UserId userId) {
        String taskUuid = UUID.randomUUID().toString();
        String todoListUuid = UUID.randomUUID().toString();

        TaskId taskId = new TaskId(taskUuid);
        TodoListId todoListId = new TodoListId(todoListUuid);

        addTodoList(todoListId, userId);
        addTask(taskId, todoListId, "write negative cases tests on remove task by ID.");

        Response response = specification.delete(String.format("/lists/%s/%s", todoListUuid, taskUuid));
        return response;
    }
}
