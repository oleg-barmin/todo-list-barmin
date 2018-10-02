package org.javaclasses.todo.storage;

import org.javaclasses.todo.model.entity.Entity;
import org.javaclasses.todo.model.entity.EntityId;

import java.util.Optional;

/**
 * Allows to access entity storage and read, create, update {@code Entity} in storage.
 *
 * @param <I> ID of the entity
 * @param <E> Entity to store
 * @author Oleg Barmin
 */
public interface Storage<I extends EntityId, E extends Entity<I>> {

    /**
     * Creates new entity if entity with same ID hasn't existed or
     * overwrites previous entity.
     *
     * @param entity entity to store
     */
    void write(E entity);

    /**
     * Finds entity in storage.
     *
     * @param id ID of entity to find
     * @return returns Optional with `entity` with given ID.
     * If optional is empty, `Entity` with given ID doesn't exists in storage.
     */
    Optional<E> read(I id);

    /**
     * Removes Entity with given ID from the storage .
     *
     * @param id ID of the entity to remove
     * @return Optional with removed entity.
     * If optional is empty, entity with given ID doesn't exists in storage
     */
    Optional<E> remove(I id);

    /**
     * Erases all data stored in storage.
     */
    void clear();
}
