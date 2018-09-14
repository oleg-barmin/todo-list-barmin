package org.javaclasses.todo.model;

import java.util.Date;

/**
 * An entity which represents task to do.
 * <p>
 * It stores task ID, its description, status (is it completed or not),
 * date of creation and date of last update (if task wasn't updated
 * the date of creation equals to last update date).
 */
public class Task extends Entity<TaskId> {
    private TodoListId todoListId;
    private String description;
    private boolean completed;
    private Date creationDate;
    private Date lastUpdateDate;

    public TodoListId getTodoListId() {
        return todoListId;
    }

    public void setTodoListId(TodoListId todoListId) {
        this.todoListId = todoListId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
