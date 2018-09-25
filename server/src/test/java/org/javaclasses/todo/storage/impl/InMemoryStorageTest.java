package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.Entity;
import org.javaclasses.todo.model.EntityId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

/**
 * @author Oleg Barmin
 */
abstract class InMemoryStorageTest<I extends EntityId, E extends Entity<I>> {
    private InMemoryStorage<I, E> storage;
    private Map<I, E> map;

    E createEntity() {
        return createEntityWithId(createID());
    }

    E createEntityWithNullId() {
        return createEntityWithId(null);
    }

    abstract I createID();

    abstract Map<I, E> getMap();

    abstract InMemoryStorage<I, E> getStorage();

    abstract E createEntityWithId(@Nullable I entityId);

    @BeforeEach
    void init() {
        map = getMap();
        storage = getStorage();
    }

    @Test
    @DisplayName("write new entities.")
    void testWrite() {
        E entity = createEntity();

        I entityId = entity.getId();
        storage.write(entity);

        Assertions.assertEquals(entity, map.get(entityId),
                "write Entity in storage, but it don't.");
    }

    @Test
    @DisplayName("throw NullPointerException if try to write null entity.")
    void testWriteNullEntity() {
        Assertions.assertThrows(NullPointerException.class, () -> storage.write(null),
                "throw NullPointerException if try to write null entity, but it don't.");
    }

    @Test
    @DisplayName("throw NullPointerException if try to write entity with null ID.")
    void testWriteEntityWithNullId() {
        E entity = createEntityWithNullId();

        Assertions.assertThrows(NullPointerException.class, () -> storage.write(entity),
                "throw NullPointerException if try to write entity with null ID, but it don't.");
    }

    @Test
    @DisplayName("overwrite existing entity if try to write entity with same ID.")
    void testOverwrite() {
        E entity = createEntity();
        I entityId = entity.getId();

        storage.write(entity);

        E entityToOverride = createEntityWithId(entityId);

        storage.write(entityToOverride);

        Assertions.assertEquals(entityToOverride, map.get(entityId),
                "overwrite entity, but it don't.");
    }


    @Test
    @DisplayName("read entities by ID.")
    void testRead() {
        E entity = createEntity();
        I entityId = entity.getId();

        storage.write(entity);

        Optional<E> optionalEntity = storage.read(entityId);
        if (!optionalEntity.isPresent()) {
            Assertions.fail("Storage has to read written entities by ID, but it don't.");
            return;
        }
        E storedEntity = optionalEntity.get();

        Assertions.assertEquals(storedEntity, map.get(entityId),
                "Storage has to read entity by ID, but it don't");
    }

    @Test
    @DisplayName("return empty Optional if try to read entity with ID, which doesn't exist in storage")
    void testReadNonExistingEntity() {
        I entityId = createID();

        Optional<E> optional = storage.read(entityId);

        Assertions.assertFalse(optional.isPresent(),
                "return empty optional on read if ID doesn't exist in storage, but it don't.");
    }

    @Test
    @DisplayName("throw NullPointerException if read method argument is null.")
    void testReadWithNullID() {
        Assertions.assertThrows(NullPointerException.class, () -> storage.read(null),
                "throw NullPointerException if read method argument is null, but it don't.");
    }

    @Test
    @DisplayName("remove entities by ID")
    void removeTest() {
        E entity = createEntity();
        I entityId = entity.getId();

        storage.write(entity);

        Optional<E> optionalEntity = storage.remove(entityId);
        if (!optionalEntity.isPresent()) {
            Assertions.fail("Storage has to remove written entities by ID, but it don't.");
            return;
        }
        E storedEntity = optionalEntity.get();

        Assertions.assertEquals(entity, storedEntity,
                "Storage has to remove written entity by ID, but it don't.");
    }

    @Test
    @DisplayName("return empty Optional if try to remove entity with ID, which doesn't exist in storage")
    void testRemoveNonExistingEntity() {
        I entityId = createID();

        Optional<E> optional = storage.remove(entityId);

        Assertions.assertFalse(optional.isPresent(),
                "return empty optional on remove if I doesn't exist in storage, but it don't.");
    }

    @Test
    @DisplayName("erase all stored entities.")
    void testClear() {
        E firstEntity = createEntity();
        E secondEntity = createEntity();
        E thirdEntity = createEntity();

        storage.write(firstEntity);
        storage.write(secondEntity);
        storage.write(thirdEntity);

        storage.clear();

        Assertions.assertEquals(0, map.size(), "erase all entities from storage, but it don't.");
    }

    /*
     * Write return value not needed, because exception should be thrown.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void testFindByField() {
        E entity = createEntity();
        storage.write(entity);

        Assertions.assertThrows(SearchByFieldException.class,
                () -> storage.findByField("1impossibleField", new Object()));
    }
}