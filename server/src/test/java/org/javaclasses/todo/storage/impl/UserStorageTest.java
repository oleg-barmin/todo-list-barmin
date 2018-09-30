package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.User;
import org.javaclasses.todo.model.UserId;
import org.javaclasses.todo.model.Username;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Testing {@link UserStorage}:
 * - basic methods inherited from {@link InMemoryStorage};
 * - {@link UserStorage#findBy(Username)}.
 *
 * @author Oleg Barmin
 */
@DisplayName("UserStorage should")
class UserStorageTest extends InMemoryStorageTest<UserId, User> {
    private final Map<UserId, User> map = new HashMap<>();
    private final UserStorage storage = new UserStorage(map);

    private final Username username = new Username("exmapleUsername@gmail.ru");
    private final Password password = new Password("qwerty123");

    @Override
    User createEntity() {
        return createEntityWithId(createID());
    }

    @Override
    User createEntityWithNullId() {
        return createEntityWithId(null);
    }

    @Override
    UserId createID() {
        return new UserId(UUID.randomUUID()
                              .toString());
    }

    @Override
    // getMap should return same object for test needs
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    Map<UserId, User> getMap() {
        return map;
    }

    @Override
    InMemoryStorage<UserId, User> getStorage() {
        return storage;
    }

    @Override
    User createEntityWithId(UserId entityId) {
        User user = new User(entityId);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    @Test
    @DisplayName("find user by username.")
    void testFindUserByUsername() {
        User entity = createEntity();

        storage.write(entity);

        Optional<User> optionalUser = storage.findBy(entity.getUsername());

        if (!optionalUser.isPresent()) {
            Assertions.fail("return Optional with user, but don't.");
            return;
        }

        User user = optionalUser.get();

        Assertions.assertEquals(entity, user, "find user by username, but it don't.");

    }
}