import {TodoComponent} from "./todoComponent";
import {EventTypeEnum} from "../event/event";
import {TaskView} from "./taskViewComponent";

export class TodoWidgetComponent extends TodoComponent {
    constructor(element, eventBus) {
        super(element, eventBus);
    }

    render() {
        const todoWidgetDiv = this.element;
        const self = this;

        todoWidgetDiv.empty();

        this.eventBus.subscribe(EventTypeEnum.NewTaskAddedEvent, function (event) {
            let number = 1;
            todoWidgetDiv.empty();
            for (let curTask of event.data) {
                // self.element.append(`<div class="taskContainer row no-gutters border border-light mt-2"></div>`);
                new TaskView(self.element, self.eventBus, number, curTask).render();
                number += 1;
            }
        })

    }

}