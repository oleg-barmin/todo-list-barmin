import {TodoComponent} from "./todoComponent";
import {CancelTaskEditing} from "../event/cancelTaskEditing";
import {TaskUpdateRequest} from "../event/taskUpdateRequest";

/**
 * Component which responsible for rendering and processing of task in edit state.
 *
 * @extends TodoComponent
 */
export class TaskEdit extends TodoComponent {
    /**
     * Creates `TaskEdit` instance.
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

    render() {
        const task = this.task;

        const saveBtnClass = "saveBtn";
        const cancelBtnClass = "cancelBtn";
        const editDescriptionTextAreaClass = "editDescriptionTextArea";

        this.element.append(
            `<div class="col-md-auto pr-2">${this.number}.</div>
                <div class="col-10">
                    <textarea style="white-space: pre-wrap;" class="${editDescriptionTextAreaClass} form-control">${task.description}</textarea>
                </div>
                <div class="col text-right">
                    <button class="${saveBtnClass} btn btn-light octicon octicon-check"></button>
                </div>
                <div class="col-md-auto text-right">
                    <button class="${cancelBtnClass} btn btn-light octicon octicon-x"></button>
                </div>`
        );

        const saveBtn = this.element.find(`.${saveBtnClass}`);
        const cancelBtn = this.element.find(`.${cancelBtnClass}`);
        const editTextArea = this.element.find(`.${editDescriptionTextAreaClass}`);

        saveBtn.click(() => {
            const newTaskDescription = editTextArea.val();
            this.eventBus.post(new TaskUpdateRequest(this.task.id, newTaskDescription))
        });

        cancelBtn.click(() => {
            this.eventBus.post(new CancelTaskEditing(task.id));
        });
    }
}