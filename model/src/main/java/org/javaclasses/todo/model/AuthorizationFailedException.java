package org.javaclasses.todo.model;

/**
 * Occurs when trying to validate {@code Token} which does not exists in the system.
 *
 * @author Oleg Barmin
 */
@SuppressWarnings("WeakerAccess") // part of public API should be public
public class AuthorizationFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates {@code AccessDeniedException} instance.
     *
     * @param token token which validation was failed.
     */
    public AuthorizationFailedException(Token token) {
        super("Session for given token: '" + token.getValue() + "' does not exists.");
    }

    /**
     * Creates {@code AuthorizationFailedException} instance.
     *
     * @param userId     ID of the user who tried to access content
     * @param todoListId ID of to-do list which attempted to access
     */
    AuthorizationFailedException(UserId userId, TodoListId todoListId) {
        super(String.format("User with ID '%s' has no authority to modify TodoList with ID: '%s'",
                            userId, todoListId));
    }

}
