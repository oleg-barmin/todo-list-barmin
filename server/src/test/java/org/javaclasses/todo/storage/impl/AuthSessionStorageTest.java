package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.AuthSession;
import org.javaclasses.todo.model.Token;
import org.javaclasses.todo.model.UserId;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@DisplayName("AuthSessionStorage should")
class AuthSessionStorageTest extends InMemoryStorageTest<Token, AuthSession>{
    private Map<Token, AuthSession> map = new HashMap<>();
    private AuthSessionStorage storage = new AuthSessionStorage(map);

    @Override
    InMemoryStorage<Token, AuthSession> getStorage() {
        return storage;
    }

    @Override
    Map<Token, AuthSession> getMap() {
        return map;
    }

    @Override
    Token createID() {
        return new Token(UUID.randomUUID().toString());
    }

    @Override
    AuthSession createEntity() {
        AuthSession authSession = new AuthSession();
        authSession.setId(createID());
        authSession.setUserId(new UserId(UUID.randomUUID().toString()));
        return authSession;
    }
}