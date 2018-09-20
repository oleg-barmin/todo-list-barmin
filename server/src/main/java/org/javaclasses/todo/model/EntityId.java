package org.javaclasses.todo.model;

import java.util.Objects;

/**
 * Ensures {@code Entity} uniqueness.
 *
 * @param <V> value of ID
 * @author Oleg Barmin
 */
public abstract class EntityId<V> {
    private final V value;

    /**
     * Creates {@code EntityId} instance.
     *
     * @param value value of ID
     */
    EntityId(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityId)) return false;
        EntityId<?> entityId = (EntityId<?>) o;
        return Objects.equals(value, entityId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "EntityId{" +
                "value=" + value +
                '}';
    }
}
