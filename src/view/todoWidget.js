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
     * @param {jQuery} element JQuery element where all task should be appended
     * @param {EventBus} eventBus `EventBus` to subscribe on necessary events.
     */
    constructor(element, eventBus) {
        super(element, eventBus);
        this.taskViewArray = [];
    }

    /**
     * Empties given container for tasks to populate it with new tasks.
     */
    render() {
        this.element.empty();

        const findTaskViewCallback = taskId =>{
            return this.taskViewArray.find(element => element.task.id.compareTo(taskId) === 0)
        };

        /**
         * Processes `TaskListUpdated` event.
         * Populates container of tasks with `TaskView` for each task stored in occurred `TaskListUpdated` event.
         *
         * @param {TaskListUpdated} taskListUpdatedEvent occurred `TaskListUpdated` event with array of new tasks.
         */
        const taskListUpdatedCallback = taskListUpdatedEvent => {
            this.element.empty();

            this.taskViewArray = taskListUpdatedEvent.taskArray.map((task, index) => {

                const taskContainer = this.element.append(`<div class="row no-gutters mt-2"></div>`).children().last();
                const taskViewWithCurrentTask = findTaskViewCallback(task.id);

                if(taskViewWithCurrentTask){
                    taskViewWithCurrentTask.element.remove();
                    taskViewWithCurrentTask.render(taskContainer, ++index, task);
                    return taskViewWithCurrentTask;
                }

                const newTaskView = new TaskView(taskContainer, this.eventBus, ++index, task);
                newTaskView.render();
                return newTaskView;
            });
        };

        this.eventBus.subscribe(EventTypes.TaskListUpdated, taskListUpdatedCallback);
        this.eventBus.subscribe(EventTypes.TaskCompletionFailed, event => alert(event.errorMsg));
        this.eventBus.subscribe(EventTypes.TaskRemovalFailed, event => alert(event.errorMsg))
    }

}