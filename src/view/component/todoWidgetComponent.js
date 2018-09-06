import {TodoComponent} from "./todoComponent";
import {EventTypeEnumeration} from "../event/event";
import {TaskViewComponent} from "./taskViewComponent";

/**
 * Renders list of tasks.
 *
 * When {@link NewTaskAddedEvent} happens gets new tasks,
 * removes previous task list and renders new tasks from `NewTaskAddedEvent`.
 * Uses {@link TaskViewComponent} for each task to render it.
 *
 * @extends TodoComponent
 */
export class TodoWidgetComponent extends TodoComponent {

    /**
     * Creates `TodoWidgetComponent` instance.
     *
     * @param element JQuery element where all task should be appended
     * @param {EventBus} eventBus `EventBus` to subscribe on necessary events.
     */
    constructor(element, eventBus) {
        super(element, eventBus);
    }

    /**
     * Empties given container for tasks to populate it
     * with new tasks when `NewTaskAddedEvent` will happen.
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
                new TaskViewComponent(self.element.children().last(), self.eventBus, number, curTask).render();
                number += 1;
            }
        });

        this.eventBus.subscribe(EventTypeEnumeration.TaskCompletionFailed, function (event) {
            alert(event.errorMsg);
        });

        this.eventBus.subscribe(EventTypeEnumeration.TaskRemovalFail, function (event) {
            alert(event.errorMsg);
        })

    }

}