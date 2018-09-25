package org.javaclasses.todo.web;

import io.restassured.specification.RequestSpecification;
import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;
import static org.javaclasses.todo.web.PreRegisteredUsers.USER_1;

class AbstractControllerTest {
    private final StorageFactory storageFactory = new StorageFactory();
    private final ServiceFactory serviceFactory = new ServiceFactory(storageFactory);
    private final Authentication authentication = serviceFactory.getAuthentication();
    private final int port = PortProvider.getPort();

    private final TodoListApplication todoListApplication = new TodoListApplication(port, serviceFactory);
    private final RequestSpecification requestSpecification = given().port(port);

    RequestSpecification getRequestSpecification() {
        return requestSpecification;
    }

    public StorageFactory getStorageFactory() {
        return storageFactory;
    }

    Token signIn(Username username, Password password) {
        return authentication.signIn(username, password);
    }

    @BeforeEach
    void startServer() {
        todoListApplication.start();
    }

    /*
     * User ID is not needed in REST test.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeEach
    void registerUser() {
        authentication.createUser(USER_1.getUsername(), USER_1.getPassword());
    }

    @AfterEach
    void stopServer() {
        todoListApplication.stop();
    }
}
