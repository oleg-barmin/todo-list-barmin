package org.javaclasses.todo.auth;

import org.javaclasses.todo.model.*;
import org.javaclasses.todo.storage.impl.AuthSessionStorage;
import org.javaclasses.todo.storage.impl.UserStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * @author Oleg Barmin
 */
@DisplayName("Authentication should")
class AuthenticationTest {
    private final AuthSessionStorage authSessionStorage = new AuthSessionStorage();
    private final UserStorage userStorage = new UserStorage();
    private final Authentication authentication = new Authentication(userStorage, authSessionStorage);


    private final Username username = new Username("example@mail.org");
    private final Password password = new Password("t24h6RSz7");

    @Test
    @DisplayName("create new users.")
    void testCreateUser() {
        UserId userId = authentication.createUser(username, password);

        Optional<User> optionalUser = userStorage.read(userId);

        if (!optionalUser.isPresent()) {
            Assertions.fail("New user should be created in user storage, but he didn't.");
        }

        User user = optionalUser.get();

        Assertions.assertEquals(username, user.getUsername(),
                "created user in storage should have equal username.");
        Assertions.assertEquals(password, user.getPassword(),
                "created user in storage should have equal password.");
    }


    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @DisplayName("throw UserAlreadyExistsException.")
    void testCreateUserWithExistingUsername() {
        authentication.createUser(username, password);
        Password secondUserPassword = new Password("example1966");

        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> authentication.createUser(username, secondUserPassword));
    }


    @Test
    @DisplayName("sign in registered users.")
    void testSignIn() {
        UserId userId = authentication.createUser(username, password);
        Token token = authentication.signIn(username, password);

        Optional<AuthSession> optional = authSessionStorage.read(token);

        if (!optional.isPresent()) {
            Assertions.fail("AuthSession should be saved in storage, but it don't.");
        }

        AuthSession authSession = optional.get();
        UserId storedUserId = authSession.getUserId();

        Assertions.assertEquals(userId, storedUserId);

    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @DisplayName("throw InvalidCredentialsException if try to sign in with invalid credentials.")
    void testSignInInvalidCredentials() {
        authentication.createUser(username, password);

        Password invalidPassword = new Password("invalid password");

        Assertions.assertThrows(InvalidCredentialsException.class,
                () -> authentication.signIn(username, invalidPassword));
    }


    @Test
    @DisplayName("sign out users.")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void testSignOut() {
        authentication.createUser(username, password);
        Token token = authentication.signIn(username, password);

        authentication.signOut(token);

        Optional<AuthSession> optionalAuthSession = authSessionStorage.read(token);

        if (optionalAuthSession.isPresent()) {
            Assertions.fail("On sign out authSession with user token should be removed from storage, but it don't.");
        }
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @DisplayName("throw no exception if sign out with expired token.")
    void testSignOutWithExpiredToken() {
        authentication.createUser(username, password);
        Token token = authentication.signIn(username, password);

        authentication.signOut(token);
        authentication.signOut(token);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @DisplayName("throw AccessDeniedException if token to validate expired.")
    void testValidate() {
        authentication.createUser(username, password);
        Token token = authentication.signIn(username, password);
        authentication.signOut(token);

        Assertions.assertThrows(AuthorizationFailedException.class, () -> authentication.validate(token));
    }
}