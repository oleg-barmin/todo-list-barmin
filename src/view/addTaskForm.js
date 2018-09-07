import {TodoComponent} from "./todoComponent";
import {AddTaskRequest} from "../event/addTaskRequest";
import {EventTypes} from "../event/event";


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
            <div class="col col-1 align-self-end">
                <button class="${addTaskBtnClass} btn btn-default btn-primary w-100">Add</button>
            </div>
            <div class="w-100"></div>
            <div class="col alert alert-danger invisible errorMsgContainer w-100 pl-3" role="alert">
            </div>`);

        const addTaskBtn = container.find(`.${addTaskBtnClass}`);
        const descriptionTextArea = container.find(`.${descriptionTextAreaClass}`);
        const errorDiv = container.find(`.${errorContainerClass}`);
        const showErrorCallback = (errorMsg) =>{
            errorDiv.empty();
            let iconSpan = $("<div>");
            iconSpan.addClass("octicon");
            iconSpan.addClass("octicon-stop");
            errorDiv.append(iconSpan);
            errorDiv.append(" "+errorMsg)
        };

        const eventBus = this.eventBus;


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
            if (keyboardEvent.ctrlKey && keyboardEvent.key === "Enter") {
                eventBus.post(new AddTaskRequest(descriptionTextArea.val()));
            }
        });
    }

}