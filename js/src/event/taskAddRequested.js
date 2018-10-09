import {Event, EventTypes} from "./event";

/**
 * Occurred when new task was added on view.
 *
 * @extends Event
 * @author Oleg Barmin
 */
export class TaskAddRequested extends Event {

    /**
     * Creates `TaskAddRequested` instance.
     *
     * @param {string} taskDescription description of new task
     * @param {TodoListId} todoListId ID of `TodoList` to which task was added
     */
    constructor(taskDescription, todoListId) {
        super(EventTypes.TaskAddRequest);
        this.todoListId = todoListId;
        this.taskDescription = taskDescription;
    }
}