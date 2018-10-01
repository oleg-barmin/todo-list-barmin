package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.storage.impl.TaskStorage;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * API which simplifies task removing.
 *
 * <p>To remove task, ID of the task to remove should be given.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of public API and its methods should be public.
public final class RemoveTask extends Operation<RemoveTask> {

    private final TaskId taskId;
    private final TaskStorage taskStorage;
    private final Authorization authorization;

    /**
     * Creates {@code RemoveTask} instance.
     *
     * @param taskId         ID of the {@code Task} to remove
     * @param taskStorage    storage to remove {@code Task} from
     * @param authorization  to validate access to {@code Task}
     * @param authentication to authenticate user token
     */
    RemoveTask(TaskId taskId, TaskStorage taskStorage, Authorization authorization,
               Authentication authentication) {
        super(authentication);
        this.taskId = checkNotNull(taskId);
        this.taskStorage = checkNotNull(taskStorage);
        this.authorization = checkNotNull(authorization);
    }

    /**
     * Removes task with given ID from storage.
     *
     * @throws TaskNotFoundException        if task with given ID was not found
     * @throws TodoListNotFoundException    if try to remove task from list which doesn't exist
     * @throws AuthorizationFailedException if given user has no authority to remove task with given ID
     */
    //return values is not needed to remove task
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void execute() throws TaskNotFoundException {
        UserId userId = validateToken();

        Optional<Task> optionalTask = taskStorage.read(taskId);

        if (!optionalTask.isPresent()) {
            throw new TaskNotFoundException(taskId);
        }

        Task task = optionalTask.get();

        authorization.validateAccess(userId, task.getTodoListId());
        taskStorage.remove(taskId);
    }
}
