import {UiComponent} from "./uiComponent";
import {TaskEditingCanceled} from "../event/taskEditingCanceled";
import {TaskUpdateRequested} from "../event/taskUpdateRequested";
import {EventTypes} from "../event/event";

/**
 * Component which responsible for rendering and processing of task in edit state.
 *
 * @extends UiComponent
 * @author Oleg Barmin
 */
export class TaskEdit extends UiComponent {

    /**
     * Creates `TaskEdit` instance.
     *
     * @param {jQuery} element jQuery element to render into
     * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
     * @param {Number} number number of the task in the list of tasks
     * @param {Task} task task to render
     * @param {TodoListId} todoListId ID of to-do list to which `TaskEdit` is related to
     */
    constructor(element, eventBus, number, task, todoListId) {
        super(element, eventBus);
        this.eventBus = eventBus;
        this.task = task;
        this.number = number;
        this.todoListId = todoListId;
        this.currentInput = task.description;
        this.errorMsg = null;
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
                <label class="col-sm-auto pr-0"></label>
                <label class="${errorLabelClass} col-9 invisible alert alert-danger p-1 mt-1"></label>
            </div>`
        );

        const saveBtn = this.element.find(`.${saveBtnClass}`);
        const cancelBtn = this.element.find(`.${cancelBtnClass}`);
        const editTextArea = this.element.find(`.${editDescriptionTextAreaClass}`);
        const errorLabel = this.element.find(`.${errorLabelClass}`);

        const descriptionRowsNumber = this.currentInput.split(/\r\n|\r|\n/).length;
        editTextArea.attr("rows", descriptionRowsNumber > 10 ? 10 : descriptionRowsNumber);
        editTextArea.focus().val(this.currentInput);
        editTextArea.scrollTop(editTextArea[0].scrollHeight - editTextArea.height());


        const renderErrorMsgCallback = errorMsg => {
            this.errorMsg = errorMsg;
            errorLabel.removeClass("invisible");
            errorLabel.empty();
            errorLabel.append(this.errorMsg);
        };

        if (this.errorMsg) {
            renderErrorMsgCallback(this.errorMsg)
        }


        /**
         * Processes `TaskUpdateFailed` event.
         * Makes `errorLabel` visible and appends into it error message from occurred `TaskUpdateFailed` event.
         *
         * @param {TaskUpdateFailed} taskUpdateFailedEvent occurred `TaskUpdateFailed` event
         *         with error message to display.
         */
        const taskUpdateFailedCallback = taskUpdateFailedEvent => {
            if (taskUpdateFailedEvent.taskId.compareTo(this.task.id) === 0) {
                renderErrorMsgCallback(taskUpdateFailedEvent.errorMsg)
            }
        };
        this.eventBus.subscribe(EventTypes.TaskUpdateFailed, taskUpdateFailedCallback);

        cancelBtn.click(() => this.eventBus.post(new TaskEditingCanceled(this.task.id)));

        /**
         * Posts `TaskEditingCanceled` if tasks description equals content of textarea,
         * otherwise posts `TaskUpdateRequested`.
         */
        const saveCallback = () => {
            const newTaskDescription = editTextArea.val();
            if (newTaskDescription === this.task.description) {
                this.eventBus.post(new TaskEditingCanceled(this.task.id));
                return;
            }
            this.eventBus.post(new TaskUpdateRequested(this.task.id, newTaskDescription, false, this.todoListId));
        };

        saveBtn.click(saveCallback);

        editTextArea.change(() => this.currentInput = editTextArea.val());
        editTextArea.keydown(keyboardEvent => {
            if ((keyboardEvent.ctrlKey || keyboardEvent.metaKey) && keyboardEvent.key === "Enter") {
                saveCallback();
            }
        });
    }
}