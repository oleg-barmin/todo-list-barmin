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

        eventBus.subscribe(EventTypeEnumeration.NewTaskAdded, () => {
            descriptionTextArea.val('');
            errorLabel.empty();
            errorLabel.addClass("invisible")
        });
        eventBus.subscribe(EventTypeEnumeration.NewTaskValidationFailed, event => {
            errorLabel.removeClass("invisible");
            errorLabel.empty();
            errorLabel.append(event.errorMsg);
        });

        addTaskBtn.click(() => {
            eventBus.post(new AddTaskRequest(descriptionTextArea.val()));
        });


    }

}