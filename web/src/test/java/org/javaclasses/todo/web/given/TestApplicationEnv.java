package org.javaclasses.todo.web.given;

import org.javaclasses.todo.model.entity.Token;

import static org.javaclasses.todo.web.given.PortProvider.getAvailablePort;

/**
 * Environment to test {@link TestTodoListApplication}, which starts and stops server with application,
 * simplifies user registration and sign in.
 *
 * @author Oleg Barmin
 */
public class TestApplicationEnv {

    private final TestTodoListApplication testTodoListApplication;
    private final int port;

    /**
     * Creates {@code TestApplicationEnv} instance.
     */
    public TestApplicationEnv() {
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
     * Registers given user in the system.
     *
     * @param user user to register
     */
    public void registerUser(SampleUser user) {
        testTodoListApplication.getAuthentication()
                               .createUser(user.getUsername(), user.getPassword());
    }

    /**
     * Signs in given user into the system.
     *
     * @param user user to sign in
     * @return token of user session
     */
    public Token signInUser(SampleUser user) {
        return testTodoListApplication.getAuthentication()
                                      .signIn(user.getUsername(), user.getPassword());
    }

}
