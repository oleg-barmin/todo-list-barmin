package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.AuthSession;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Oleg Barmin
 */
@DisplayName("AuthSessionStorage should")
class AuthSessionStorageTest extends InMemoryStorageTest<Token, AuthSession> {
    private final Map<Token, AuthSession> map = new HashMap<>();
    private final AuthSessionStorage storage = new AuthSessionStorage(map);

    @Override
    InMemoryStorage<Token, AuthSession> getStorage() {
        return storage;
    }

    @Override
    // getMap should return same object for test needs
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    Map<Token, AuthSession> getMap() {
        return map;
    }

    @Override
    AuthSession createEntityWithId(Token entityId) {
        AuthSession authSession = new AuthSession(entityId);
        authSession.setUserId(new UserId(UUID.randomUUID().toString()));
        return authSession;
    }

    @Override
    Token createID() {
        return new Token(UUID.randomUUID().toString());
    }
}