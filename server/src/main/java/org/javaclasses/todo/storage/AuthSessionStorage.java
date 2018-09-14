package org.javaclasses.todo.storage;

import com.google.common.base.Preconditions;
import org.javaclasses.todo.model.AuthSession;
import org.javaclasses.todo.model.Token;

import java.util.Optional;

/**
 * Storage of `AuthSession` entities by their `Token`.
 */
public class AuthSessionStorage extends InMemoryStorage<Token, AuthSession> {

    @Override
    public AuthSession write(AuthSession entity) {
        Optional<AuthSession> authSessionById = findById(entity.getId());

        if (authSessionById.isPresent()) {
            update(authSessionById.get());
            return entity;
        }

        return create(entity);
    }

    @Override
    public Optional<AuthSession> read(Token id) {
        Preconditions.checkNotNull(id, "ID of AuthSession cannot be null");

        return findById(id);
    }
}
