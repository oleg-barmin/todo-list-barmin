package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserStorage should")
class UserStorageTest extends InMemoryStorageTest<UserId, User>{
    private Map<UserId, User> map = new HashMap<>();
    private UserStorage storage = new UserStorage(map);

    @Override
    UserId createID() {
        return new UserId(UUID.randomUUID().toString());
    }

    @Override
    User createEntity() {
        User user = new User();
        user.setId(createID());
        user.setUsername(new Username("username"));
        user.setPassword(new Password("password"));

        return user;
    }

    @Override
    InMemoryStorage<UserId, User> getStorage() {
        return storage;
    }

    @Override
    Map<UserId, User> getMap() {
        return map;
    }

    @Test
    @DisplayName("find user by username.")
    void testFindUserByUsername(){
        User entity = createEntity();

        storage.write(entity);

        Optional<User> optionalUser = storage.findUserByUsername(new Username(entity.getUsername().getUsername()));

        if(!optionalUser.isPresent()){
            Assertions.fail("Should return Optional with user, but don't.");
            return;
        }

        User user = optionalUser.get();

        Assertions.assertEquals(entity, user, "returned user should be equals to written, but it don't.");

    }
}