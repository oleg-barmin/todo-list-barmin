import {Event, EventTypes} from "./event";

/**
 * Occurs when controller updated list of tasks.
 *
 * @extends Event
 * @author Oleg Barmin
 */
export class TaskListUpdated extends Event {

    /**
     * Creates `TaskListUpdated` instance.
     *
     * @param {Array} taskArray sorted array of task from model.
     * @param {TodoListId } todoListId ID of `TodoList` which task was updated
     */
    constructor(taskArray, todoListId) {
        super(EventTypes.TaskListUpdated);
        this.taskArray = taskArray;
        this.todoListId = todoListId;
    }
}