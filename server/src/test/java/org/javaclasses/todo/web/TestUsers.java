package org.javaclasses.todo.web;

import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.UserId;
import org.javaclasses.todo.model.Username;

/**
 * Test Users to test {@link TodoListApplication} with.
 */
//Fields should not be serialized
@SuppressWarnings("NonSerializableFieldInSerializableClass")
enum TestUsers {
    USER_1;

    private UserId userId;
    private final Username username;
    private final Password password;

    private Token token;

    TestUsers() {
        username = new Username("first_user");
        password = new Password("first_User_password_123");
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }
}
