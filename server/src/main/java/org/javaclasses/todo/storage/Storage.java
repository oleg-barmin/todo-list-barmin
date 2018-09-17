package org.javaclasses.todo.storage;

import org.javaclasses.todo.model.Entity;

import java.util.List;
import java.util.Optional;

/**
 * Allows to access entity storage and read, create, update `Entity` in storage.
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
     * @return entity which was stored
     */
    E write(E entity);

    /**
     * Finds entity in storage.
     *
     * @param id ID of entity to find
     * @return returns Optional with `entity` with given ID.
     *         If optional is empty, `Entity` with given ID doesn't exists in storage.
     */
    Optional<E> read(I id);

    /**
     * Removes Entity with given ID from the storage .
     *
     * @param id ID of the entity to remove
     * @return Optional with removed entity.
     *         If optional is empty, entity with given ID doesn't exists in storage
     */
    Optional<E> remove(I id);

    /**
     * Finds `Entity` with given ID.
     *
     * @param id ID to find `Entity` with.
     * @return Optional with found entity.
     *         If optional is empty, entity with given ID doesn't exists in storage
     */
    Optional<E> findById(I id);

    /**
     * Finds all entities which field with given name, has given value.
     *
     * @param fieldName name of `Entity` field
     * @param fieldValue value of desired field
     *
     * @return list of entities with field of desired value
     */
    List<E> findEntitiesWithField(String fieldName, Object fieldValue);
}
