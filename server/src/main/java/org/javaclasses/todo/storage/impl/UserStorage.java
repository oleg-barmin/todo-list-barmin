package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.User;
import org.javaclasses.todo.model.UserId;
import org.javaclasses.todo.model.Username;

import java.util.List;
import java.util.Optional;

/**
 * Storage of `User` entities by their `UserId`.
 */
public class UserStorage extends InMemoryStorage<UserId, User> {

    /**
     * Finds `User` in storage by given `Username`.
     *
     * @return Optional with `User` with given ID.
     * If optional is empty means that user with given `Username` doesn't exists in storage.
     */
    public Optional<User> findUserByUsername(Username username) {
        List<User> users = findEntitiesWithField("username", username);

        if (users.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(users.get(0));
    }
}
