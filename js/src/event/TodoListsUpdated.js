import {Event, EventTypes} from "./event";

/**
 * Occurs when to-do lists was updated.
 *
 * @extends Event
 * @author Oleg Barmin
 */
export class TodoListsUpdated extends Event {

    /**
     * Creates `TodoListsUpdated` instance
     *
     * @param {TodoListId[]} todoListIds IDs of to-do lists which was updated
     */
    constructor(todoListIds) {
        super(EventTypes.TodoListsUpdated);
        this.todoListIds = todoListIds;
    }
}