import {Event, EventTypeEnumeration} from "./event";

/**
 * Occurs when end user tries to edit a task.
 *
 * @extends Event
 */
export class StartTaskEditing extends Event {

    /**
     * Creates `StartTaskEditing` instance.
     *
     * @param taskId id of a task which editing was requested.
     */
    constructor(taskId) {
        super(EventTypeEnumeration.StartTaskEditing);
        this.taskId = taskId;
    }
}