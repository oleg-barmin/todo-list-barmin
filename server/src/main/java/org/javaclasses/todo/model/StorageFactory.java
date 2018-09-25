package org.javaclasses.todo.model;

import org.javaclasses.todo.storage.impl.AuthSessionStorage;
import org.javaclasses.todo.storage.impl.TaskStorage;
import org.javaclasses.todo.storage.impl.TodoListStorage;
import org.javaclasses.todo.storage.impl.UserStorage;

/**
 * Provides storage for services of TodoList application.
 *
 * <p>Implementation of <a href="https://en.wikipedia.org/wiki/Singleton_pattern">singleton</a> pattern.
 *
 * @author Oleg Barmin
 * @implNote all services are lazy initialized
 */
/* Storage cannot not be serialized */
@SuppressWarnings("NonSerializableFieldInSerializableClass")
class StorageFactory {
    private AuthSessionStorage authSessionStorage = null;
    private TaskStorage taskStorage = null;
    private TodoListStorage todoListStorage = null;
    private UserStorage userStorage = null;

    /**
     * Provides instance of {@link AuthSessionStorage}.
     *
     * @return instance of {@code AuthSessionStorage}
     */
    synchronized AuthSessionStorage getAuthSessionStorage() {
        if (authSessionStorage == null) {
            authSessionStorage = new AuthSessionStorage();
        }
        return authSessionStorage;
    }

    /**
     * Provides instance of {@link TaskStorage}.
     *
     * @return instance of {@code TaskStorage}
     */
    synchronized TaskStorage getTaskStorage() {
        if (taskStorage == null) {
            taskStorage = new TaskStorage();
        }
        return taskStorage;
    }

    /**
     * Provides instance of {@link TodoListStorage}.
     *
     * @return instance of {@code TodoListStorage}
     */
    synchronized TodoListStorage getTodoListStorage() {
        if (todoListStorage == null) {
            todoListStorage = new TodoListStorage();
        }
        return todoListStorage;
    }

    /**
     * Provides instance of {@link UserStorage}.
     *
     * @return instance of {@code UserStorage}
     */
    synchronized UserStorage getUserStorage() {
        if (userStorage == null) {
            userStorage = new UserStorage();
        }
        return userStorage;
    }
}
