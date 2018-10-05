import {UiComponent} from "./uiComponent";
import {AddTaskRequest} from "../event/addTaskRequest";
import {EventTypes} from "../event/event";


/**
 * Component which responsible for rendering and processing of add task form.
 */
export class AddTaskForm extends UiComponent {

    /**
     * Creates `AddTaskForm` instance.
     *
     * @param {jQuery} element jQuery element to render into
     * @param {EventBus} eventBus `EventBus` to subscribe and publish component-specific events
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
            <div class="col col-1 align-self-end">
                <button class="${addTaskBtnClass} btn btn-default btn-primary w-100">Add</button>
            </div>
            <div class="w-100"></div>
            <div class="col alert alert-danger invisible errorMsgContainer w-100 pl-3" role="alert">
            </div>`);

        const addTaskBtn = container.find(`.${addTaskBtnClass}`);
        const descriptionTextArea = container.find(`.${descriptionTextAreaClass}`);
        const errorDiv = container.find(`.${errorContainerClass}`);
        const eventBus = this.eventBus;

        /**
         * Renders given error message under `descriptionTextAreaClass`.
         *
         * @param {string} errorMsg error message to render
         */
        const showErrorCallback = errorMsg => {
            errorDiv.empty();
            let iconSpan = $("<div>");
            iconSpan.addClass("octicon");
            iconSpan.addClass("octicon-stop");
            errorDiv.append(iconSpan);
            errorDiv.append(" " + errorMsg)
        };

        /**
         * Processes `NewTaskAdded` event.
         * Makes `descriptionTextArea` and `errorDiv` empty and invisible.
         */
        const newTaskAddedCallback = () => {
            descriptionTextArea.val('');
            errorDiv.empty();
            errorDiv.addClass("invisible")
        };

        /**
         * Processes `NewTaskValidationFailed`.
         * Makes `errorDiv` visible and appends into it error message from occurred `NewTaskValidationFailed` event.
         *
         * @param {NewTaskValidationFailed} newTaskValidationFailedEvent `NewTaskValidationFailed` with
         *        error message to display.
         */
        const newTaskValidationFailedCallback = newTaskValidationFailedEvent => {
            errorDiv.removeClass("invisible");
            showErrorCallback(newTaskValidationFailedEvent.errorMsg);
        };

        eventBus.subscribe(EventTypes.NewTaskAdded, newTaskAddedCallback);
        eventBus.subscribe(EventTypes.NewTaskValidationFailed, newTaskValidationFailedCallback);

        addTaskBtn.click(() => eventBus.post(new AddTaskRequest(descriptionTextArea.val())));
        descriptionTextArea.keydown(keyboardEvent => {
            if ((keyboardEvent.ctrlKey || keyboardEvent.metaKey) && keyboardEvent.key === "Enter") {
                eventBus.post(new AddTaskRequest(descriptionTextArea.val()));
            }
        });
    }

}