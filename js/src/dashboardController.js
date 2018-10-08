import {TodoList} from "./model/todo-list";
import {EventTypes} from "./event/event";
import {NewTaskAdded} from "./event/newTaskAdded";
import {NewTaskValidationFailed} from "./event/newTaskValidationFailed";
import {TaskCompletionFailed} from "./event/taskCompletionFailed";
import {TaskRemovalFailed} from "./event/taskRemovalFailed";
import {TaskListUpdated} from "./event/taskListUpdated";
import {TaskUpdateFailed} from "./event/taskUpdateFailed";
import {TaskRemoved} from "./event/taskRemoved";
import {TaskUpdated} from "./event/taskUpdated";
import {Authentication} from "./authentication";

/**
 * Event-based facade for {@link TodoList}.
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
     */
    constructor(eventBus, authentication) {
        this.todoList = new TodoList();
        this.eventBus = eventBus;
        this.authentication = authentication;

        /**
         * Adds new task with description stored in occurred `AddTaskRequest` to `TodoList`.
         *
         * if given description is valid:
         *      - posts {@link NewTaskAdded}
         *      - posts {@link TaskListUpdated} with new task list.
         * Otherwise {@link NewTaskValidationFailed} will be posted
         *
         * @param {AddTaskRequest} addTaskEvent `AddTaskRequest` with description of the task to add.
         */
        const addTaskRequestCallback = addTaskEvent => {
            try {
                this.todoList.add(addTaskEvent.taskDescription);
                this.eventBus.post(new NewTaskAdded());
                this.eventBus.post(new TaskListUpdated(this.todoList.all()));
            } catch (e) {
                this.eventBus.post(new NewTaskValidationFailed(e.message));
            }
        };

        /**
         * Removes task with ID stored in occurred `TaskRemovalRequested` from `TodoList`.
         *
         * If task with given ID was found in `TodoList` posts {@link TaskListUpdated} with new task list.
         * Otherwise: posts {@link TaskRemovalFailed}.
         *
         * @param {TaskRemovalRequested} taskRemovalEvent `TaskRemovalRequested` event with ID of the task to remove.
         */
        const taskRemovalRequestCallback = taskRemovalEvent => {
            try {
                this.todoList.remove(taskRemovalEvent.taskId);
                this.eventBus.post(new TaskRemoved(taskRemovalEvent.taskId));
                this.eventBus.post(new TaskListUpdated(this.todoList.all()));
            } catch (e) {
                this.eventBus.post(new TaskRemovalFailed("Task removal fail."))
            }
        };

        /**
         * Completes task in `TodoList` by ID which stored in occurred `TaskCompletionRequested`.
         *
         * If task with given ID was found in `TodoList` posts {@link TaskListUpdated} with new task list.
         * Otherwise: posts {@link TaskCompletionFailed}.
         *
         * @param {TaskCompletionRequested} taskCompletionEvent `TaskCompletionRequested` event
         *        which contains ID of task to complete.
         */
        const taskCompletionRequestedCallback = taskCompletionEvent => {
            try {
                this.todoList.complete(taskCompletionEvent.taskId);
                this.eventBus.post(new TaskListUpdated(this.todoList.all()));
            } catch (e) {
                this.eventBus.post(new TaskCompletionFailed("Task completion fail."))
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
         *        which contains ID of task to update and its new description.
         */
        const taskUpdateRequestCallback = taskUpdateEvent => {
            try {
                this.todoList.update(taskUpdateEvent.taskId, taskUpdateEvent.newTaskDescription);
                this.eventBus.post(new TaskUpdated(taskUpdateEvent.taskId));
                this.eventBus.post(new TaskListUpdated(this.todoList.all()));
            } catch (e) {
                this.eventBus.post(new TaskUpdateFailed(taskUpdateEvent.taskId,
                    "New task description cannot be empty."))
            }
        };

        const addTaskHandler =
            eventBus.subscribe(EventTypes.AddTaskRequest, addTaskRequestCallback);
        const removeTaskHandler =
            eventBus.subscribe(EventTypes.TaskRemovalRequested, taskRemovalRequestCallback);
        const completeTaskHandler =
            eventBus.subscribe(EventTypes.TaskCompletionRequested, taskCompletionRequestedCallback);
        const updateTaskHandler =
            eventBus.subscribe(EventTypes.TaskUpdateRequested, taskUpdateRequestCallback);

        eventBus.subscribe(EventTypes.SignOutCompleted, () => {
            eventBus.unsubscribe(EventTypes.AddTaskRequest, addTaskHandler);
            eventBus.unsubscribe(EventTypes.TaskRemovalRequested, removeTaskHandler);
            eventBus.unsubscribe(EventTypes.TaskCompletionRequested, completeTaskHandler);
            eventBus.unsubscribe(EventTypes.TaskUpdateRequested, updateTaskHandler);
        })
    }

}