package org.javaclasses.todo.web;

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
        USER_1.setUserId(getAuthentication().createUser(USER_1.getUsername(), USER_1.getPassword()));
        USER_1.setToken(getAuthentication().signIn(USER_1.getUsername(), USER_1.getPassword()));

        super.start();
    }
}
