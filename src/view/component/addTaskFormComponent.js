import {TodoComponent} from "./todoComponent";
import {AddTaskRequestEvent} from "../event/addTaskRequestEvent";
import {EventTypeEnum} from "../event/event";

export class AddTaskFormComponent extends TodoComponent {
    constructor(element, eventBus) {
        super(element, eventBus);

    }

    render() {
        let container = this.element;
        container.empty();
        const descriptionTextAreaClass = "descriptionTextArea";
        const addTaskBtnClass = "addTaskBtn";

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
        eventBus.subscribe(EventTypeEnum.NewTaskAddedEvent, function () {
            descriptionTextArea.val('');
        });
        addTaskBtn.click(() => {
            eventBus.post(new AddTaskRequestEvent(descriptionTextArea.val()));
        });


    }

}