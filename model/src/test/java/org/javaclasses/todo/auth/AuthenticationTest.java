package org.javaclasses.todo.auth;

import org.javaclasses.todo.model.AuthorizationFailedException;
import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.entity.AuthSession;
import org.javaclasses.todo.model.entity.Token;
import org.javaclasses.todo.model.entity.User;
import org.javaclasses.todo.model.entity.UserId;
import org.javaclasses.todo.model.entity.Username;
import org.javaclasses.todo.storage.impl.AuthSessionStorage;
import org.javaclasses.todo.storage.impl.UserStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Testing {@link Authentication} service which should validate user {@link Token} and allow user to:
 * - sign-in into the system;
 * - register in the system;
 * - sign-out from the system.
 *
 * @author Oleg Barmin
 */
@DisplayName("Authentication should")
class AuthenticationTest {

    private final AuthSessionStorage authSessionStorage = new AuthSessionStorage();
    private final UserStorage userStorage = new UserStorage();
    private final Authentication authentication = new Authentication(userStorage,
                                                                     authSessionStorage);

    private final Username username = new Username("example@mail.org");
    private final Password password = new Password("t24h6RSz7");

    @Test
    @DisplayName("create new users.")
    void testCreateUser() {
        authentication.createUser(username, password);

        Optional<User> optionalUser = userStorage.findBy(username);

        if (!optionalUser.isPresent()) {
            Assertions.fail("create new user in user storage, but he didn't.");
        }

        User user = optionalUser.get();

        Assertions.assertEquals(username, user.getUsername(),
                                "save user with equal username, but it don't.");
        Assertions.assertEquals(password, user.getPassword(),
                                "save user with equal password, but it don't.");
    }

    @Test
    @DisplayName("throw UserAlreadyExistsException.")
    void testCreateUserWithExistingUsername() {
        authentication.createUser(username, password);
        Password secondUserPassword = new Password("example1966");

        Assertions.assertThrows(UserAlreadyExistsException.class,
                                () -> authentication.createUser(username, secondUserPassword));
    }

    @Test
    @DisplayName("throw EmptyCredentialsException if try to create user with empty username or password.")
    void testCreateUserWithEmptyCredentials() {
        Username emptyUsername = new Username("");
        Password emptyPassword = new Password("");

        Assertions.assertThrows(EmptyCredentialsException.class,
                                () -> authentication.createUser(username, emptyPassword));

        Assertions.assertThrows(EmptyCredentialsException.class,
                                () -> authentication.createUser(emptyUsername, password));
    }

    @Test
    @DisplayName("sign in registered users.")
    void testSignIn() {
        authentication.createUser(username, password);
        Token token = authentication.signIn(username, password);
        UserId userId = authentication.validate(token);

        Optional<AuthSession> optional = authSessionStorage.read(token);

        if (!optional.isPresent()) {
            Assertions.fail("be save AuthSession in storage, but it don't.");
        }

        AuthSession authSession = optional.get();
        UserId storedUserId = authSession.getUserId();

        Assertions.assertEquals(userId, storedUserId);

    }

    @Test
    /*
     * To test sign out token is not needed.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @DisplayName("throw InvalidCredentialsException if try to sign in with invalid credentials.")
    void testSignInInvalidCredentials() {
        authentication.createUser(username, password);

        Password invalidPassword = new Password("invalid password");

        Assertions.assertThrows(InvalidCredentialsException.class,
                                () -> authentication.signIn(username, invalidPassword));
    }

    @Test
    /*
     * To test sign out token is not needed.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @DisplayName("throw EmptyCredentialsException if try to sign in user with empty username or password.")
    void tesSignInUserWithEmptyCredentials() {
        Username emptyUsername = new Username("");
        Password emptyPassword = new Password("");

        Assertions.assertThrows(EmptyCredentialsException.class,
                                () -> authentication.signIn(username, emptyPassword));

        Assertions.assertThrows(EmptyCredentialsException.class,
                                () -> authentication.signIn(emptyUsername, password));
    }

    @Test
    @DisplayName("sign out users.")
    void testSignOut() {
        authentication.createUser(username, password);
        Token token = authentication.signIn(username, password);

        authentication.signOut(token);

        Optional<AuthSession> optionalAuthSession = authSessionStorage.read(token);

        if (optionalAuthSession.isPresent()) {
            Assertions.fail(
                    "On sign out authSession with user token should be removed from storage, but it don't.");
        }
    }

    @Test
    @DisplayName("throw no exception if sign out with expired token.")
    void testSignOutWithExpiredToken() {
        authentication.createUser(username, password);
        Token token = authentication.signIn(username, password);

        authentication.signOut(token);
        authentication.signOut(token);
    }

    @Test
    @DisplayName("throw AccessDeniedException if token to validate expired.")
    void testValidate() {
        authentication.createUser(username, password);
        Token token = authentication.signIn(username, password);
        authentication.signOut(token);

        Assertions.assertThrows(AuthorizationFailedException.class,
                                () -> authentication.validate(token));
    }
}