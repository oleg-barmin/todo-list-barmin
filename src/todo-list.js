/**
 * Stores information about task.
 *
 * <p>It stores task ID, its content, status (is it completed or not),
 * date of creation and date of last update (if task wasn't updated
 * the date of creation equals to last update date).
 */
class Task {
    /**
     * Creates task instance.
     *
     * @param {string} id task id.
     * @param {string} content content of the task.
     * @param {boolean} completed task status (is it completed or not).
     * @param {Date} creationDate date when task was created.
     * @param {Date} lastUpdateDate date when task was last updated (corresponds to
     * {@param creationDate} if task was not updated.
     */
    constructor(id, content, completed, creationDate, lastUpdateDate) {
        this.ID = id;
        this.content = content;
        this.completed = completed;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate || creationDate;
    }
}

