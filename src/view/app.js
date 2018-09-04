import {Application} from "./todo-list-app";

$(function () {
    new Application($(".todoList")[0]).start()
});