"use strict";
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