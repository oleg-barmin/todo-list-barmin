/**
 * Static methods that help a method or constructor check whether it was invoked
 * with correct parameters.
 *
 * @author Oleg Barmin
 */
export class Preconditions {

    /**
     * Validates whether given argument is not null or undefined.
     *
     * @param {*} value the value of parameter being checked.
     * @param {string} parameterName name of the parameter.
     *
     * @throws ParameterIsNotDefinedException if given parameter is null or undefined.
     *
     * @returns {*} value of the parameter if it is not null or undefined otherwise ParameterIsNotDefinedException is thrown.
     */
    static isDefined(value, parameterName) {
        if (!value) {
            throw new ParameterIsNotDefinedException(value, parameterName);
        }
        return value;
    }

    /**
     * Validates if given string is not undefined, null or empty.
     *
     * @param {string} stringToCheck string that should be checked.
     * @param {string} stringName name of string being checked
     *
     * @throws EmptyStringException if given string is empty or undefined.
     *
     * @returns {string} string value if given string is not
     */
    static checkStringNotEmpty(stringToCheck, stringName) {
        if (!(stringToCheck && typeof stringToCheck === "string")) {
            throw new EmptyStringException(stringToCheck, stringName)
        }
        stringToCheck = stringToCheck.trim();
        if (stringToCheck === "") {
            throw new EmptyStringException(stringToCheck, stringName)
        }
        return stringToCheck;
    }
}

/**
 * Indicates that a null or undefined argument was found while it wasn't expected.
 *
 * @extends Error
 * @author Oleg Barmin
 */
export class ParameterIsNotDefinedException extends Error {

    /**
     * Creates `ParameterIsNotDefinedException` instance.
     *
     * @param {*} value parameters value
     * @param {string} parameterName name of the parameter that was checked
     */
    constructor(value, parameterName) {
        super(`Parameter '${parameterName}' should be not null and not undefined, Actual value: '${value}'`);
        this.name = this.constructor.name;
    }
}

/**
 * Indicates that given task description is undefined, null or empty.
 *
 * @author Oleg Barmin
 */
export class EmptyStringException extends Error {

    /**
     * Creates `EmptyStringException` instance.
     *
     * @param {string} value actual value of task description
     * @param {string} stringName name of the string that was checked
     */
    constructor(value, stringName) {
        super(`String '${stringName}' should be a string and cannot be undefined, null or empty. Actual value: '${value}'`);
        this.name = this.constructor.name;
        if (value === "") {
            this.message = "Task description cannot be empty."
        }
    }
}