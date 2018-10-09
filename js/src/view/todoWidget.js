import {UiComponent} from "./uiComponent";
import {EventTypes} from "../event/event";
import {TaskView} from "./taskView";

/**
 * Renders list of tasks.
 *
 * When {@link NewTaskAdded} happens gets new tasks,
 * removes previous task list and renders new tasks from `NewTaskAdded`.
 *
 * @extends UiComponent
 * @author Oleg Barmin
 */
export class TodoWidget extends UiComponent {

    /**
     * Creates `TodoWidget` instance.
     *
     * @param {jQuery} element JQuery element where all task should be appended
     * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
     * @param {TodoListId} todoListId ID of to-do list to which `TodoWidget` is related to
     */
    constructor(element, eventBus, todoListId) {
        super(element, eventBus);
        this.todoListId = todoListId;
        this.taskViewArray = [];
    }

    /**
     * Merges given `taskViewArray` and `taskArray`.
     *
     * If `taskViewArray` doesn't contain a `TaskView` with task from `taskArray` method creates new `TaskView`.
     *
     * @param {Array} taskViewArray array of `TaskView` array to merge with tasks
     * @param {Array} taskArray array of task to merge with
     * @return {Array} taskViewArray array of `TaskView`s in order of `taskArray`
     *
     * @private
     */
    _merge(taskViewArray, taskArray) {
        return taskArray.map((task, index) => {

            const taskContainer = this.element.append(`<div class="row no-gutters mt-2"></div>`).children().last();
            const taskViewWithCurrentTask = taskViewArray.find(element => element.task.id.compareTo(task.id) === 0);

            if (taskViewWithCurrentTask) {
                taskViewWithCurrentTask.update(taskContainer, ++index, task);
                return taskViewWithCurrentTask;
            }

            return new TaskView(taskContainer, this.eventBus, ++index, task, this.todoListId);
        });
    }

    /**
     * Empties given container for tasks to populate it with new tasks.
     */
    render() {
        this.element.empty();

        /**
         * Processes `TaskListUpdated` event.
         * Populates container of tasks with `TaskView` for each task stored in occurred `TaskListUpdated` event.
         *
         * @param {TaskListUpdated} taskListUpdatedEvent occurred `TaskListUpdated` event with array of new tasks.
         */
        const taskListUpdatedCallback = taskListUpdatedEvent => {
            if (taskListUpdatedEvent.todoListId === this.todoListId) {
                this.element.empty();
                this.taskViewArray = this._merge(this.taskViewArray, taskListUpdatedEvent.taskArray);
                this.taskViewArray.forEach(taskView => taskView.render());
            }
        };

        this.eventBus.subscribe(EventTypes.TaskListUpdated, taskListUpdatedCallback);
        this.eventBus.subscribe(EventTypes.TaskCompletionFailed, event => alert(event.errorMsg));
        this.eventBus.subscribe(EventTypes.TaskRemovalFailed, event => alert(event.errorMsg))
    }

}