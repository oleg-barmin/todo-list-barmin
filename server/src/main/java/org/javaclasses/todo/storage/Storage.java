package org.javaclasses.todo.storage;

import org.javaclasses.todo.model.Entity;

/**
 * Allow to access access entity storage and read, create, update `Entity` in storage.
 *
 * @param <I> ID of the entity
 * @param <E> Entity to store
 */
public interface Storage<I, E extends Entity<I>> {

    /**
     * Creates new entity if entity with same ID din't existed or
     * updates previous entity.
     *
     * @param entity entity to store
     *
     * @return entity which was stored
     */
    E write(E entity);

    /**
     * Finds entity in storage.
     *
     * @param id ID of entity to find
     *
     * @return Entity with given ID
     */
    E read(I id);
}
