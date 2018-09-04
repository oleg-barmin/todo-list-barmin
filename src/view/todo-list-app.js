import {EventBus} from "./event/event";
import {AddTaskFormComponent} from "./component/addTaskFormComponent";
import {Controller} from "./controller";
import {TodoWidgetComponent} from "./component/todoWidgetComponent";

export class Application {
    constructor(rootSelector) {
        this.root = rootSelector;
        this.eventBus = new EventBus(".eventBus");
        this.controller = new Controller(this.eventBus);
    }

    start() {
        let addForm = $(this.root).find(".addTaskForm");
        let addTaskForm = new AddTaskFormComponent(addForm, this.eventBus);
        let taskView = new TodoWidgetComponent(".todoWidget", this.eventBus);

        addTaskForm.render();
        taskView.render();

    }
}