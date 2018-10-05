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
import {SignInFailed} from "./event/signInFailed";
import {SignInCompleted} from "./event/signInCompleted";

/**
 * Event-based facade for {@link TodoList}.
 */
export class Controller {

    /**
     * Creates `Controller` instance.
     *
     * During construction of instance it creates new `TodoList` instance,
     * where it will contains all tasks, and EventBus to process occurred events.
     *
     * @param {EventBus} eventBus evenBus to work with
     */
    constructor(eventBus) {
        this.todoList = new TodoList();
        this.eventBus = eventBus;
        this.authentication = new Authentication();

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
                this.eventBus.post(new TaskUpdateFailed(taskUpdateEvent.taskId, "New task description cannot be empty."))
            }
        };

        /**
         * Posts `SignInCompleted` event if user was successfully authenticated,
         * otherwise posts `SignInFailed` event.
         *
         * @param {CredentialsSubmitted} credentialsSubmittedEvent event
         *         which contains username and password typed in by user
         */
        const credentialsSubmittedRequestCallback = credentialsSubmittedEvent => {
            const username = credentialsSubmittedEvent.username;
            const password = credentialsSubmittedEvent.password;

            this.authentication
                .signIn(username, password)
                .then(() => {
                    this.eventBus.post(new SignInCompleted())
                })
                .catch(() => {
                    this.eventBus.post(new SignInFailed())
                });
        };

        eventBus.subscribe(EventTypes.AddTaskRequest, addTaskRequestCallback);
        eventBus.subscribe(EventTypes.TaskRemovalRequested, taskRemovalRequestCallback);
        eventBus.subscribe(EventTypes.TaskCompletionRequested, taskCompletionRequestedCallback);
        eventBus.subscribe(EventTypes.TaskUpdateRequested, taskUpdateRequestCallback);
        eventBus.subscribe(EventTypes.CredentialsSubmitted, credentialsSubmittedRequestCallback)
    }

}