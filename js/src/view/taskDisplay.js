import {UiComponent} from "./uiComponent";
import {TaskRemovalRequested} from "../event/taskRemovalRequested";
import {TaskCompletionRequested} from "../event/taskCompletionRequested";
import {TaskEditingStarted} from "../event/taskEditingStarted";

/**
 * Component which responsible for rendering and processing of task in display state.
 *
 * @extends UiComponent
 */
export class TaskDisplay extends UiComponent {

    /**
     * Creates `TaskView` instance.
     *
     * @param {jQuery} element jQuery element to render into
     * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
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

        const removeBtnClass = "removeBtn";
        const completeBtnClass = "completeBtn";
        const editBtnClass = "editBtn";

        const escapedTaskDescription = $('<div/>').text(this.task.description).html();
        const taskDescriptionDivClass = "taskDescription";

        this.element.append(
            `<div class="col-md-auto pr-2">${this.number}.</div>
           <div class="col-9 ${taskDescriptionDivClass}" style="white-space: pre-wrap;">${escapedTaskDescription}</div>
                <div class="col text-right">
                    <button class="${editBtnClass} btn btn-light octicon octicon-pencil"></button>
                </div>
                <div class="col-md-auto text-right">
                    <button class="${completeBtnClass} btn btn-light octicon octicon-check"></button>
                </div>
                <div class="col-md-auto text-right">
                    <button class="${removeBtnClass} btn btn-light octicon octicon-trashcan"></button>
                </div>`
        );


        const removeBtn = this.element.find(`.${removeBtnClass}`);
        const completeBtn = this.element.find(`.${completeBtnClass}`);
        const editBtn = this.element.find(`.${editBtnClass}`);
        const taskDescriptionDiv = this.element.find(`.${taskDescriptionDivClass}`);

        completeBtn.click(() => this.eventBus.post(new TaskCompletionRequested(task.id)));
        editBtn.click(() => this.eventBus.post(new TaskEditingStarted(task.id)));
        removeBtn.click(() => {
            if (confirm("Delete the task?")) {
                this.eventBus.post(new TaskRemovalRequested(task.id));
            }
        });

        if (task.completed) {
            completeBtn.remove();
            editBtn.remove();
            taskDescriptionDiv.replaceWith(() => $(`<del style="white-space: pre-wrap;"/>`)
                .append(taskDescriptionDiv.contents()));
        }
    }
}