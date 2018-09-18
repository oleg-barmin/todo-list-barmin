package org.javaclasses.todo.storage.impl;

import com.google.common.annotations.VisibleForTesting;
import org.javaclasses.todo.model.AuthSession;
import org.javaclasses.todo.model.Token;

import java.util.Map;

/**
 * Storage of `AuthSession` entities by their `Token`.
 */
public class AuthSessionStorage extends InMemoryStorage<Token, AuthSession> {
    public AuthSessionStorage() {
    }

    @VisibleForTesting
    AuthSessionStorage(Map<Token, AuthSession> map) {
        super(map);
    }
}
