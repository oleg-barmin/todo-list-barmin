import {TodoComponent} from "./todoComponent";
import {EventTypes} from "../event/event";
import {TaskView} from "./taskView";

/**
 * Renders list of tasks.
 *
 * When {@link NewTaskAdded} happens gets new tasks,
 * removes previous task list and renders new tasks from `NewTaskAdded`.
 * Uses {@link TaskView} for each task to render it.
 *
 * @extends TodoComponent
 */
export class TodoWidget extends TodoComponent {

    /**
     * Creates `TodoWidget` instance.
     *
     * @param element JQuery element where all task should be appended
     * @param {EventBus} eventBus `EventBus` to subscribe on necessary events.
     */
    constructor(element, eventBus) {
        super(element, eventBus);
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
            let taskIndex = 1;
            this.element.empty();
            taskListUpdatedEvent.taskArray.forEach((element) => {
                this.element.append(`<div class="row no-gutters mt-2"></div>`);
                new TaskView(this.element.children().last(), this.eventBus, taskIndex, element).render();
                taskIndex++;
            });
        };

        this.eventBus.subscribe(EventTypes.TaskListUpdated, taskListUpdatedCallback);
        this.eventBus.subscribe(EventTypes.TaskCompletionFailed, event => alert(event.errorMsg));
        this.eventBus.subscribe(EventTypes.TaskRemovalFailed, event => alert(event.errorMsg))
    }

}