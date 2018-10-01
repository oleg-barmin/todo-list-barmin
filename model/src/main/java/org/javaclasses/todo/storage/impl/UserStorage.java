package org.javaclasses.todo.storage.impl;

import com.google.common.annotations.VisibleForTesting;
import org.javaclasses.todo.model.User;
import org.javaclasses.todo.model.UserId;
import org.javaclasses.todo.model.Username;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Storage of {@code User} entities by their {@code UserId}.
 *
 * @author Oleg Barmin
 */
public class UserStorage extends InMemoryStorage<UserId, User> {

    public UserStorage() {
    }

    @VisibleForTesting
    UserStorage(Map<UserId, User> map) {
        super(map);
    }

    /**
     * Finds {@code User} in storage by given {@code Username}.
     *
     * @return Optional with {@code User} with given ID.
     * If optional is empty means that user with given {@code Username} doesn't exists in storage.
     */
    public Optional<User> findBy(Username username) {
        List<User> users = findByField("username", username);

        if (users.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(users.get(0));
    }
}
