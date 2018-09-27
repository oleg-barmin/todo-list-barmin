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
    static class GetTaskRequestHandler extends SecuredAbstractRequestHandler<Void> {

        private final TodoService todoService;

        /**
         * Creates {@code GetTaskRequestHandler} instance.
         *
         * @param todoService service to work with
         */
        GetTaskRequestHandler(TodoService todoService) {
            super(Void.class);
            this.todoService = checkNotNull(todoService);
        }

        @Override
        Answer processVerifiedRequest(RequestData<Void> requestData, Token token) {
            TaskId taskId = extractTaskId(requestData.getRequestParams());

            Task task = todoService.findTask(taskId).
                    authorizedWith(token)
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
    static class CreateTaskRequestHandler extends SecuredAbstractRequestHandler<CreateTaskPayload> {

        private final TodoService todoService;

        /**
         * Creates {@code CreateTaskRequestHandler} instance.
         *
         * @param todoService service to work with
         */
        CreateTaskRequestHandler(TodoService todoService) {
            super(CreateTaskPayload.class);
            this.todoService = checkNotNull(todoService);
        }

        @Override
        Answer processVerifiedRequest(RequestData<CreateTaskPayload> requestData, Token token) {
            TaskId taskId = extractTaskId(requestData.getRequestParams());
            TodoListId todoListId = extractTodoListId(requestData.getRequestParams());

            CreateTaskPayload payload = requestData.getPayload();
            String taskDescription = payload.getTaskDescription();

            todoService.addTask(taskId)
                    .authorizedWith(token)
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
    static class TaskUpdateRequestHandler extends SecuredAbstractRequestHandler<TaskUpdatePayload> {

        private final TodoService todoService;

        /**
         * Creates {@code TaskUpdateRequestHandler} instance.
         *
         * @param todoService service to work with
         */
        TaskUpdateRequestHandler(TodoService todoService) {
            super(TaskUpdatePayload.class);
            this.todoService = checkNotNull(todoService);
        }

        @Override
        Answer processVerifiedRequest(RequestData<TaskUpdatePayload> requestData, Token token) {
            TaskUpdatePayload payload = requestData.getPayload();
            TaskId taskId = extractTaskId(requestData.getRequestParams());

            String taskDescription = payload.getTaskDescription();
            boolean taskStatus = payload.isTaskStatus();

            todoService.updateTask(taskId)
                    .authorizedWith(token)
                    .setStatus(taskStatus)
                    .withDescription(taskDescription)
                    .execute();

            return Answer.ok();
        }
    }

    /**
     * Handles remove {@code Task} request.
     */
    static class TaskRemoveRequestHandler extends SecuredAbstractRequestHandler<Void> {

        private final TodoService todoService;

        /**
         * Creates {@code TaskRemoveRequestHandler} instance.
         *
         * @param todoService service to work with
         */
        TaskRemoveRequestHandler(TodoService todoService) {
            super(Void.class);
            this.todoService = checkNotNull(todoService);
        }

        @Override
        Answer processVerifiedRequest(RequestData<Void> requestData, Token token) {
            TaskId taskId = extractTaskId(requestData.getRequestParams());

            todoService.removeTask(taskId)
                    .authorizedWith(token)
                    .execute();

            return Answer.ok();
        }
    }
}
