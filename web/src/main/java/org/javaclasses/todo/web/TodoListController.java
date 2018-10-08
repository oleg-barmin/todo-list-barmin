package org.javaclasses.todo.web;

import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.TodoListNotFoundException;
import org.javaclasses.todo.model.TodoService;
import org.javaclasses.todo.model.entity.Task;
import org.javaclasses.todo.model.entity.TodoList;
import org.javaclasses.todo.model.entity.TodoListId;
import org.javaclasses.todo.model.entity.Token;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.javaclasses.todo.web.Params.getTodoListIdParam;

/**
 * Processes requests which modifies {@link TodoList}.
 *
 * @author Oleg Barmin
 */
class TodoListController {

    private TodoListController() {
    }

    /**
     * Handles get user lists request.
     *
     * <p>Allows to read all {@linkplain TodoList TodoLists} of user.
     *
     * @author Oleg Barmin
     */
    static class ReadUserListsHandler extends SecuredAbstractRequestHandler {

        private final TodoService todoService;

        /**
         * Creates {@code ReadUserListsHandler} instance.
         *
         * @param todoService service to work with
         */
        ReadUserListsHandler(TodoService todoService) {
            this.todoService = todoService;
        }

        /**
         * Reads all to-do lists IDs of user.
         *
         * @param requestData data of received request
         * @param token       token of user who sent request
         * @return response with all users to-do lists.
         */
        @Override
        HttpResponse process(RequestData requestData, Token token) {

            List<TodoList> todoLists = todoService.readUserTodoLists()
                                                  .authorizedWith(token)
                                                  .execute();

            return HttpResponse.ok(todoLists);
        }
    }

    /**
     * Handles create {@code TodoList} request.
     *
     * @author Oleg Barmin
     */
    static class CreateTodoListRequestHandler extends SecuredAbstractRequestHandler {

        private final TodoService todoService;

        /**
         * Creates {@code CreateTodoListRequestHandler} instance.
         *
         * @param todoService todoService to work with
         */
        CreateTodoListRequestHandler(TodoService todoService) {
            this.todoService = checkNotNull(todoService);
        }

        /**
         * Creates to-do list with ID specified in URL parameters.
         *
         * @param requestData data of create list request
         * @param token       token of user who sent request
         * @return answer with 200 status code if list was created successfully
         * @throws AuthorizationFailedException if user token expired.
         */
        @Override
        HttpResponse process(RequestData requestData, Token token) {
            String uuid = requestData.getRequestParams()
                                     .getParamValue(getTodoListIdParam());

            TodoListId todoListId = new TodoListId(uuid);

            todoService.createList(todoListId)
                       .authorizedWith(token)
                       .execute();

            return HttpResponse.ok();
        }
    }

    /**
     * Handles read all {@code Task}s from {@code TodoList} request.
     *
     * @author Oleg Barmin
     */
    static class ReadTasksRequestHandler extends SecuredAbstractRequestHandler {
        private final TodoService todoService;

        /**
         * Creates {@code ReadTasksRequestHandler} instance.
         *
         * @param todoService todoService to work with.
         */
        ReadTasksRequestHandler(TodoService todoService) {
            this.todoService = checkNotNull(todoService);
        }

        /**
         * Reads all tasks from to-do list with ID specified in URL params.
         *
         * @param requestData data of read all tasks
         * @param token       token of user who sent request
         * @return answer with status code 200 and requested to-do list tasks
         * if reading of tasks was performed successfully
         * @throws TodoListNotFoundException    if to-do list with given ID was not found
         * @throws AuthorizationFailedException if user toke expired or
         *                                      user has no permission to read task from this list.
         */
        @Override
        HttpResponse process(RequestData requestData, Token token) {
            String todoListIdParam = requestData.getRequestParams()
                                                .getParamValue(getTodoListIdParam());

            TodoListId todoListId = new TodoListId(todoListIdParam);

            List<Task> tasks = todoService.readTasksFrom(todoListId)
                                          .authorizedWith(token)
                                          .execute();

            return HttpResponse.ok(tasks);
        }
    }
}
