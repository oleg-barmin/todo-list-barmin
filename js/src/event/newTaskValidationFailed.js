import {Event, EventTypes} from "./event";


/**
 * Occurs when validation of description of new task in `NewTaskValidationFailed` was failed.
 *
 * @extends Event
 * @author Oleg Barmin
 */
export class NewTaskValidationFailed extends Event{

    /**
     * Creates `NewTaskValidationFailed` instance.
     *
     * @param {string} errorMsg description of error
     * @param {TodoListId} todoListId ID of to-do list
     */
    constructor(errorMsg, todoListId) {
        super(EventTypes.NewTaskValidationFailed);
        this.errorMsg = errorMsg;
        this.todoListId = todoListId;
    }
}