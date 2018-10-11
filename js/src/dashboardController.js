import {TodoList} from "./todo-list";
import {EventTypes} from "./event/event";
import {NewTaskAdded} from "./event/newTaskAdded";
import {NewTaskValidationFailed} from "./event/newTaskValidationFailed";
import {TaskRemovalFailed} from "./event/taskRemovalFailed";
import {TaskListUpdated} from "./event/taskListUpdated";
import {TaskUpdateFailed} from "./event/taskUpdateFailed";
import {TaskRemoved} from "./event/taskRemoved";
import {TaskUpdated} from "./event/taskUpdated";
import {Authentication} from "./authentication";
import {Backend} from "./backend";

/**
 * Event-based facade for {@link TodoList}.
 *
 * @author Oleg Barmin
 */
export class DashboardController {

    /**
     * Creates `DashboardController` instance.
     *
     * During construction of instance it creates new `TodoList` instance,
     * where it will contains all tasks, and EventBus to process occurred events.
     *
     * @param {EventBus} eventBus evenBus to work with
     * @param {Authentication} authentication to authorized operations
     * @param {Backend} backend to send request to server
     */
    constructor(eventBus, authentication, backend) {
        this.eventBus = eventBus;
        this.authentication = authentication;
        this.backend = backend;
        this.todoLists = new Map();

        /**
         * Sends requests to receive all tasks of this `TodoList`.
         *
         * If response code was 200: posts `TaskListUpdated` event with received tasks
         * of to-do list which tasks was updated.
         *
         * @param {TodoListId} todoListId ID of to-do list which tasks must be updated
         */
        const updateTaskList = (todoListId) => {
            this.todoLists.get(todoListId.id).all()
                .then(tasks => this.eventBus.post(new TaskListUpdated(tasks, todoListId)))
                .catch(() => alert("Failed to updateTask tasks list, try to reload page."));
        };

        /**
         * Adds new task with description stored in occurred `TaskAddRequested` to `TodoList`.
         *
         * if given description is valid:
         *      - posts {@link NewTaskAdded}
         *      - posts {@link TaskListUpdated} with new task list.
         * Otherwise {@link NewTaskValidationFailed} will be posted
         *
         * @param {TaskAddRequested} taskAddRequested `TaskAddRequested` with description of the task to add.
         */
        const addTaskRequestCallback = taskAddRequested => {
            try {
                this.todoLists.get(taskAddRequested.todoListId.id).add(taskAddRequested.taskDescription)
                    .then(() => {
                        this.eventBus.post(new NewTaskAdded(taskAddRequested.todoListId));
                        updateTaskList(taskAddRequested.todoListId);
                    }).catch(() => alert("Failed to add task, try to reload page."));
            } catch (e) {
                this.eventBus.post(new NewTaskValidationFailed(e.message, taskAddRequested.todoListId));
            }
        };

        /**
         * Removes task with ID stored in occurred `TaskRemovalRequested` from `TodoList`.
         *
         * If task with given ID was found in `TodoList` posts {@link TaskListUpdated} with new task list.
         * Otherwise: posts {@link TaskRemovalFailed}.
         *
         * @param {TaskRemovalRequested} taskRemovalEvent `TaskRemovalRequested` event with ID of the task to removeTask.
         */
        const taskRemovalRequestCallback = taskRemovalEvent => {
            try {
                this.todoLists.get(taskRemovalEvent.todoListId.id).remove(taskRemovalEvent.taskId)
                    .then(() => {
                        this.eventBus.post(new TaskRemoved(taskRemovalEvent.taskId));
                        updateTaskList(taskRemovalEvent.todoListId);
                    }).catch(() => alert("Failed to removeTask task, try to reload page."));
            } catch (e) {
                this.eventBus.post(new TaskRemovalFailed("Task removal fail."))
            }
        };

        /**
         * Updates task in `TodoList` by ID with new description.
         * Both ID and new description are stored in occurred `TaskCompletionRequested` event.
         *
         * If task with given ID was found in `TodoList`:
         *      - posts {@link TaskUpdated} with ID of updated task.
         *      - posts {@link TaskListUpdated} with new task list.
         * Otherwise: posts {@link TaskUpdateFailed}.
         *
         * @param {TaskUpdateRequested} taskUpdateEvent `TaskUpdateRequested` event
         *        which contains ID of task to updateTask and its new description.
         */
        const taskUpdateRequestCallback = taskUpdateEvent => {
            try {
                this.todoLists.get(taskUpdateEvent.todoListId.id)
                    .update(taskUpdateEvent.taskId, taskUpdateEvent.newTaskDescription, taskUpdateEvent.status)
                    .then(() => {
                        if (!taskUpdateEvent.status) {
                            this.eventBus.post(new TaskUpdated(taskUpdateEvent.taskId));
                        }
                        updateTaskList(taskUpdateEvent.todoListId);
                    }).catch(() => alert("Failed to updateTask task, try to reload page."));
            } catch (e) {
                this.eventBus.post(new TaskUpdateFailed(taskUpdateEvent.taskId,
                    "New task description cannot be empty."))
            }
        };

        /**
         * Updates to-do lists and posts `TaskListUpdated` event for each to-do list to update their tasks.
         *
         * @param {TodoListsUpdated} event event which occurred
         */
        const todoListsUpdated = event => {
            event.todoListIds.forEach(el => {
                this.todoLists.set(el.id, new TodoList(el, this.authentication.token, this.backend));
                this.todoLists.get(el.id).all()
                    .then(tasks => this.eventBus.post(new TaskListUpdated(tasks, el)))
                    .catch(() => alert("to-do list updateTask failed."));
            });
        };

        const addTaskHandler =
            eventBus.subscribe(EventTypes.TaskAddRequest, addTaskRequestCallback);
        const removeTaskHandler =
            eventBus.subscribe(EventTypes.TaskRemovalRequested, taskRemovalRequestCallback);
        const updateTaskHandler =
            eventBus.subscribe(EventTypes.TaskUpdateRequested, taskUpdateRequestCallback);
        const todoListUpdatedCallback =
            eventBus.subscribe(EventTypes.TodoListsUpdated, todoListsUpdated);

        eventBus.subscribe(EventTypes.SignOutCompleted, () => {
            eventBus.unsubscribe(EventTypes.TaskAddRequest, addTaskHandler);
            eventBus.unsubscribe(EventTypes.TaskRemovalRequested, removeTaskHandler);
            eventBus.unsubscribe(EventTypes.TaskUpdateRequested, updateTaskHandler);
            eventBus.unsubscribe(EventTypes.TodoListsUpdated, todoListUpdatedCallback);
        })
    }

}