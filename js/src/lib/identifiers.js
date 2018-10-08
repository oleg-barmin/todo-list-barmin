import {Preconditions} from "./preconditions";

/**
 * Used to identify tasks.
 *
 * @author Oleg Barmin
 */
export class TaskId {

    /**
     * Creates `TaskId` instance.
     *
     * @param id ID to store
     */
    constructor(id) {
        Preconditions.checkStringNotEmpty(id, "ID");
        this.id = id;
    }

    /**
     * Compares `TaskId` objects by stored ID.
     *
     * @param {TaskId} taskId `TaskId` to compare with
     *
     * @throws TypeError if given `taskId` is not TaskId class instance
     *
     * @returns {number} result positive, negative or 0 if given task is less, greater or equal to current.
     */
    compareTo(taskId) {
        if (!(taskId instanceof TaskId)) {
            throw new TypeError("Object of TaskId was expected");
        }

        return this.id.localeCompare(taskId.id);
    }
}

/**
 * Used to identify `TodoList`s.
 *
 * @author Oleg Barmin
 */
export class TodoListId {

    /**
     * Creates `TodoListId` instance.
     *
     * @param id ID to store
     */
    constructor(id) {
        Preconditions.checkStringNotEmpty(id, "ID");
        this.id = id;
    }

    /**
     * Compares `TodoListId` objects by stored ID.
     *
     * @param {TodoListId} todoListId `TodoListId` to compare with
     *
     * @throws TypeError if given `todoListId` is not TodoListId class instance
     *
     * @returns {number} result positive, negative or 0 if given task is less, greater or equal to current.
     */
    compareTo(todoListId) {
        if (!(todoListId instanceof TodoListId)) {
            throw new TypeError("Object of TodoListId was expected");
        }

        return this.id.localeCompare(todoListId.id);
    }
}