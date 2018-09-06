import {TodoComponent} from "./todoComponent";
import {CancelTaskEditing} from "../event/cancelTaskEditing";
import {TaskUpdateRequest} from "../event/taskUpdateRequest";
import {EventTypeEnumeration} from "../event/event";

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
        const errorLabelClass = "errorMsgLabel";

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
                </div>
                <div class="w-100"></div>
                <div class="col">
                <label class="${errorLabelClass} invisible w-100 alert-danger"></label>
            </div>`
        );

        const saveBtn = this.element.find(`.${saveBtnClass}`);
        const cancelBtn = this.element.find(`.${cancelBtnClass}`);
        const editTextArea = this.element.find(`.${editDescriptionTextAreaClass}`);
        const errorLabel = this.element.find(`.${errorLabelClass}`);

        /**
         * Processes `TaskUpdateFailed` event.
         * Makes `errorLabel` visible and appends into it error message from occurred `TaskUpdateFailed` event.
         *
         * @param {TaskUpdateFailed} taskUpdateFailedEvent occurred `TaskUpdateFailed` event
         *         with error message to display.
         */
        const taskUpdateFailedCallback = taskUpdateFailedEvent => {
            errorLabel.removeClass("invisible");
            errorLabel.empty();
            errorLabel.append(taskUpdateFailedEvent.errorMsg);
        };

        this.eventBus.subscribe(EventTypeEnumeration.TaskUpdateFailed, taskUpdateFailedCallback);

        saveBtn.click(() => this.eventBus.post(new TaskUpdateRequest(this.task.id, editTextArea.val())));
        cancelBtn.click(() => this.eventBus.post(new CancelTaskEditing(task.id)));
    }
}