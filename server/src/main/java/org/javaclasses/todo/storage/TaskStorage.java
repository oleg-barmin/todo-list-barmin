package org.javaclasses.todo.storage;

import org.javaclasses.todo.model.Task;
import org.javaclasses.todo.model.TaskId;

/**
 * Storage of `Task` entities by their `TaskId`.
 */
public class TaskStorage extends InMemoryStorage<TaskId, Task> {

}
