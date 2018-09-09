import {Event, EventTypes} from "./event";

/**
 * Occurs when task with specified ID need to be completed.
 *
 * @extends Event
 */
export class TaskCompletionRequested extends Event {

    /**
     * Creates `TaskCompletionRequested` instance.
     *
     * @param {TaskId} taskId ID of task to remove.
     */
    constructor(taskId) {
        super(EventTypes.TaskCompletionRequested);
        this.taskId = taskId;
    }
}