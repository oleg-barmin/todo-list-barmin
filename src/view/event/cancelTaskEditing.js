import {Event, EventTypeEnumeration} from "./event";

/**
 * Occurs when end user tries to cancel a task editing.
 *
 * @extends Event
 */
export class CancelTaskEditing extends Event {

    /**
     * Creates `CancelTaskEditing` instance.
     *
     * @param taskId id of a task which editing was canceled
     */
    constructor(taskId) {
        super(EventTypeEnumeration.CancelTaskEditing);
        this.taskId = taskId;
    }
}