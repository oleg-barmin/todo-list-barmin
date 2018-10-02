package org.javaclasses.todo.storage.impl;

import com.google.common.annotations.VisibleForTesting;
import org.javaclasses.todo.model.entity.AuthSession;
import org.javaclasses.todo.model.entity.Token;

import java.util.Map;

/**
 * Storage of {@code AuthSession} entity by their {@code Token}.
 *
 * @author Oleg Barmin
 */
public class AuthSessionStorage extends InMemoryStorage<Token, AuthSession> {

    public AuthSessionStorage() {
    }

    @VisibleForTesting
    AuthSessionStorage(Map<Token, AuthSession> map) {
        super(map);
    }
}
