import {Event, EventTypes} from "./event";

/**
 * Occurs when processing of `TaskRemovalRequested` event was performed successfully.
 */
export class TaskRemovalPerformed extends Event{

    /**
     * Creates `TaskRemovalPerformed` instance.
     *
     * @param taskId ID of the task, which removal was performed
     */
    constructor(taskId){
        super(EventTypes.TaskRemovalPerformed);
        this.taskId = taskId;
    }
}