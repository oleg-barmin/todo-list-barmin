package org.javaclasses.todo.storage;

import com.google.common.base.Preconditions;
import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TaskId;

import java.util.Optional;

/**
 * Storage of `Task` entities by their `TaskId`.
 */
public class TaskStorage extends InMemoryStorage<TaskId, Task> {

    @Override
    public Task write(Task entity) {
        Optional<Task> taskById = findById(entity.getId());

        if (taskById.isPresent()) {
            update(taskById.get());
            return entity;
        }

        return create(entity);
    }

    @Override
    public Optional<Task> read(TaskId id) {
        Preconditions.checkNotNull(id, "ID of Task cannot be null");

        return findById(id);
    }
}
