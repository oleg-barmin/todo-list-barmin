import {TodoList} from "./model/todo-list";
import {EventTypes} from "./event/event";
import {NewTaskAdded} from "./event/newTaskAdded";
import {NewTaskValidationFailed} from "./event/newTaskValidationFailed";
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
     * @param {Array} todoListIds ID of `TodoList`s to work with
     */
    constructor(eventBus, authentication, todoListIds) {
        this.eventBus = eventBus;
        this.authentication = authentication;
        this.todoLists = new Map();

        todoListIds.forEach(el => {
            this.todoLists.set(el.id, new TodoList(el, this.authentication.token));

            this.todoLists.get(el.id).all().then(tasks => {
                this.eventBus.post(new TaskListUpdated(tasks, el));
            }).catch(() => {
                alert("task list update failed.")
            });
        });

        /**
         * Sends requests to receive all tasks of this `TodoList`.
         *
         * If response code was 200: posts `TaskListUpdated` event with received tasks
         * of to-do list which tasks was updated.
         */
        const updateTaskList = (todoListId) => {
            this.todoLists.get(todoListId.id).all()
                .then(tasks => {
                    this.eventBus.post(new TaskListUpdated(tasks, todoListId));
                }).catch(() => {
                alert("task list update failed.")
            });
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
                        this.eventBus.post(new NewTaskAdded());
                        updateTaskList(taskAddRequested.todoListId);
                    })
                    .catch(() => alert("task adding failed."));
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
                this.todoLists.get(taskRemovalEvent.todoListId.id).remove(taskRemovalEvent.taskId)
                    .then(() => {
                        this.eventBus.post(new TaskRemoved(taskRemovalEvent.taskId));
                        updateTaskList(taskRemovalEvent.todoListId);
                    });
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
         *        which contains ID of task to update and its new description.
         */
        const taskUpdateRequestCallback = taskUpdateEvent => {
            try {
                this.todoLists.get(taskUpdateEvent.todoListId.id).update(taskUpdateEvent.taskId, taskUpdateEvent.newTaskDescription, taskUpdateEvent.status)
                    .then(() => {
                        if (!taskUpdateEvent.status) {
                            this.eventBus.post(new TaskUpdated(taskUpdateEvent.taskId));
                        }
                        updateTaskList(taskUpdateEvent.todoListId);
                    });
            } catch (e) {
                this.eventBus.post(new TaskUpdateFailed(taskUpdateEvent.taskId,
                    "New task description cannot be empty."))
            }
        };

        const addTaskHandler =
            eventBus.subscribe(EventTypes.TaskAddRequest, addTaskRequestCallback);
        const removeTaskHandler =
            eventBus.subscribe(EventTypes.TaskRemovalRequested, taskRemovalRequestCallback);
        const updateTaskHandler =
            eventBus.subscribe(EventTypes.TaskUpdateRequested, taskUpdateRequestCallback);

        eventBus.subscribe(EventTypes.SignOutCompleted, () => {
            eventBus.unsubscribe(EventTypes.TaskAddRequest, addTaskHandler);
            eventBus.unsubscribe(EventTypes.TaskRemovalRequested, removeTaskHandler);
            eventBus.unsubscribe(EventTypes.TaskUpdateRequested, updateTaskHandler);
        })
    }

}