package org.javaclasses.todo.storage;

import com.google.common.base.Preconditions;
import org.javaclasses.todo.model.User;
import org.javaclasses.todo.model.UserId;

import java.util.Optional;

/**
 * Storage of `User` entities by their `UserId`.
 */
public class UserStorage extends InMemoryStorage<UserId, User> {

    @Override
    public User write(User entity) {
        Optional<User> userById = findById(entity.getId());

        if (userById.isPresent()) {
            update(userById.get());
            return entity;
        }

        return create(entity);
    }

    @Override
    public Optional<User> read(UserId id) {
        Preconditions.checkNotNull(id, "ID of User cannot be null");

        return findById(id);
    }
}
