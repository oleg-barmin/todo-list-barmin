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
import {TodoListIdGenerator} from "./lib/todoListIdGenerator";

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
     * @param {UserLists} userLists service to work with user lists.
     */
    constructor(eventBus, authentication, userLists) {
        this.userLists = userLists;
        this.eventBus = eventBus;
        this.authentication = authentication;

        /**
         * Creates initial to-do list for new user.
         */
        const createInitialsTodoList = () => {
            const todoList = new TodoList(TodoListIdGenerator.generateID(), authentication.token);
            this.userLists.create(todoList.todoListId)
                .then(() => {
                    this.todoList = todoList;
                });
        };

        this.userLists.readLists()
            .then(todoListsIds => {
                    if (todoListsIds.length === 0) {
                        // if user has no lists - create one
                        createInitialsTodoList();
                    }
                    else {
                        this.todoList = new TodoList(todoListsIds[0], authentication.token);
                    }
                }
            );

        /**
         * Sends requests to receive all tasks of this `TodoList`.
         *
         * If response code was 200: posts `TaskListUpdated` event with received tasks,
         * otherwise:
         */
        const updateTaskList = () => {
            this.todoList.all()
                .then(tasks => {
                    this.eventBus.post(new TaskListUpdated(tasks));
                })
                .catch(() => {
                    alert("task list update failed.")
                });
        };

        /**
         * Adds new task with description stored in occurred `TaskAddRequest` to `TodoList`.
         *
         * if given description is valid:
         *      - posts {@link NewTaskAdded}
         *      - posts {@link TaskListUpdated} with new task list.
         * Otherwise {@link NewTaskValidationFailed} will be posted
         *
         * @param {TaskAddRequest} addTaskEvent `TaskAddRequest` with description of the task to add.
         */
        const addTaskRequestCallback = addTaskEvent => {
            try {
                this.todoList.add(addTaskEvent.taskDescription)
                    .then(() => {
                        this.eventBus.post(new NewTaskAdded());
                        updateTaskList();
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
                this.todoList.remove(taskRemovalEvent.taskId)
                    .then(() => {
                        this.eventBus.post(new TaskRemoved(taskRemovalEvent.taskId));
                        updateTaskList();
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
                this.todoList.update(taskUpdateEvent.taskId, taskUpdateEvent.newTaskDescription, taskUpdateEvent.status)
                    .then(() => {
                        if (!taskUpdateEvent.status) {
                            this.eventBus.post(new TaskUpdated(taskUpdateEvent.taskId));
                        }
                        updateTaskList();
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