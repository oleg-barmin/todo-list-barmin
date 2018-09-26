package org.javaclasses.todo.web;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;
import static org.javaclasses.todo.web.TodoListApplication.CREATE_LIST_PATH;

//abstract test has nothing to test.
@SuppressWarnings("AbstractClassWithoutAbstractMethods")
abstract class AbstractHandlerTest {
    private final StorageFactory storageFactory = new StorageFactory();
    private final ServiceFactory serviceFactory = new ServiceFactory(storageFactory);
    private final Authentication authentication = serviceFactory.getAuthentication();
    private final int port = PortProvider.getPort();

    private final TodoListApplication todoListApplication = new TodoListApplication(port, serviceFactory);
    private final RequestSpecification specification = given().port(port);

    private UserId userId;

    RequestSpecification getRequestSpecification() {
        return specification;
    }

    Token signIn(Username username, Password password) {
        return authentication.signIn(username, password);
    }

    @BeforeEach
    void startServer() {
        todoListApplication.start();
    }


    //User ID is not needed in REST test.
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeEach
    void registerUser() {
        userId = authentication.createUser(USER_1.getUsername(), USER_1.getPassword());
    }

    @AfterEach
    void stopServer() {
        todoListApplication.stop();
    }

    UserId getUserId() {
        return userId;
    }

    void addTodoList(TodoListId todoListId, UserId userId) {
        CreateListPayload payload = new CreateListPayload(userId, todoListId);
        String requestBody = new Gson().toJson(payload);

        specification.body(requestBody);
        specification.post(CREATE_LIST_PATH);
    }

    void addTask(TaskId taskId, TodoListId todoListId, String description) {
        String payload = new Gson().toJson(new CreateTaskPayload(description));
        specification.body(payload);
        specification.post(String.format("/lists/%s/%s",
                todoListId.getValue(), taskId.getValue()));
    }

    Task readTask(TodoListId todoListId, TaskId taskId) {
        Response response = getRequestSpecification().get(String.format("/lists/%s/%s", todoListId.getValue(), taskId.getValue()));

        String taskJson = response.body().asString();

        return new Gson().fromJson(taskJson, Task.class);
    }
}
