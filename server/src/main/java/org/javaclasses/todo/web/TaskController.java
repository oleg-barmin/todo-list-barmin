package org.javaclasses.todo.web;

import org.javaclasses.todo.model.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.javaclasses.todo.web.TodoListApplication.TASK_ID_PARAM;
import static org.javaclasses.todo.web.TodoListApplication.TODO_LIST_ID_PARAM;

/**
 * Handles {@link Task} related requests.
 *
 * @author Oleg Barmin
 */
class TaskController {

    /**
     * Extracts {@link TaskId} from parameters of Request.
     *
     * @param requestParams request parameters
     * @return {@code TaskId} from parameters
     */
    private static TaskId extractTaskId(RequestParams requestParams) {
        String taskIdParam = requestParams.getParamValue(TASK_ID_PARAM);
        return new TaskId(taskIdParam);
    }

    /**
     * Extracts {@link TodoListId} from parameters of Request.
     *
     * @param requestParams request parameters
     * @return {@code TodoListId} from parameters
     */
    //method used once, but added for consistency.
    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private static TodoListId extractTodoListId(RequestParams requestParams) {
        String taskIdParam = requestParams.getParamValue(TODO_LIST_ID_PARAM);
        return new TodoListId(taskIdParam);
    }


    /**
     * Handles get {@code Task} requests.
     *
     * @author Oleg Barmin
     */
    static class GetTaskHandler extends SecuredAbstractHandler<Void> {

        private final TodoService todoService;

        /**
         * Creates {@code GetTaskHandler} instance.
         *
         * @param todoService service to work with
         */
        GetTaskHandler(TodoService todoService) {
            super(Void.class);
            this.todoService = checkNotNull(todoService);
        }

        @Override
        Answer securedProcess(RequestData<Void> requestData, Token token) {
            TaskId taskId = extractTaskId(requestData.getRequestParams());

            Task task = todoService.authorizeBy(token)
                    .findTask(taskId)
                    .execute();

            String answerBody = objectToJson(task);

            return Answer.ok(answerBody);
        }
    }

    /**
     * Handles create {@code Task} request.
     *
     * @author Oleg Barmin
     */
    static class CreateTaskHandler extends SecuredAbstractHandler<CreateTaskPayload> {

        private final TodoService todoService;

        /**
         * Creates {@code CreateTaskHandler} instance.
         *
         * @param todoService service to work with
         */
        CreateTaskHandler(TodoService todoService) {
            super(CreateTaskPayload.class);
            this.todoService = checkNotNull(todoService);
        }

        @Override
        Answer securedProcess(RequestData<CreateTaskPayload> requestData, Token token) {
            TaskId taskId = extractTaskId(requestData.getRequestParams());
            TodoListId todoListId = extractTodoListId(requestData.getRequestParams());

            CreateTaskPayload payload = requestData.getPayload();
            String taskDescription = payload.getTaskDescription();

            todoService.authorizeBy(token)
                    .addTask(taskId)
                    .withTodoListId(todoListId)
                    .withDescription(taskDescription)
                    .execute();

            return Answer.ok();
        }
    }

    /**
     * Handles update {@code Task} request.
     *
     * @author Oleg Barmin
     */
    static class TaskUpdateHandler extends SecuredAbstractHandler<TaskUpdatePayload> {

        private final TodoService todoService;

        /**
         * Creates {@code TaskUpdateHandler} instance.
         *
         * @param todoService service to work with
         */
        TaskUpdateHandler(TodoService todoService) {
            super(TaskUpdatePayload.class);
            this.todoService = checkNotNull(todoService);
        }

        @Override
        Answer securedProcess(RequestData<TaskUpdatePayload> requestData, Token token) {
            TaskUpdatePayload payload = requestData.getPayload();
            TaskId taskId = extractTaskId(requestData.getRequestParams());

            String taskDescription = payload.getTaskDescription();
            boolean taskStatus = payload.isCompleted();

            todoService.authorizeBy(token)
                    .updateTask(taskId)
                    .setStatus(taskStatus)
                    .withDescription(taskDescription)
                    .execute();

            return Answer.ok();
        }
    }

    /**
     * Handles remove {@code Task} request.
     */
    static class TaskRemoveHandler extends SecuredAbstractHandler<Void> {

        private final TodoService todoService;

        /**
         * Creates {@code TaskRemoveHandler} instance.
         *
         * @param todoService service to work with
         */
        TaskRemoveHandler(TodoService todoService) {
            super(Void.class);
            this.todoService = checkNotNull(todoService);
        }

        @Override
        Answer securedProcess(RequestData<Void> requestData, Token token) {
            TaskId taskId = extractTaskId(requestData.getRequestParams());

            todoService.authorizeBy(token)
                    .removeTask(taskId)
                    .execute();

            return Answer.ok();
        }
    }
}
