(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory() :
        typeof define === 'function' && define.amd ? define(factory) :
            (factory());
}(this, (function () {
    'use strict';

    /**
     * Static methods that help a method or constructor check whether it was invoked
     * with correct parameters.
     *
     * @author Oleg Barmin
     */
    class Preconditions {

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
    class ParameterIsNotDefinedException extends Error {

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
     * @extends Error
     * @author Oleg Barmin
     */
    class EmptyStringException extends Error {

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
                this.message = "Task description cannot be empty.";
            }
        }
    }

    /**
     * Used to identify tasks.
     *
     * @author Oleg Barmin
     */
    class TaskId {

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
     * Generates unique `TaskId` for `Task`.
     *
     * Current implementation based on uuid v4.
     *
     * @author Oleg Barmin
     */
    class TaskIdGenerator {

        /**
         * Generates unique `TaskID`.
         *
         * @returns {TaskId} ID generated.
         */
        static generateID() {
            if (typeof(require) !== 'undefined') {
                return require('uuid/v4')();
            }
            const rawId = uuidv4();
            return new TaskId(rawId);
        }
    }

    /**
     * Stores algorithm to sort an array of  `Task`.
     *
     * @author Oleg Barmin
     */
    class TaskSorter {

        /**
         * Sorts array of tasks by:
         * - Status (uncompleted then completed).
         * - Date of last updateTask.
         * - Lexicographically by description.
         * - Lexicographically by ID.
         *
         * @param {Array} array with tasks to sort
         */
        static sortTasksArray(array) {
            return array.sort((firstTask, secondTask) => {
                if (firstTask.completed === secondTask.completed) {
                    const compareByDateResult = secondTask.lastUpdateDate - firstTask.lastUpdateDate;
                    if (compareByDateResult !== 0) {
                        return compareByDateResult;
                    }
                    const compareByDescriptionResult = firstTask.description.localeCompare(secondTask.description);
                    if (compareByDescriptionResult !== 0) {
                        return compareByDescriptionResult;
                    }
                    return secondTask.id.compareTo(firstTask.id);
                }

                return firstTask.completed ? 1 : -1;
            });
        }
    }

    /**
     * Task to do.
     *
     * It stores task ID, its description, status (is it completed or not),
     * date of creation and date of last update (if task wasn't updated
     * the date of creation equals to last update date).
     *
     * @author Oleg Barmin
     */
    class Task {

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

    QUnit.module("Preconditions should");
    QUnit.test("throw ", assert => {
        assert.throws(
            () => Preconditions.isDefined(undefined, "any parameter"),
            new ParameterIsNotDefinedException(undefined, "any parameter"),
            "ParameterIsNotDefinedException when isDefined() argument is undefined."
        );

        assert.throws(
            () => Preconditions.isDefined(null, "any parameter"),
            new ParameterIsNotDefinedException(null, "any parameter"),
            "ParameterIsNotDefinedException when isDefined() argument is null."
        );

        assert.throws(
            () => Preconditions.checkStringNotEmpty(undefined, "any string"),
            new EmptyStringException(undefined, "any string"),
            "EmptyStringException when checkStringNotEmpty() argument is undefined."
        );

        assert.throws(
            () => Preconditions.checkStringNotEmpty(null, "any string"),
            new EmptyStringException(null, "any string"),
            "EmptyStringException when checkStringNotEmpty() argument is null."
        );

        assert.throws(
            () => Preconditions.checkStringNotEmpty("", "any string"),
            new EmptyStringException("", "any string"),
            "EmptyStringException when checkStringNotEmpty() argument is empty."
        );

        assert.throws(
            () => Preconditions.checkStringNotEmpty("    ", "any string"),
            new EmptyStringException("", "any string"),
            "EmptyStringException when checkStringNotEmpty() argument contains only of multiple spaces."
        );
    });

    QUnit.module("TasksSorter should ");
    QUnit.test("sort ", assert => {
        const firstDate = new Date("01 Jan 1970 00:00:00 GMT");
        const secondDate = new Date("02 Jan 1970 00:00:00 GMT");
        const thirdDate = new Date("03 Jan 1970 00:00:00 GMT");
        const fourthDate = new Date("04 Jan 1970 00:00:00 GMT");

        let firstTask = new Task(new TaskId("1"), "1", firstDate);
        let secondTask = new Task(new TaskId("2"), "2", secondDate);
        let thirdTask = new Task(new TaskId("3"), "3", thirdDate);
        let fourthTask = new Task(new TaskId("4"), "4", fourthDate);

        let array = [
            firstTask,
            fourthTask,
            secondTask,
            thirdTask
        ];

        TaskSorter.sortTasksArray(array);

        assert.propEqual(
            array,
            [
                fourthTask,
                thirdTask,
                secondTask,
                firstTask
            ],
            " by date."
        );

        const sameDate = new Date();

        firstTask.lastUpdateDate = sameDate;
        secondTask.lastUpdateDate = sameDate;
        thirdTask.lastUpdateDate = sameDate;
        fourthTask.lastUpdateDate = sameDate;

        array = [
            firstTask,
            fourthTask,
            thirdTask,
            secondTask
        ];

        TaskSorter.sortTasksArray(array);

        assert.propEqual(
            array,
            [
                firstTask,
                secondTask,
                thirdTask,
                fourthTask
            ],
            " by description if dates are same."
        );

        const sameDescription = "same description";
        firstTask.description = sameDescription;
        secondTask.description = sameDescription;
        thirdTask.description = sameDescription;
        fourthTask.description = sameDescription;


        assert.propEqual(
            array,
            [
                firstTask,
                secondTask,
                thirdTask,
                fourthTask
            ],
            " by ID if dates and descriptions are same."
        );
    });

    QUnit.module("TaskIdGenerator should");
    QUnit.test("generate ", assert => {
        let set = new Set();

        let hasDuplicate = false;
        for (let i = 0; i < 1000; i++) {
            let id = TaskIdGenerator.generateID();
            if (set.has(id)) {
                hasDuplicate = true;
                break;
            }
            set.add(id);
        }
        assert.ok(!hasDuplicate, " unique id.");
    });

})));
