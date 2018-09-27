package org.javaclasses.todo.web;

import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.Username;

import static org.javaclasses.todo.web.TestUsers.UN_SINGED_IN_USER;
import static org.javaclasses.todo.web.TestUsers.USER_1;

/**
 * Class to Test TodoListApplication and register {@code User}s to tests.
 */
class TestTodoListApplication extends TodoListApplication {

    /**
     * Creates {@code TestTodoListApplication} instance with given PORT.
     *
     * @param port port to start application on
     */
    TestTodoListApplication(int port) {
        super(port);
    }

    /**
     * Starts {@code TodoListApplication} on given port and registers all {@code User}s from {@link TestUsers}.
     */
    @Override
    public void start() {
        USER_1.setUsername(new Username("first_user"));
        USER_1.setPassword(new Password("first_user_password"));

        UN_SINGED_IN_USER.setUsername(new Username("un_signed_in_user"));
        UN_SINGED_IN_USER.setPassword(new Password("un_signed_in_user_password"));

        USER_1.setUserId(getAuthentication().createUser(USER_1.getUsername(), USER_1.getPassword()));
        USER_1.setToken(getAuthentication().signIn(USER_1.getUsername(), USER_1.getPassword()));


        UN_SINGED_IN_USER.setUserId(getAuthentication().createUser(
                UN_SINGED_IN_USER.getUsername(),
                UN_SINGED_IN_USER.getPassword()));


        super.start();
    }
}
