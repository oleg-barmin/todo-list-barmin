import {Event, EventTypes} from "./event";

/**
 * Occurs when controller adds new task to the model.
 *
 * @extends Event
 * @author Oleg Barmin
 */
export class NewTaskAdded extends Event {

    /**
     * Creates `NewTaskAdded` instance.
     *
     * @param {TodoListId} todoListId ID of to-do list to which task was added.
     */
    constructor(todoListId) {
        super(EventTypes.NewTaskAdded);
        this.todoListId = todoListId;
    }
}