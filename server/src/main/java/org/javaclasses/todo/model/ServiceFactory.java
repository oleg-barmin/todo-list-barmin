package org.javaclasses.todo.model;

import com.google.common.annotations.VisibleForTesting;
import org.javaclasses.todo.auth.Authentication;

import static org.javaclasses.todo.model.StorageFactory.*;

/**
 * Provides services of TodoList application.
 *
 * <p>Implementation of <a href="https://en.wikipedia.org/wiki/Singleton_pattern">singleton</a> pattern.
 *
 * @author Oleg Barmin
 * @implNote all services are lazy initialized
 */
@SuppressWarnings({
        "NonSerializableFieldInSerializableClass", // service classes cannot be serialized
        "WeakerAccess" // part of public API should remain public.
})
public class ServiceFactory {

    private ServiceFactory() {
    }

    @VisibleForTesting
    public static void clearStorages() {
        getAuthSessionStorage().clear();
        getTaskStorage().clear();
        getTodoListStorage().clear();
        getUserStorage().clear();
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
            this.value = new Authentication(getUserStorage(), getAuthSessionStorage());
        }
    }

    private enum TodoServiceSingleton {
        INSTANCE;

        private final TodoService value;

        TodoServiceSingleton() {
            this.value = new TodoService(AuthenticationSingleton.INSTANCE.value, getTodoListStorage(), getTaskStorage());
        }
    }

}