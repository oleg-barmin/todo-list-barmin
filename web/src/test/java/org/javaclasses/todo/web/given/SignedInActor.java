package org.javaclasses.todo.web.given;

import org.javaclasses.todo.model.Token;

/**
 * Represents a user which was registered and signed in into the system.
 *
 * @author Oleg Barmin
 */
public class SignedInActor extends Actor {

    private final Token token;

    /**
     * Creates {@code SignedInActor} instance from {@link Actor} instance with given {@link Token}.
     *
     * @param actor actor to create instance from
     * @param token token in the system
     */
    SignedInActor(Actor actor, Token token) {
        super(actor.getUserId(), actor.getUsername(), actor.getPassword());
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
