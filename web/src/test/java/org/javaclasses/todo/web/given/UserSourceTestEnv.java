package org.javaclasses.todo.web.given;

import org.javaclasses.todo.model.Password;
import org.javaclasses.todo.model.entity.Username;

/**
 * Source of {@link SampleUser}s to test to-do list application with them.
 *
 * <p>Each pre-defined user has password, unique username and list of tasks with valid descriptions.
 *
 * @author Oleg Barmin
 */
public class UserSourceTestEnv {

    private UserSourceTestEnv() {
    }

    private static final SampleUser bob = new SampleUser(
            new Username("bob"),
            new Password("bob1234"),
            "buy apple.",
            "wash apple.",
            "sell apple."
    );

    private static final SampleUser alice = new SampleUser(
            new Username("alice"),
            new Password("qwerty123456"),
            "get up early.",
            "eat my breakfast.",
            "go to the gym.",
            "get the things done."
    );

    public static SampleUser getBob() {
        return bob;
    }

    public static SampleUser getAlice() {
        return alice;
    }

}
