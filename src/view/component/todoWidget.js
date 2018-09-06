import {TodoComponent} from "./todoComponent";
import {EventTypeEnumeration} from "../event/event";
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
     * Empties given container for tasks to populate it
     * with new tasks when `NewTaskAdded` will happen.
     */
    render() {
        const todoWidgetDiv = this.element;
        const self = this;

        todoWidgetDiv.empty();

        this.eventBus.subscribe(EventTypeEnumeration.TaskListUpdated, function (event) {
            let number = 1;
            todoWidgetDiv.empty();
            for (let curTask of event.taskArray) {
                self.element.append(`<div class="row no-gutters border border-light mt-2"></div>`);
                new TaskView(self.element.children().last(), self.eventBus, number, curTask).render();
                number += 1;
            }
        });

        this.eventBus.subscribe(EventTypeEnumeration.TaskCompletionFailed, function (event) {
            alert(event.errorMsg);
        });

        this.eventBus.subscribe(EventTypeEnumeration.TaskRemovalFailed, function (event) {
            alert(event.errorMsg);
        })

    }

}