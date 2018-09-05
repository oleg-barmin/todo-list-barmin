import {TodoComponent} from "./todoComponent";

/**
 * Component which responsible for rendering and processing of task.
 *
 * @extends TodoComponent
 */
export class TaskViewComponent extends TodoComponent {

    /**
     * Creates `TaskViewComponent` instance.
     *
     * @param element Jquery element to render into
     * @param {EventBus} eventBus eventBust to subscribe and post events
     * @param {Number} number number of the task in the list of tasks
     * @param {Task} task task to render
     */
    constructor(element, eventBus, number, task) {
        super(element, eventBus);
        this.eventBus = eventBus;
        this.task = task;
        this.number = number;
    }

    /**
     * Renders task into given element.
     */
    render() {
        const task = this.task;

        this.element.append(
            `<div class="row no-gutters border border-light mt-2">
                <input type="hidden" name="taskId" value="${task.id}">
                <div class="col-md-auto pr-2">${this.number}.</div>
                <div class="col-10" style="white-space: pre-wrap;">${task.description}</div>
                <div class="col text-right">
                    <button class="btn btn-light octicon octicon-check"></button>
                </div>
                <div class="col-md-auto text-right">
                    <button class="btn btn-light octicon octicon-trashcan"></button>
                </div>
            </div>`
        );
    }
}