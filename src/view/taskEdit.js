import {TodoComponent} from "./todoComponent";
import {TaskEditingCanceled} from "../event/taskEditingCanceled";
import {TaskUpdateRequested} from "../event/taskUpdateRequested";
import {EventTypes} from "../event/event";

/**
 * Component which responsible for rendering and processing of task in edit state.
 *
 * @extends TodoComponent
 */
export class TaskEdit extends TodoComponent {

    /**
     * Creates `TaskEdit` instance.
     *
     * @param {jQuery} element jQuery element to render into
     * @param {EventBus} eventBus eventBust to subscribe and post events
     * @param {Number} number number of the task in the list of tasks
     * @param {Task} task task to render
     */
    constructor(element, eventBus, number, task) {
        super(element, eventBus);
        this.eventBus = eventBus;
        this.task = task;
        this.number = number;
        this.currentInput = task.description;
    }

    render() {
        const saveBtnClass = "saveBtn";
        const cancelBtnClass = "cancelBtn";
        const editDescriptionTextAreaClass = "editDescriptionTextArea";
        const errorLabelClass = "errorMsgLabel";

        this.element.append(
            `<div class="col-md-auto pr-2">${this.number}.</div>
                <div class="col-9">
                    <textarea style="white-space: pre-wrap;" class="${editDescriptionTextAreaClass} form-control"></textarea>
                </div>
                <div class="col text-right">
                    <button class="${saveBtnClass} btn btn-sm btn-primary">Save</button>
                </div>
                <div class="col-sm-auto">
                    <button class="${cancelBtnClass} btn btn-sm btn-light">Cancel</button>
                </div>
                <div class="w-100"></div>
                <div class="col">
                <label class="${errorLabelClass} alert alert-danger invisible w-100 alert-danger"></label>
            </div>`
        );

        const saveBtn = this.element.find(`.${saveBtnClass}`);
        const cancelBtn = this.element.find(`.${cancelBtnClass}`);
        const editTextArea = this.element.find(`.${editDescriptionTextAreaClass}`);
        const errorLabel = this.element.find(`.${errorLabelClass}`);

        const descriptionRowsNumber = this.currentInput.split(/\r\n|\r|\n/).length;
        editTextArea.attr("rows", descriptionRowsNumber > 10 ? 10 : descriptionRowsNumber);
        editTextArea.focus().val(this.currentInput);

        /**
         * Processes `TaskUpdateFailed` event.
         * Makes `errorLabel` visible and appends into it error message from occurred `TaskUpdateFailed` event.
         *
         * @param {TaskUpdateFailed} taskUpdateFailedEvent occurred `TaskUpdateFailed` event
         *         with error message to display.
         */
        const taskUpdateFailedCallback = taskUpdateFailedEvent => {
            if (taskUpdateFailedEvent.taskId.compareTo(this.task.id) === 0) {
                errorLabel.removeClass("invisible");
                errorLabel.empty();
                errorLabel.append(taskUpdateFailedEvent.errorMsg);
            }
        };
        this.eventBus.subscribe(EventTypes.TaskUpdateFailed, taskUpdateFailedCallback);

        cancelBtn.click(() => this.eventBus.post(new TaskEditingCanceled(this.task.id)));

        saveBtn.click(() => {
            const newTaskDescription = editTextArea.val();
            if (newTaskDescription === this.task.description) {
                this.eventBus.post(new TaskEditingCanceled(this.task.id));
                return;
            }
            this.eventBus.post(new TaskUpdateRequested(this.task.id, newTaskDescription));
        });

        editTextArea.change(() => this.currentInput = editTextArea.val());
    }
}