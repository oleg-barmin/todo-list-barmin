import {TodoComponent} from "./todoComponent";

export class TaskView extends TodoComponent {
    constructor(element, eventBus, number, task) {
        super(element, eventBus);
        this.eventBus = eventBus;
        this.task = task;
        this.number = number;
    }

    render() {
        const task = this.task;
        const number = this.number;
        const taskComponent = this.element;
        const trashButtonClass = "trashButton";


        const encodedDescription = TaskView.htmlEncode(undefined,task.description);
        console.log(encodedDescription);
        taskComponent.append(
            `<div class="row no-gutters border border-light mt-2">
                <input type="hidden" name="taskId" value="${task.id}">
                <div class="col-md-auto pr-2">${number}.</div>
                <div class="col-10 taskDescriptionDiv" style="white-space: pre-wrap;">${encodedDescription}</div>
                <div class="col text-right">
                    <button class="btn btn-light octicon octicon-check"></button>
                </div>
                <div class="col-md-auto text-right">
                    <button class="${trashButtonClass} btn btn-light octicon octicon-trashcan"></button>
                </div>
            </div>`
        );
        this.element.find(".taskDescriptionDiv");
    }

}