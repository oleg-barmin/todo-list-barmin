"use strict";

import {Preconditions} from "../lib/preconditions";

/**
 * Task to do.
 *
 * It stores task ID, its description, status (is it completed or not),
 * date of creation and date of last update (if task wasn't updated
 * the date of creation equals to last update date).
 */
export class Task {

    /**
     * Creates task instance.
     *
     * @param {TaskId} id task ID
     * @param {string} description description of the task
     * @param {Date} creationDate date when task was created
     *
     */
    constructor(id, description, creationDate) {
        this.id = id;
        this.description = description;
        this.completed = false;
        this.creationDate = creationDate;
        this.lastUpdateDate = creationDate;
    }

    toString() {
        return `Task: [ID = ${this.id}, description = ${this.description}, completed = ${this.completed}, `
            + `creation date = ${this.creationDate}, last update date = ${this.lastUpdateDate}]`
    }
}

/**
 * Used to identify tasks.
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
     * @param taskId `TaskId` to compare with
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