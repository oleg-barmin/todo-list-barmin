package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TestRoutesFormat.TASK_ROUTE_FORMAT;
import static org.javaclasses.todo.web.TestUsers.USER_1;

@DisplayName("UpdateTaskHandler should")
class UpdateTaskHandlerTest extends AbstractSecuredHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    @Test
    @DisplayName("update tasks in the system.")
    void testUpdateTask() {
        specification.header(X_TODO_TOKEN, USER_1.getToken().getValue());

        TaskId taskId = new TaskId(UUID.randomUUID().toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        addTodoList(todoListId);
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

        addTodoList(todoListId);
        addTask(taskId, todoListId, "write negative cases tests on task update.");

        TaskUpdatePayload payload = new TaskUpdatePayload(false, "new task description");

        return specification.body(new Gson().toJson(payload))
                .put(format(TASK_ROUTE_FORMAT, todoListId.getValue(), taskId.getValue()));
    }
}
