"use strict";

/**
 * Task to do.
 *
 * It stores task ID, its description, status (is it completed or not),
 * date of creation and date of last update (if task wasn't updated
 * the date of creation equals to last update date).
 *
 * @author Oleg Barmin
 */
export class Task {

    /**
     * Creates task instance.
     *
     * @param {TaskId} id task ID
     * @param {string} description description of the task
     * @param {Date} creationDate date when task was created
     *
     * @param {boolean} [completed=false] status of task (completed or not)
     * @param {Date} [lastUpdate=creationDate] date of last task update
     */
    constructor(id, description, creationDate, completed = false, lastUpdate = creationDate) {
        this.id = id;
        this.description = description;
        this.completed = completed;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdate;
    }

    toString() {
        return `Task: [ID = ${this.id}, description = ${this.description}, completed = ${this.completed}, `
            + `creation date = ${this.creationDate}, last update date = ${this.lastUpdateDate}]`
    }
}