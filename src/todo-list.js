/**
 * Stores information about task.
 *
 * It stores task ID, its content, status (is it completed or not),
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
    constructor(id, content, completed = false, creationDate, lastUpdateDate = creationDate) {
        this.ID = Preconditions.isDefined(id, "ID");
        this.content = Preconditions.isDefined(content, "task content");
        this.completed = completed;
        this.creationDate = Preconditions.isDefined(creationDate, "date of creation");
        this.lastUpdateDate = lastUpdateDate;
    }
}

/**
 * Static methods that help a method or constructor check whether it was invoked
 * with correct parameters.
 */
class Preconditions {

    /**
     * Validates whether given argument is not null or undefined.
     *
     * @param {*} value the value of parameter being checked.
     * @param {string} parameterName name of the parameter.
     * @throws ParameterIsNotDefinedError if given parameter is null or undefined.
     * @returns {*} value of the parameter if it is not null or undefined otherwise ParameterIsNotDefinedError is thrown.
     */
    static isDefined(value, parameterName) {
        if (!value) {
            throw new ParameterIsNotDefinedError(value, parameterName);
        }
        return value;
    }
}

/**
 * Custom error type which indicates that a null or undefined variable was found, when it shouldn't.
 *
 * @extends Error
 */
class ParameterIsNotDefinedError extends Error {

    /**
     * Creates ParameterIsNotDefinedError instance.
     *
     * @param value parameters value.
     * @param parameterName name of the parameter that was checked.
     */
    constructor(value, parameterName) {
        super(`Parameter ${parameterName} should be not null and not undefined, Actual value: '${value}'`);
        this.name = this.constructor.name;
    }
}
