package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Finds task by ID.
 *
 * @author Oleg Barmin
 */
public class FindTask extends Operation<FindTask> {

    private final TaskId taskId;
    private final Authorization authorization;

    /**
     * Creates {@code FindTask} instance.
     *
     * @param taskId         ID of the {@code Task} to find
     * @param authorization  to validate if user has access to {@code Task} with given ID
     * @param authentication to validate user token
     */
    FindTask(TaskId taskId, Authorization authorization, Authentication authentication) {
        super(authentication);
        this.taskId = checkNotNull(taskId);
        this.authorization = checkNotNull(authorization);
    }

    /**
     * Finds task with given ID.
     *
     * @return task with given ID
     * @throws TaskNotFoundException        if task to find doesn't exist
     * @throws TodoListNotFoundException    if {@code TodoList} with ID specified in task doesn't exist.
     * @throws AuthorizationFailedException if user has no authority to read {@code Task} with given ID
     */
    public Task execute() {
        return authorization.validateAccess(validateToken(), taskId);
    }
}
