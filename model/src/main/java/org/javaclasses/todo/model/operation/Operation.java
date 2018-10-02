package org.javaclasses.todo.model.operation;

import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.entity.Token;
import org.javaclasses.todo.model.entity.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstract operation which allows to validate user token.
 *
 * @param <O> sub-class of operation
 * @author Oleg Barmin
 */
//class has no meaning in TodoList application without sub-class which should provide business logic of operation.
@SuppressWarnings("AbstractClassWithoutAbstractMethods")
abstract class Operation<O extends Operation<O>> {
    private final Authentication authentication;
    private Token token;

    /**
     * Creates {@code Operation} instance.
     *
     * @param authentication service to validate user token.
     */
    Operation(Authentication authentication) {
        this.authentication = checkNotNull(authentication);
    }

    /**
     * Stores token to validate.
     *
     * @param token user token
     * @return this operation instance
     */
    // Casts this to generic type to provide type covariance in the derived classes.
    @SuppressWarnings("unchecked")
    public O authorizedWith(Token token) {
        this.token = checkNotNull(token);
        return (O) this;
    }

    /**
     * Validates stored user {@code token}.
     *
     * @return ID of user to which {@code token} relate\
     * @throws AuthorizationFailedException if given token is not valid
     */
    UserId validateToken() {
        checkNotNull(token);
        return authentication.validate(token);
    }
}
