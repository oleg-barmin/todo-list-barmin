import {Event, EventTypes} from "./event";

/**
 * Occurs when processing of `TaskRemovalRequested` event was performed successfully.
 */
export class TaskRemoved extends Event{

    /**
     * Creates `TaskRemoved` instance.
     *
     * @param taskId ID of the task, which removal was performed
     */
    constructor(taskId){
        super(EventTypes.TaskRemoved);
        this.taskId = taskId;
    }
}