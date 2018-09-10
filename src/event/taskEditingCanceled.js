import {Event, EventTypes} from "./event";

/**
 * Occurs when end-user tries to cancel a task editing.
 *
 * @extends Event
 */
export class TaskEditingCanceled extends Event {

    /**
     * Creates `TaskEditingCanceled` instance.
     *
     * @param {TaskId} taskId ID of a task which editing was canceled
     */
    constructor(taskId) {
        super(EventTypes.TaskEditingCanceled);
        this.taskId = taskId;
    }
}