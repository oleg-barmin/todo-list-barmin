import {Event, EventTypeEnumeration} from "./event";

/**
 * Occurs when end user submitted changes of a task description.
 */
export class TaskUpdateRequest extends Event{

    /**
     * Creates `TaskUpdateRequest` instance.
     *
     * @param {TaskId} taskId ID of task which description needs to be updated
     * @param {string} newTaskDescription new description of task.
     */
    constructor(taskId, newTaskDescription) {
        super(EventTypeEnumeration.TaskUpdateRequest);
        this.taskId = taskId;
        this.newTaskDescription = newTaskDescription;
    }

}