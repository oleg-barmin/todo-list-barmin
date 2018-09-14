package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.impl.AuthSession;
import org.javaclasses.todo.model.impl.Token;

/**
 * Storage of `AuthSession` entities by their `Token`.
 */
public class AuthSessionStorage extends InMemoryStorage<Token, AuthSession> {
}
