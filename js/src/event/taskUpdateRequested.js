import {Event, EventTypes} from "./event";

/**
 * Occurs when end-user submitted changes of a task description.
 *
 * @extends Event
 * @author Oleg Barmin
 */
export class TaskUpdateRequested extends Event {

    /**
     * Creates `TaskUpdateRequested` instance.
     *
     * @param {TaskId} taskId ID of task which description needs to be updated
     * @param {string} newTaskDescription new description of task
     * @param {boolean} [status=false] status of tasks
     * @param {TodoListId} todoListId ID of `TodoList` which task update was requested
     */
    constructor(taskId, newTaskDescription, status = false, todoListId) {
        super(EventTypes.TaskUpdateRequested);
        this.taskId = taskId;
        this.status = status;
        this.newTaskDescription = newTaskDescription;
        this.todoListId = todoListId;
    }

}