package org.javaclasses.todo.web;

import org.javaclasses.todo.auth.Authentication;

import static org.mockito.Mockito.mock;

/* Services cannot be serializable. */
@SuppressWarnings("NonSerializableFieldInSerializableClass")
class MockServiceFactory {

    static {
        TodoListApplicationEnum.INSTANCE.todoListApplication.start();
    }

    private MockServiceFactory() {
    }

    static Authentication getAuthenticationMock() {
        return AuthenticationEnum.INSTANCE.authentication;
    }

    private enum TodoListApplicationEnum {
        INSTANCE;

        private final TodoListApplication todoListApplication;

        TodoListApplicationEnum() {
            this.todoListApplication = new TodoListApplication(getAuthenticationMock());
        }
    }

    private enum AuthenticationEnum {
        INSTANCE;

        private final Authentication authentication;

        AuthenticationEnum() {
            this.authentication = mock(Authentication.class);
        }
    }
}
