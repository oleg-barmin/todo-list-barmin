package org.javaclasses.todo.web;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.model.TaskId;
import org.javaclasses.todo.model.TodoListId;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.lang.String.format;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.DescribedAs.describedAs;
import static org.javaclasses.todo.web.SecuredAbstractRequestHandler.X_TODO_TOKEN;
import static org.javaclasses.todo.web.TestRoutesFormat.TASK_ROUTE_FORMAT;
import static org.javaclasses.todo.web.TestUsers.USER_1;

@DisplayName("RemoveTaskHandler should")
class RemoveTaskHandlerTest extends AbstractSecuredHandlerTest {

    private final RequestSpecification specification = getRequestSpecification();

    @Test
    @DisplayName("remove tasks from system by ID.")
    void testFindTaskById() {
        specification.header(X_TODO_TOKEN, USER_1.getToken().getValue());

        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());
        TaskId taskId = new TaskId(UUID.randomUUID().toString());

        addTodoList(todoListId);
        addTask(taskId, todoListId, "write tests on remove task by ID.");

        Response response = specification.delete(format(TASK_ROUTE_FORMAT, todoListId.getValue(), taskId.getValue()));

        response.then()
                .statusCode(describedAs("return status code 200, when " +
                                                "signed in user removes task by ID from his to-do list.", is(HTTP_OK)));
    }

    @Override
    Response sendRequest(Token token, UserId userId) {
        TaskId taskId = new TaskId(UUID.randomUUID().toString());
        TodoListId todoListId = new TodoListId(UUID.randomUUID().toString());

        addTodoList(todoListId);
        addTask(taskId, todoListId, "write negative cases tests on remove task by ID.");

        return specification.delete(format(TASK_ROUTE_FORMAT, todoListId.getValue(), taskId.getValue()));
    }
}
