package org.javaclasses.todo.model;

import java.util.Objects;

/**
 * Represents an abstract entity which is used in TodoList application.
 *
 * <p>Each entity has ID {@link EntityId} to ensure its uniques.
 *
 * @param <I> ID of the entity
 * @author Oleg Barmin
 */
public abstract class Entity<I extends EntityId> {
    private final I id;

    Entity(I i) {
        this.id = i;
    }

    public I getId() {
        return id;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(getId(), entity.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId());
    }
}
