package org.javaclasses.todo.web.given;

import org.javaclasses.todo.model.TaskId;

import java.util.UUID;

/**
 * Generates unique {@link TaskId}.
 *
 * @author Oleg Barmin
 * @implNote ID generation is based on {@link UUID}
 */
public class TasksIdGenerator {

    private TasksIdGenerator() {
    }

    /**
     * Generates unique {@code TaskId}.
     *
     * @return unique {@code TaskId}
     */
    public static TaskId generateTaskId() {
        return new TaskId(UUID.randomUUID()
                              .toString());
    }
}
