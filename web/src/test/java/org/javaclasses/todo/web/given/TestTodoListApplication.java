package org.javaclasses.todo.web.given;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.web.TodoListApplication;

/**
 * Simplifies testing of  {@link TodoListApplication}.
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

    @Override
    public Authentication getAuthentication() {
        return super.getAuthentication();
    }
}
