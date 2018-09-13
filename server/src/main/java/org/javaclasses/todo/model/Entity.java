package org.javaclasses.todo.model;

/**
 * Represents record in database of business entity.
 *
 * @param <Id> ID of the entity
 */
public abstract class Entity<Id> {
    private Id id;

    public Id getId(){
        return id;
    }
}
