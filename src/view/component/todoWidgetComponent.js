import {TodoComponent} from "./todoComponent";
import {EventTypeEnum} from "../event/event";
import {TaskView} from "./taskViewComponent";

export class TodoWidgetComponent extends TodoComponent {
    constructor(selector, eventBus) {
        super(selector, eventBus);
    }

    render() {
        const todoWidgetDiv = $(this.selector);
        const self = this;

        todoWidgetDiv.empty();

        this.eventBus.subscribe(EventTypeEnum.NewTaskAddedEvent, function (event) {
            let number = 1;
            todoWidgetDiv.empty();
            for (let curTask of event.data) {
                new TaskView(self.selector, self.eventBus, number, curTask).render();
                number += 1;
            }
        })

    }

}