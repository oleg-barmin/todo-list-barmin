import {Event, EventTypeEnumeration} from "./event";

/**
 * Occurs when task with specified id need to be completed.
 *
 * @extends Event
 */
export class TaskCompletionRequested extends Event {

    /**
     * Creates `TaskCompletionRequested` instance.
     *
     * @param {TaskId} taskId id of task to remove.
     */
    constructor(taskId) {
        super(EventTypeEnumeration.TaskCompletionRequested);
        this.taskId = taskId;
    }
}