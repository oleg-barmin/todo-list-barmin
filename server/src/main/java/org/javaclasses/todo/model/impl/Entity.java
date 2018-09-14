package org.javaclasses.todo.model.impl;

/**
 * Represents an abstract entity which is used in TodoList application.
 *
 * @param <Id> ID of the entity
 */
public abstract class Entity<Id> {
    private Id id;

    public void setId(Id id) {
        this.id = id;
    }

    public Id getId() {
        return id;
    }

}
