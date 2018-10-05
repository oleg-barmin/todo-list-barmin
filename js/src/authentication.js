/**
 * Signs in users by their usernames and passwords.
 *
 * @author Oleg Barmin
 */
export class Authentication {

    /**
     * Creates `Authentication` instance.
     */
    constructor() {
        this.token = null;
    }


    /**
     * Returns `Promise` which signs in user with given username and password.
     *
     * <p>If given credentials is valid promise will be resolved and
     * token of user session will be stored, otherwise
     * AuthenticationFailedException will be thrown inside promise.
     *
     * @param {String} username username of user who tries to sign-in
     * @param {String} password password of user who tries to sign-in
     * @returns {Promise} to work with
     */
    signIn(username, password) {
        return new Promise((resolve) => {
            const usernameAndPassword = username + ":" + password;
            const encodedCredentials = btoa(usernameAndPassword);

            const xmlHttpRequest = new XMLHttpRequest();

            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    this.token = JSON.parse(xmlHttpRequest.response);
                    resolve()
                } else {
                    throw new AuthenticationFailedException();
                }
            };

            xmlHttpRequest.open("POST", "/auth");
            xmlHttpRequest.setRequestHeader("Authentication", "Basic " + encodedCredentials);
            xmlHttpRequest.send();
        });
    }
}

/**
 * Occurs when user typed in invalid username or password during authentication.
 *
 * @author Oleg Barmin
 */
export class AuthenticationFailedException extends Error {

    /**
     * Creates `AuthenticationFailedException` instance.
     */
    constructor() {
        super("Authentication failed.")
    }

}

