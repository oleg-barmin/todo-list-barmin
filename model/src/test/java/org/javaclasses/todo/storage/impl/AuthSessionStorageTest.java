package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.entity.AuthSession;
import org.javaclasses.todo.model.entity.Token;
import org.javaclasses.todo.model.entity.UserId;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Testing {@link AuthSessionStorage}.
 *
 * @author Oleg Barmin
 */
@DisplayName("AuthSessionStorage should")
class AuthSessionStorageTest extends InMemoryStorageTest<Token, AuthSession> {
    private final Map<Token, AuthSession> map = new HashMap<>();
    private final AuthSessionStorage storage = new AuthSessionStorage(map);

    @Override
    Token createID() {
        return new Token(UUID.randomUUID()
                             .toString());
    }

    @Override
    // getMap should return same object for test needs
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    Map<Token, AuthSession> getMap() {
        return map;
    }

    @Override
    InMemoryStorage<Token, AuthSession> getStorage() {
        return storage;
    }

    @Override
    AuthSession createEntityWithId(Token entityId) {
        AuthSession authSession = new AuthSession(entityId);
        authSession.setUserId(new UserId(UUID.randomUUID()
                                             .toString()));
        return authSession;
    }
}