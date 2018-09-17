package org.javaclasses.todo.storage.impl;

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
    private final Map<I, E> storage = new HashMap<>();

    @Override
    public E write(E entity) {
        Optional<E> entityById = findById(entity.getId());

        if (entityById.isPresent()) {
            update(entityById.get());
            return entity;
        }

        return create(entity);
    }

    @Override
    public Optional<E> read(I id) {
        Preconditions.checkNotNull(id, "ID of Entity cannot be null");

        return findById(id);
    }

    private E create(E entity) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(entity.getId(), "To create Entity it must have not null ID.");

        storage.put(entity.getId(), entity);

        return entity;
    }

    @Override
    public Optional<E> findById(I id) {
        Preconditions.checkNotNull(id, "Cannot find entity with null ID.");

        return Optional.ofNullable(storage.get(id));
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


    @Override
    public List<E> findEntitiesWithField(String fieldName, Object fieldValue) {
        List<E> result = new LinkedList<>();

        try {

            for (E entity : storage.values()) {
                Class<? extends Entity> aClass = entity.getClass();
                Field declaredField = aClass.getDeclaredField(fieldName);

                declaredField.setAccessible(true);

                Object value = declaredField.get(entity);
                if (value.equals(fieldValue)) {
                    result.add(entity);
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
            return result;
        }

        return result;
    }

}
