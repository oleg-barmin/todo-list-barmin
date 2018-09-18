package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.Entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

abstract class InMemoryStorageTest<ID, E extends Entity<ID>> {
    private InMemoryStorage<ID, E> storage;
    private Map<ID, E> map;

    abstract ID createID();
    abstract E createEntity();
    abstract InMemoryStorage<ID,E> getStorage();
    abstract Map<ID,E> getMap();

    @BeforeEach
    void init(){
        map = getMap();
        storage = getStorage();
    }

    @Test
    @DisplayName("write new entities.")
    void testWrite() {
        E entity = createEntity();
        entity.setId(createID());

        ID entityId = entity.getId();
        storage.write(entity);

        Assertions.assertTrue(map.containsKey(entityId),
                "ID of entity as key should be written to map, but it don't.");
        Assertions.assertEquals(entity, map.get(entityId),
                "Entity in storage should be equals to entity which was saved, but it don't.");
    }

    @Test
    @DisplayName("throw NullPointerException if try to write null entity.")
    void testWriteNullEntity() {
        Assertions.assertThrows(NullPointerException.class, () -> storage.write(null),
                "should throw NullPointerException if try to write null entity, but it don't.");
    }

    @Test
    @DisplayName("throw NullPointerException if try to write entity with null ID.")
    void testWriteEntityWithNullId() {
        E entity = createEntity();
        entity.setId(null);

        Assertions.assertThrows(NullPointerException.class, () -> storage.write(entity),
                "should throw NullPointerException if try to write entity with null ID, but it don't.");
    }

    @Test
    @DisplayName("override existing entity if try to write entity with same ID.")
    void testOverride() {
        E entity = createEntity();
        entity.setId(createID());
        ID entityId = entity.getId();

        storage.write(entity);

        E entityToOverride = createEntity();
        entityToOverride.setId(entityId);

        storage.write(entityToOverride);

        Assertions.assertEquals(entityToOverride, map.get(entityId),
                "entity in storage should be overridden, but it don't.");
    }

    @Test
    @DisplayName("read entities by ID.")
    void testRead() {
        E entity = createEntity();
        entity.setId(createID());

        ID entityId = entity.getId();
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
        ID entityId = createID();

        Optional<E> optional = storage.read(entityId);

        Assertions.assertFalse(optional.isPresent(),
                "should return empty optional if ID doesn't exist in storage, but it don't.");
    }

    @Test
    @DisplayName("throw NullPointerException if read method argument is null.")
    void testReadWithNullID() {
        Assertions.assertThrows(NullPointerException.class, () -> storage.read(null),
                "should throw NullPointerException if read method argument is null, but it don't.");
    }

    @Test
    @DisplayName("remove entities by ID")
    void removeTest() {
        E entity = createEntity();
        entity.setId(createID());

        ID entityId = entity.getId();
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
    void testWriteNonExistingEntity() {
        ID entityId = createID();

        Optional<E> optional = storage.remove(entityId);

        Assertions.assertFalse(optional.isPresent(),
                "should return empty optional if ID doesn't exist in storage, but it don't.");
    }
}