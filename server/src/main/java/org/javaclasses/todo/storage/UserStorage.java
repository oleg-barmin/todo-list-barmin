package org.javaclasses.todo.storage;

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
     *         If optional is empty means that user with given `Username` doesn't exists in storage.
     */
    public Optional<User> findUserByUsername(Username username) {
        List<User> userList = all();

        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }
}
