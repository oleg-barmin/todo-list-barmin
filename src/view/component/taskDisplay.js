import {TodoComponent} from "./todoComponent";
import {TaskRemovalRequest} from "../event/taskRemovalRequest";
import {TaskCompletionRequested} from "../event/taskCompletionRequested";
import {StartTaskEditing} from "../event/startTaskEditing";

/**
 * Component which responsible for rendering and processing of task in display state.
 *
 * @extends TodoComponent
 */
export class TaskDisplay extends TodoComponent {

    /**
     * Creates `TaskView` instance.
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

        removeBtn.click(() => this.eventBus.post(new TaskRemovalRequest(task.id)));
        completeBtn.click(() => this.eventBus.post(new TaskCompletionRequested(task.id)));
        editBtn.click(() => this.eventBus.post(new StartTaskEditing(task.id)));

        if (task.completed) {
            completeBtn.remove();
            editBtn.remove();
            taskDescriptionDiv.replaceWith(() => $("<del/>").append(taskDescriptionDiv.contents()));
        }
    }
}