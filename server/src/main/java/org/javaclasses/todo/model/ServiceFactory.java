package org.javaclasses.todo.model;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.storage.impl.AuthSessionStorage;
import org.javaclasses.todo.storage.impl.TaskStorage;
import org.javaclasses.todo.storage.impl.TodoListStorage;
import org.javaclasses.todo.storage.impl.UserStorage;

/**
 * Provides services of TodoList application.
 *
 * <p>Implementation of <a href="https://en.wikipedia.org/wiki/Singleton_pattern">singleton</a> pattern.
 */
// part of public API should remain public.
// service classes cannot be serialized
@SuppressWarnings({"NonSerializableFieldInSerializableClass", "WeakerAccess"})
public class ServiceFactory {

    private ServiceFactory() {
    }

    /**
     * Provides instance of {@link TodoService}.
     *
     * @return instance of {@code TodoService}
     */
    public static TodoService getTodoService() {
        return TodoServiceSingleton.INSTANCE.value;
    }

    /**
     * Provides instance of {@link Authentication}.
     *
     * @return instance of {@code Authentication}
     */
    public static Authentication getAuthentication() {
        return AuthenticationSingleton.INSTANCE.value;
    }

    private enum AuthenticationSingleton {
        INSTANCE;

        private final Authentication value;

        AuthenticationSingleton() {
            UserStorage userStorage = new UserStorage();
            AuthSessionStorage authSessionStorage = new AuthSessionStorage();

            this.value = new Authentication(userStorage, authSessionStorage);
        }
    }

    private enum TodoServiceSingleton {
        INSTANCE;

        private final TodoService value;

        TodoServiceSingleton() {
            TaskStorage taskStorage = new TaskStorage();
            TodoListStorage todoListStorage = new TodoListStorage();

            this.value = new TodoService(AuthenticationSingleton.INSTANCE.value, todoListStorage, taskStorage);
        }
    }

}