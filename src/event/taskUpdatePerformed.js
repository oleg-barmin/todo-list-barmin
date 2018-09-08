import {Event, EventTypes} from "./event";

export class TaskUpdatePerformed extends Event{
    /**
     * todo jsdoc
     *
     * @param {string} errorMsg description of error
     */
    constructor(taskId) {
        super(EventTypes.TaskUpdatePerformed);
        this.taskId = taskId;
    }
}