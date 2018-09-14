package org.javaclasses.todo.model;

/**
 * An entity which represents a list of tasks to-do.
 * <p>
 * Contains:
 * - `TodoListId` which unifies `TodoList`
 * - `UserId` ID of user who owns `TodoList`
 */
public class TodoList extends Entity<TodoListId> {
    private TodoListId id;
    private UserId owner;

    public UserId getOwner() {
        return owner;
    }

    public void setOwner(UserId owner) {
        this.owner = owner;
    }
}
