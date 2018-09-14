package org.javaclasses.todo.storage.impl;

import org.javaclasses.todo.model.impl.Task;
import org.javaclasses.todo.model.impl.TaskId;

/**
 * Storage of `Task` entities by their `TaskId`.
 */
public class TaskStorage extends InMemoryStorage<TaskId, Task> {

}
