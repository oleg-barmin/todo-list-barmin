import {TodoComponent} from "./todoComponent";
import {AddTaskRequest} from "../event/addTaskRequest";
import {EventTypeEnumeration} from "../event/event";


/**
 * Component which responsible for rendering and processing of add task form.
 */
export class AddTaskForm extends TodoComponent {

    /**
     * Creates `AddTaskForm` instance.
     *
     * @param element element to render into
     * @param {EventBus} eventBus `EventBus` to connect with controller
     */
    constructor(element, eventBus) {
        super(element, eventBus);
    }

    /**
     * Renders tasks container and subscribes to necessary Events.
     */
    render() {
        const container = this.element;
        const descriptionTextAreaClass = "descriptionTextArea";
        const addTaskBtnClass = "addTaskBtn";
        const errorContainerClass = "errorMsgContainer";

        container.empty();

        container.append(`<div class="col">
                <textarea class="${descriptionTextAreaClass} form-control w-100"></textarea>
            </div>
            <div class="col col-1 align-self-end text-right">
                <button class="${addTaskBtnClass} btn btn-default btn-primary w-100">add</button>
            </div>
            <div class="w-100"></div>
            <div class="col">
                <label class="errorMsgContainer invisible w-100 alert-danger"></label>
            </div>`);

        const addTaskBtn = container.find(`.${addTaskBtnClass}`);
        const descriptionTextArea = container.find(`.${descriptionTextAreaClass}`);
        const errorLabel = container.find(`.${errorContainerClass}`);

        const eventBus = this.eventBus;

        /**
         * Processes `NewTaskAdded` event.
         * Makes `descriptionTextArea` and `errorLabel` empty and invisible.
         */
        const newTaskAddedCallback = () => {
            descriptionTextArea.val('');
            errorLabel.empty();
            errorLabel.addClass("invisible")
        };

        /**
         * Processes `NewTaskValidationFailed`.
         * Makes `errorLabel` visible and appends into it error message from `NewTaskValidationFailed` event.
         *
         * @param {NewTaskValidationFailed} newTaskValidationFailedEvent `NewTaskValidationFailed` with
         *        error message to display.
         */
        const newTaskValidationFailedCallback = newTaskValidationFailedEvent => {
            errorLabel.removeClass("invisible");
            errorLabel.empty();
            errorLabel.append(newTaskValidationFailedEvent.errorMsg);
        };

        eventBus.subscribe(EventTypeEnumeration.NewTaskAdded, newTaskAddedCallback);
        eventBus.subscribe(EventTypeEnumeration.NewTaskValidationFailed, newTaskValidationFailedCallback);

        addTaskBtn.click(() => eventBus.post(new AddTaskRequest(descriptionTextArea.val())));
    }

}