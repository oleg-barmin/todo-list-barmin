package org.javaclasses.todo.web;

import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.TaskNotFoundException;
import org.javaclasses.todo.model.TodoListNotFoundException;
import org.javaclasses.todo.model.TodoService;
import org.javaclasses.todo.model.entity.Task;
import org.javaclasses.todo.model.entity.TaskId;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.model.entity.Token;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Handles {@link Task} related requests.
 *
 * @author Oleg Barmin
 */
class TaskController {

    private TaskController() {
    }

    /**
     * Extracts {@link TaskId} from parameters of Request.
     *
     * @param requestParams request parameters
     * @return {@code TaskId} from parameters
     */
    private static TaskId extractTaskId(RequestParams requestParams) {
        String taskIdParam = requestParams.getParamValue(Params.getTaskIdParam());
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
        String taskIdParam = requestParams.getParamValue(Params.getTodoListIdParam());
        return new TodoListId(taskIdParam);
    }

    /**
     * Handles get {@code Task} requests.
     *
     * @author Oleg Barmin
     */
    static class GetTaskRequestHandler extends SecuredAbstractRequestHandler {

        private final TodoService todoService;

        /**
         * Creates {@code GetTaskRequestHandler} instance.
         *
         * @param todoService service to work with
         */
        GetTaskRequestHandler(TodoService todoService) {
            this.todoService = checkNotNull(todoService);
        }

        /**
         * Retrieves task with ID specified in URL parameters in to-do list with ID specified in URL parameters.
         *
         * @param requestData data of get task request
         * @param token       token of user who sent request
         * @return answer with status code 200 and requested task in body if request was handled successfully.
         * @throws TaskNotFoundException        if task with given ID was not found
         * @throws TodoListNotFoundException    if TodoList with specified ID was not found
         * @throws AuthorizationFailedException if user token expired or
         *                                      user has no permission to modify to-do list with given ID
         */
        @Override
        HttpResponse process(RequestData requestData, Token token) {
            TaskId taskId = extractTaskId(requestData.getRequestParams());

            Task task = todoService.findTask(taskId)
                                   .authorizedWith(token)
                                   .execute();

            return HttpResponse.ok(task);
        }
    }

    /**
     * Handles create {@code Task} request.
     *
     * @author Oleg Barmin
     */
    static class CreateTaskRequestHandler extends SecuredAbstractRequestHandler {

        private final TodoService todoService;

        /**
         * Creates {@code CreateTaskRequestHandler} instance.
         *
         * @param todoService service to work with
         */
        CreateTaskRequestHandler(TodoService todoService) {
            this.todoService = checkNotNull(todoService);
        }

        /**
         * Creates task with ID specified in URL parameters in to-do list with ID specified in URL parameters.
         *
         * @param requestData data of task create request
         * @param token       token of user who sent request
         * @return answer with status code 200 if task creating was performed successfully
         * @throws TaskNotFoundException        if task with given ID was not found
         * @throws TodoListNotFoundException    if TodoList with specified ID was not found
         * @throws AuthorizationFailedException if user token expired or
         *                                      user has no permission to modify to-do list with given ID
         */
        @Override
        HttpResponse process(RequestData requestData, Token token) {
            RequestBody body = requestData.getRequestBody();

            if (body.isEmpty()) {
                return HttpResponse.internalError();
            }

            TaskId taskId = extractTaskId(requestData.getRequestParams());
            TodoListId todoListId = extractTodoListId(requestData.getRequestParams());

            CreateTaskPayload payload = body.as(CreateTaskPayload.class);

            String taskDescription = payload.getTaskDescription();

            todoService.addTask(taskId)
                       .authorizedWith(token)
                       .withTodoListId(todoListId)
                       .withDescription(taskDescription)
                       .execute();

            return HttpResponse.ok();
        }
    }

    /**
     * Handles update {@code Task} request.
     *
     * @author Oleg Barmin
     */
    static class UpdateTaskRequestHandler extends SecuredAbstractRequestHandler {

        private final TodoService todoService;

        /**
         * Creates {@code UpdateTaskRequestHandler} instance.
         *
         * @param todoService service to work with
         */
        UpdateTaskRequestHandler(TodoService todoService) {
            this.todoService = checkNotNull(todoService);
        }

        /**
         * Updates task with ID specified in URL parameters from to-do list wih ID specified in URL parameters.
         *
         * @param requestData data of task update request
         * @param token       token of user who sent request
         * @return answer with status code 200 if task updating was performed successfully
         * @throws TaskNotFoundException        if task with given ID was not found
         * @throws TodoListNotFoundException    if TodoList with specified ID was not found
         * @throws AuthorizationFailedException if user token expired or
         *                                      user has no permission to modify to-do list with given ID
         */
        @SuppressWarnings("ResultOfMethodCallIgnored") // watch inside method
        @Override
        HttpResponse process(RequestData requestData, Token token) {
            RequestBody body = requestData.getRequestBody();

            if (body.isEmpty()) {
                return HttpResponse.internalError();
            }

            TaskUpdatePayload payload = body.as(TaskUpdatePayload.class);

            TaskId taskId = extractTaskId(requestData.getRequestParams());
            String taskDescription = payload.getTaskDescription();
            boolean taskStatus = payload.isTaskStatus();

            todoService.updateTask(taskId)
                       .authorizedWith(token)
                       .withDescription(taskDescription)
                       .setStatus(taskStatus)
                       .execute();

            return HttpResponse.ok();
        }
    }

    /**
     * Handles remove {@code Task} request.
     */
    static class RemoveTaskRequestHandler extends SecuredAbstractRequestHandler {

        private final TodoService todoService;

        /**
         * Creates {@code RemoveTaskRequestHandler} instance.
         *
         * @param todoService service to work with
         */
        RemoveTaskRequestHandler(TodoService todoService) {
            this.todoService = checkNotNull(todoService);
        }

        /**
         * Remove task with ID specified in URL parameters from to-do list with ID specified in URl parameters.
         *
         * @param requestData data of remove task request
         * @param token       token of user who sent request
         * @return answer with status code 200 if task removal was performed successfully
         * @throws TaskNotFoundException        if task with given ID was not found
         * @throws TodoListNotFoundException    if TodoList with specified ID was not found
         * @throws AuthorizationFailedException if user token expired or
         *                                      user has no permission to modify to-do list with given ID
         */
        @Override
        HttpResponse process(RequestData requestData, Token token) {
            TaskId taskId = extractTaskId(requestData.getRequestParams());

            todoService.removeTask(taskId)
                       .authorizedWith(token)
                       .execute();

            return HttpResponse.ok();
        }
    }
}
