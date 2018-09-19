package org.javaclasses.todo.storage.impl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import org.javaclasses.todo.model.Entity;
import org.javaclasses.todo.storage.Storage;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Storage of Entities by their ID in memory.
 *
 * @param <I> ID of Entity
 * @param <E> Entity to store
 */
public abstract class InMemoryStorage<I, E extends Entity<I>> implements Storage<I, E> {
    private final Map<I, E> storage;

    protected InMemoryStorage() {
        this.storage = new HashMap<>();
    }

    @VisibleForTesting
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    InMemoryStorage(Map<I, E> map) {
        this.storage = map;
    }

    @Override
    public E write(E entity) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(entity.getId());

        Optional<E> entityById = read(entity.getId());

        if (entityById.isPresent()) {
            update(entity);
            return entity;
        }

        return create(entity);
    }

    @Override
    public Optional<E> read(I id) {
        Preconditions.checkNotNull(id, "ID of Entity cannot be null");

        return Optional.ofNullable(storage.get(id));
    }

    private E create(E entity) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(entity.getId(), "To create Entity it must have not null ID.");

        storage.put(entity.getId(), entity);

        return entity;
    }

    private void update(E entity) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(entity.getId());

        storage.put(entity.getId(), entity);
    }

    @Override
    public Optional<E> remove(I id) {
        Preconditions.checkNotNull(id, "Cannot remove entity with null ID.");

        return Optional.ofNullable(storage.remove(id));
    }

    /**
     * Finds all entities which field with given name, has given value.
     *
     * @param fieldName  name of {@code Entity} field
     * @param fieldValue value of desired field
     * @return list of entities with field of desired value
     * @throws SearchByFieldException if field with given name doesn't exists in entity.
     */
    @SuppressWarnings("ThrowInsideCatchBlockWhichIgnoresCaughtException")
    List<E> findByField(String fieldName, Object fieldValue) throws SearchByFieldException {
        List<E> result = new LinkedList<>();

        for (E entity : storage.values()) {
            Class<? extends Entity> aClass = entity.getClass();

            Field declaredField;

            try {
                declaredField = aClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                throw new SearchByFieldException(fieldName);
            }

            try {
                declaredField.setAccessible(true);
                Object value = declaredField.get(entity);
                if (value.equals(fieldValue)) {
                    result.add(entity);
                }
            } catch (IllegalAccessException e) {
                throw new SearchByFieldException(fieldName);
            } finally {
                declaredField.setAccessible(false);
            }
        }

        return result;
    }

}
