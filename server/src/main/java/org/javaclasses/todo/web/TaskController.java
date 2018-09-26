package org.javaclasses.todo.web;

import org.javaclasses.todo.model.*;

class TaskController {

    static class GetTaskHandler extends SecuredAbstractHandler<Void> {

        private final TodoService todoService;

        /**
         * Creates {@code AbstractRequestHandler} instance.
         */
        GetTaskHandler(TodoService todoService) {
            super(Void.class);
            this.todoService = todoService;
        }

        @Override
        Answer securedProcess(RequestData<Void> requestData, Token token) {
            String todoListIdParam = requestData.getRequestParams().getParamValue(":todolistid");
            String taskIdParam = requestData.getRequestParams().getParamValue(":taskid");

            TaskId taskId = new TaskId(taskIdParam);

            Task task = todoService.authorizeBy(token)
                    .findTask(taskId)
                    .execute();

            TodoListId todoListId = task.getTodoListId();

            if (!todoListId.equals(new TodoListId(todoListIdParam))) {
                return Answer.forbidden();
            }

            String answerBody = objectToJson(task);

            return Answer.ok(answerBody);
        }
    }

    static class CreateTaskHandler extends SecuredAbstractHandler<CreateTaskPayload> {
        private final TodoService todoService;

        /**
         * Creates {@code AbstractRequestHandler} instance.
         */
        CreateTaskHandler(TodoService todoService) {
            super(CreateTaskPayload.class);
            this.todoService = todoService;
        }

        @Override
        Answer securedProcess(RequestData<CreateTaskPayload> requestData, Token token) {
            CreateTaskPayload payload = requestData.getPayload();

            String taskIdParam = requestData.getRequestParams().getParamValue(":taskid");
            String todoListIdParam = requestData.getRequestParams().getParamValue(":todolistid");

            TodoListId todoListId = new TodoListId(todoListIdParam);
            TaskId taskId = new TaskId(taskIdParam);
            String taskDescription = payload.getTaskDescription();

            todoService.authorizeBy(token)
                    .addTask(taskId)
                    .withTodoListId(todoListId)
                    .withDescription(taskDescription)
                    .execute();

            return Answer.ok();
        }
    }
}
