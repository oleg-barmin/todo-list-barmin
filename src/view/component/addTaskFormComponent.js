import {TodoComponent} from "./todoComponent";
import {AddTaskRequestEvent} from "../event/addTaskRequestEvent";
import {EventTypeEnumeration} from "../event/event";


/**
 * Component which responsible for rendering and processing of add task form.
 */
export class AddTaskFormComponent extends TodoComponent {

    /**
     * Creates `AddTaskFormComponent` instance.
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

        container.empty();

        container.append(`<div class="col">
                <textarea class="${descriptionTextAreaClass} form-control w-100"></textarea>
            </div>
            <div class="col col-1 align-self-end text-right">
                <button class="${addTaskBtnClass} btn btn-default btn-primary w-100">add</button>
            </div>
            <div class="w-100"></div>
            <div class="col">
                <label hidden class="w-100 alert-danger">Exception msg</label>
            </div>`);

        let addTaskBtn = container.find(`.${addTaskBtnClass}`);
        let descriptionTextArea = container.find(`.${descriptionTextAreaClass}`);

        const eventBus = this.eventBus;

        eventBus.subscribe(EventTypeEnumeration.NewTaskAddedEvent, function () {
            descriptionTextArea.val('');
        });
        addTaskBtn.click(() => {
            eventBus.post(new AddTaskRequestEvent(descriptionTextArea.val()));
        });


    }

}