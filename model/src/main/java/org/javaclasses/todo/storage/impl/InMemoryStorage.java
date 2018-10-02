package org.javaclasses.todo.storage.impl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import org.javaclasses.todo.model.entity.Entity;
import org.javaclasses.todo.model.entity.EntityId;
import org.javaclasses.todo.storage.Storage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Storage of Entities by their ID in memory.
 *
 * @param <I> ID of Entity
 * @param <E> Entity to store
 * @author Oleg Barmin
 */
/*
 * Sub-classes of InMemoryStorage must work only with entity of TodoList application,
 * so InMemoryStorage with generics should be abstract.
 */
@SuppressWarnings("AbstractClassWithoutAbstractMethods")
abstract class InMemoryStorage<I extends EntityId, E extends Entity<I>> implements Storage<I, E> {

    private final Map<I, E> storage;

    InMemoryStorage() {
        this.storage = new HashMap<>();
    }

    @VisibleForTesting
    /* For test needs given map should be same object. */
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    InMemoryStorage(Map<I, E> map) {
        this.storage = map;
    }

    @Override
    public void write(E entity) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(entity.getId());

        Optional<E> entityById = read(entity.getId());

        if (entityById.isPresent()) {
            update(entity);
            return;
        }

        create(entity);
    }

    @Override
    public Optional<E> read(I id) {
        Preconditions.checkNotNull(id, "ID of Entity cannot be null");

        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<E> remove(I id) {
        Preconditions.checkNotNull(id, "Cannot remove entity with null ID.");

        return Optional.ofNullable(storage.remove(id));
    }

    @Override
    public void clear() {
        this.storage.clear();
    }

    private void create(E entity) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(entity.getId(), "To create Entity it must have not null ID.");

        storage.put(entity.getId(), entity);
    }

    private void update(E entity) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(entity.getId());

        storage.put(entity.getId(), entity);
    }

    /**
     * Finds all entity which field with given name, has given value.
     *
     * @param fieldName  name of {@code Entity} field
     * @param fieldValue value of desired field
     * @return list of entity with field of desired value
     * @throws SearchByFieldException if field with given name doesn't exists in entity.
     */
    /* reflect API converted to SearchByFieldException, because in this case it marks about programming error. */
    @SuppressWarnings("ThrowInsideCatchBlockWhichIgnoresCaughtException")
    List<E> findByField(String fieldName, Object fieldValue) throws SearchByFieldException {
        List<E> result = new ArrayList<>();

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
