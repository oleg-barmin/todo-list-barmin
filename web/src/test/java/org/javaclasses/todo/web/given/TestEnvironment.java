package org.javaclasses.todo.web.given;

import com.devskiller.jfairy.Fairy;
import org.javaclasses.todo.auth.Authentication;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.entity.Token;
import org.javaclasses.todo.model.entity.UserId;
import org.javaclasses.todo.model.entity.Username;

import static org.javaclasses.todo.web.given.PortProvider.getAvailablePort;

/**
 * Environment to test {@link TestTodoListApplication}, which starts and stops server with application,
 * simplifies user registration and sign in.
 *
 * @author Oleg Barmin
 */
public class TestEnvironment {

    /**
     * To generate usernames and passwords.
     */
    private final Fairy fairy = Fairy.create();

    private final TestTodoListApplication testTodoListApplication;
    private final int port;

    /**
     * Creates {@code TestEnvironment} instance.
     */
    public TestEnvironment() {
        port = getAvailablePort();
        testTodoListApplication = new TestTodoListApplication(port);
    }

    /**
     * Returns port of {@link TestTodoListApplication}.
     *
     * @return port of {@code TestTodoListApplication}.
     */
    public int getApplicationPort() {
        return port;
    }

    /**
     * Starts server with {@link TestTodoListApplication}.
     */
    public void startServer() {
        testTodoListApplication.start();
    }

    /**
     * Stops server with {@link TestTodoListApplication}.
     */
    public void stopServer() {
        testTodoListApplication.stop();
    }

    /**
     * Register {@link User} with random username, password into the {@link TestTodoListApplication}.
     *
     * @return instance of {@link Actor} with registered users username, password and ID.
     */
    public Actor registerUser() {
        Username username = new Username(fairy.person()
                                              .getUsername());
        Password password = new Password(fairy.person()
                                              .getPassword());

        UserId userId = testTodoListApplication.getAuthentication()
                                               .createUser(username, password);

        return new Actor(userId, username, password);
    }

    /**
     * Creates and signs in {@code User} with random username, password into the {@link TestTodoListApplication}.
     *
     * @return instance of {@link SignedInActor} with registered users username, password, ID and token.
     */
    public SignedInActor createAndSignInActor() {
        Actor actor = registerUser();

        Authentication authentication = testTodoListApplication.getAuthentication();
        Token token = authentication.signIn(actor.getUsername(), actor.getPassword());

        return new SignedInActor(actor, token);
    }

}
