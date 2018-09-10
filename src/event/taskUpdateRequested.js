import {Event, EventTypes} from "./event";

/**
 * Occurs when end-user submitted changes of a task description.
 *
 * @extends Event
 */
export class TaskUpdateRequested extends Event{

    /**
     * Creates `TaskUpdateRequested` instance.
     *
     * @param {TaskId} taskId ID of task which description needs to be updated
     * @param {string} newTaskDescription new description of task.
     */
    constructor(taskId, newTaskDescription) {
        super(EventTypes.TaskUpdateRequested);
        this.taskId = taskId;
        this.newTaskDescription = newTaskDescription;
    }

}