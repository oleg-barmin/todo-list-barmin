import {Event, EventTypes} from "./event";

/**
 * Occurs when task with specified ID need to be removed.
 *
 * @extends Event
 * @author Oleg Barmin
 */
export class TaskRemovalRequested extends Event {

    /**
     * Creates `TaskRemovalRequested` instance.
     *
     * @param {TaskId} taskId ID of task to remove.
     * @param {TodoListId} todoListId ID of `TodoList` which task was removed
     */
    constructor(taskId, todoListId) {
        super(EventTypes.TaskRemovalRequested);
        this.taskId = taskId;
        this.todoListId = todoListId;
    }
}