package org.javaclasses.todo.web;

import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.Username;

/**
 * Default Users to test application with.
 */
//Fields should not be serialized
@SuppressWarnings("NonSerializableFieldInSerializableClass")
enum PreRegisteredUsers {
    USER_1;

    private final Username username;
    private final Password password;

    PreRegisteredUsers() {
        username = new Username("first_user");
        password = new Password("first_User_password_123");
    }

    public Username getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }
}
