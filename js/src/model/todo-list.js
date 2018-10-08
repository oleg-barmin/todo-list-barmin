import {EmptyStringException, Preconditions} from "../lib/preconditions.js";
import {Task} from "./task.js";
import {TaskIdGenerator} from "../lib/taskIdGenerator.js";
import {TasksClone} from "../lib/todolists.js";

/**
 * Tasks to-do.
 *
 * Allows:
 *  - add new tasks
 *  - marks them as completed
 *  - update their descriptions
 *  - remove from list
 *
 * Task is being sorted by:
 * - Status (uncompleted tasks first, completed last).
 * - Date of last update.
 * - Lexicographically by description.
 *
 * Example:
 * ```
 *  const todoList = new TodoList();
 *  const firstTaskId = todoList.add("first task");
 *  const secondTaskId = todoList.add("second task");
 *  const thirdTaskId = todoList.add("third task");
 * ```
 * Task list will be sorted by creation date:
 * - third task
 * - second task
 * - first task
 *
 * Then lets complete third task.
 * ```
 * todoList.complete(thirdTaskId);
 * ```
 *
 * Now list will look this way:
 * - second task
 * - first task
 * - ~~third task~~
 *
 * if you update the first task, it will move to the top of the list.
 * ```
 * todoList.update(firstTaskId, "updated first task");
 * ```
 *
 * - <b>updated first task</b>
 * - second task
 * - ~~third task~~
 */
export class TodoList {

    /**
     * Creates `TodoList` instance.
     *
     * @param {TodoListId} todoListId ID of to-do list
     */
    constructor(todoListId) {
        this._tasksArray = [];
        this.todoListId = todoListId;
    }

    /**
     * Adds new task to `TodoList`.
     *
     * @param {string} taskDescription description of the task to add
     *
     * @throws EmptyStringException if given task description is not defined or empty
     *
     * @returns {TaskId} id copy of ID of task that was added
     */
    add(taskDescription) {
        taskDescription = Preconditions.checkStringNotEmpty(taskDescription, "task description");

        const taskId = TaskIdGenerator.generateID();
        const currentDate = new Date();
        const taskToAdd = new Task(taskId, taskDescription, currentDate);
        this._tasksArray.push(taskToAdd);

        TaskSorter.sortTasksArray(this._tasksArray);

        return TasksClone.cloneObject(taskId);
    }

    /**
     * Finds task by its ID.
     *
     * @param {TaskId} taskId ID of specified task
     * @returns {Task} task task with specified ID
     * @private
     */
    _getTaskById(taskId) {
        for (let cur of this._tasksArray) {
            if (cur.id.compareTo(taskId) === 0) {
                return cur;
            }
        }
    }

    /**
     * Finds task by ID and marks it as completed.
     *
     * @param {TaskId} taskId ID of specified task
     *
     * @throws TaskNotFoundException if task with specified ID was not found
     * @throws TaskAlreadyCompletedException if task with specified ID is already completed
     */
    complete(taskId) {
        Preconditions.isDefined(taskId, "task ID");

        const storedTask = this._getTaskById(taskId);

        if (!storedTask) {
            throw new TaskNotFoundException(taskId);
        }

        if (storedTask.completed) {
            throw new TaskAlreadyCompletedException(taskId);
        }
        storedTask.completed = true;
        storedTask.lastUpdateDate = new Date();
        TaskSorter.sortTasksArray(this._tasksArray);
    }

    /**
     * Finds task by ID and updates its description.
     *
     * @param {TaskId} taskId ID of the specified task
     * @param {string} updatedDescription new description of the task
     *
     * @throws TaskNotFoundException if task with specified ID was not found
     * @throws CannotUpdateCompletedTaskException if try to updated completed task
     */
    update(taskId, updatedDescription) {
        Preconditions.isDefined(taskId, "task ID");
        updatedDescription = Preconditions.checkStringNotEmpty(updatedDescription, "updated description");

        const taskToUpdate = this._getTaskById(taskId);

        if (!taskToUpdate) {
            throw new TaskNotFoundException(taskId);
        }

        if (taskToUpdate.completed) {
            throw new CannotUpdateCompletedTaskException(taskId);
        }

        taskToUpdate.description = updatedDescription;
        taskToUpdate.lastUpdateDate = new Date();

        TaskSorter.sortTasksArray(this._tasksArray);
    }

    /**
     * Removes task with given ID from to-do list.
     *
     * @param {TaskId} taskId ID of task to delete
     *
     * @throws TaskNotFoundException if task with specified ID was not found
     */
    remove(taskId) {
        Preconditions.isDefined(taskId, "task ID");

        const desiredTask = this._tasksArray.find((element, index, array) => {
            if (element.id.compareTo(taskId) === 0) {
                array.splice(index, 1);
                return true;
            }
        });

        if (!desiredTask) {
            throw new TaskNotFoundException(taskId);
        }
    }

    /**
     * Returns copy of all tasks stored in to-do list.
     *
     * @returns {Array} tasksArray copy of array with task
     */
    all() {
        return TasksClone.cloneArray(this._tasksArray);
    }
}

/**
 * Stores algorithm to sort an array of  `Task`.
 */
export class TaskSorter {

    /**
     * Sorts array of tasks by:
     * - Status (uncompleted then completed).
     * - Date of last update.
     * - Lexicographically by description.
     * - Lexicographically by ID.
     *
     * @param {Array} array with tasks to sort
     */
    static sortTasksArray(array) {
        array.sort((firstTask, secondTask) => {
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
 * Indicates that task was not found.
 *
 * @extends Error
 */
export class TaskNotFoundException extends Error {

    /**
     * Crates `TaskNotFoundException` instance.
     *
     * @param {TaskId} taskId ID if the task that was not found
     */
    constructor(taskId) {
        super(`Task with ID ${taskId.id} was not found.`);
        this.name = this.constructor.name;
    }
}


/**
 * Indicates that task was already completed.
 *
 * @extends Error
 */
export class TaskAlreadyCompletedException extends Error {

    /**
     * Crates `TaskAlreadyCompletedException` instance.
     *
     * @param {TaskId} taskId  ID of task which was already completed.
     */
    constructor(taskId) {
        super(`Task with ID ${taskId.id} is alredy completed.`);
        this.name = this.constructor.name;
    }
}

/**
 * Occurs when trying to update completed task.
 *
 * @extends Error
 */
export class CannotUpdateCompletedTaskException extends Error {

    /**
     * Crates `CannotUpdateCompletedTaskException` instance.
     *
     * @param {TaskId} taskId ID of task which was already completed before update attempt.
     */
    constructor(taskId) {
        super(`Completed tasks cannot be updated. Task with ID: ${taskId} is completed.`);
        this.name = this.constructor.name;
    }
}