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

@DisplayName("UserStorage should")
class UserStorageTest extends InMemoryStorageTest<UserId, User> {
    private final Map<UserId, User> map = new HashMap<>();
    private final UserStorage storage = new UserStorage(map);


    private final Username username = new Username("exmapleUsername@gmail.ru");
    private final Password password = new Password("qwerty123");

    @Override
    UserId createID() {
        return new UserId(UUID.randomUUID().toString());
    }

    @Override
    User createEntity() {
        return createEntityWithId(createID());
    }

    @Override
    InMemoryStorage<UserId, User> getStorage() {
        return storage;
    }

    @Override
    // getMap should return same object for test needs
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    Map<UserId, User> getMap() {
        return map;
    }

    @Override
    User createEntityWithNullId() {
        return createEntityWithId(null);
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

        Optional<User> optionalUser = storage.findUserByUsername(entity.getUsername());

        if (!optionalUser.isPresent()) {
            Assertions.fail("Should return Optional with user, but don't.");
            return;
        }

        User user = optionalUser.get();

        Assertions.assertEquals(entity, user, "returned user should be equals to written, but it don't.");

    }
}