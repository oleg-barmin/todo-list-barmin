import {Event, EventTypeEnumeration} from "./event";

/**
 * Occurs when task with specified id need to be removed.
 *
 * @extends Event
 */
export class TaskRemovalRequest extends Event {

    /**
     * Creates `TaskRemovalRequest` instance.
     *
     * @param {TaskId} taskId id of task to remove.
     */
    constructor(taskId) {
        super(EventTypeEnumeration.TaskRemovalRequest);
        this.taskId = taskId;
    }
}