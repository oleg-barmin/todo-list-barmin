package org.javaclasses.todo.model;

import java.util.Objects;

/**
 * Ensures {@code Entity} uniqueness.
 *
 * @param <V> value of ID
 * @author Oleg Barmin
 */
/* Abstract EntityId has no meaning in business logic of TodoList application, so has to be abstract.*/
@SuppressWarnings("AbstractClassWithoutAbstractMethods")
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

    public V getValue() {
        return value;
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
