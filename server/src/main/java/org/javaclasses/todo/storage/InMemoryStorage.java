package org.javaclasses.todo.storage;

import com.google.common.base.Preconditions;
import org.javaclasses.todo.model.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Storage of Entities by their ID in memory.
 *
 * @param <I> ID of Entity
 * @param <E> Entity to store
 */
public abstract class InMemoryStorage<I, E extends Entity<I>> implements Storage<I, E>{
    private final Map<I, E> storage = new HashMap<>();

    protected E create(E entity) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(entity.getId(), "To create Entity it must have not null ID.");

        storage.put(entity.getId(), entity);

        return entity;
    }

    protected Optional<E> findById(I id) {
        Preconditions.checkNotNull(id, "Cannot find entity with null ID.");

        return Optional.ofNullable(storage.get(id));
    }

    protected void update(E entity) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(entity.getId());

        storage.put(entity.getId(), entity);
    }

    @Override
    public Optional<E> remove(I id) {
        Preconditions.checkNotNull(id, "Cannot remove entity with null ID.");

        return Optional.ofNullable(storage.remove(id));
    }
}
