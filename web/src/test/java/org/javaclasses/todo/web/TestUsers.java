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
    USER_1, UN_SINGED_IN_USER;

    private UserId userId;
    private Username username;
    private Password password;

    private Token token;

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

    public void setUsername(Username username) {
        this.username = username;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }
}
