import {EventBus} from "./event/event";
import {AddTaskFormComponent} from "./component/addTaskFormComponent";
import {Controller} from "./controller";
import {TodoWidgetComponent} from "./component/todoWidgetComponent";

/**
 * Starts a to-do list app.
 */
export class TodoListApp {

    /**
     * Creates `TodoListApp` instance.
     *
     * @param rootElement root JQuery element where to-do app will be deployed
     */
    constructor(rootElement) {
        this.root = rootElement;
    }

    /**
     * Starts a `TodoListApp` for `rootElement` that was provided in constructor.
     *
     * Creates an environment for necessary components and renders them.
     */
    start() {
        this.root.append("<div class='container'></div>");
        let container = $(this.root.find(".container")[0]);

        container.append(`<div hidden class="eventBus"></div>
        <div class="row justify-content-md-center">
            <div class="col-md-auto">
                <h1>To-Do</h1>
            </div>
        </div>
        <div class="addTaskForm row justify-content-md-center no-gutters"></div>
        <div class="todoWidget"></div>`);

        this.eventBus = new EventBus(container.find(".eventBus"));
        this.controller = new Controller(this.eventBus);

        let addTaskForm = new AddTaskFormComponent(container.find(".addTaskForm"), this.eventBus);
        let taskView = new TodoWidgetComponent(container.find(".todoWidget"), this.eventBus);

        addTaskForm.render();
        taskView.render();
    }
}


$(function () {
    let todoLists = $(".todoList");
    new TodoListApp($(todoLists[0])).start();
});