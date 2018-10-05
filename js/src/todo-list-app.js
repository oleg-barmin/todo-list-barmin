import {EventBus, EventTypes} from "./event/event";
import {Controller} from "./controller";
import {SignInForm} from "./view/signInForm";
import {AddTaskForm} from "./view/addTaskForm";
import {TodoWidget} from "./view/todoWidget";

/**
 * Starts a to-do list app.
 */
export class TodoListApp {

    /**
     * Creates `TodoListApp` instance.
     *
     * @param rootElement root jQuery element where to-do app will be deployed
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
        this.root.append(`<div class='container'></div>`);
        this.root.append(`<div hidden class="eventBus"></div>`);

        this.eventBus = new EventBus(this.root.find(".eventBus"));
        const container = $(this.root.find(".container")[0]);

        this.controller = new Controller(this.eventBus);

        this.eventBus.subscribe(EventTypes.SignInCompleted, () => {
            container.empty();

            container.append(`<div class="row justify-content-md-center">
                                <div class="col-md-auto">
                                    <h1>To-Do</h1>
                                </div>
                                </div>
                            <div class="addTaskForm row justify-content-md-center"></div>
                            <div class="todoWidget"></div>`);

            let addTaskForm = new AddTaskForm(container.find(".addTaskForm"), this.eventBus);
            let taskView = new TodoWidget(container.find(".todoWidget"), this.eventBus);

            addTaskForm.render();
            taskView.render();
        });

        let signInForm = new SignInForm(container, this.eventBus);
        signInForm.render()
    }
}


$(function () {
    let todoLists = $(".todoList");
    new TodoListApp($(todoLists[0])).start();
});