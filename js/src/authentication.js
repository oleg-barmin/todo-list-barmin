/**
 * Authenticate user by his username and password and signs out users from the system.
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
     * If given credentials is valid promise will be resolved and
     * token of user session will be stored, otherwise
     * AuthenticationFailedException will be thrown inside promise.
     *
     * @param {String} username username of user who tries to sign-in
     * @param {String} password password of user who tries to sign-in
     * @returns {Promise} to work with
     */
    signIn(username, password) {
        return new Promise((resolve, reject) => {
            const usernameAndPassword = username + ":" + password;
            const encodedCredentials = btoa(usernameAndPassword);

            const xmlHttpRequest = new XMLHttpRequest();

            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    this.token = JSON.parse(xmlHttpRequest.response).value;
                    resolve()
                } else {
                    reject(new AuthenticationFailedException());
                }
            };

            xmlHttpRequest.open("POST", "/auth");
            xmlHttpRequest.setRequestHeader("Authentication", "Basic " + encodedCredentials);
            xmlHttpRequest.send();
        });
    }

    /**
     * Returns `Promise` which signs out user with stored token.
     *
     * If sign-out performed successfully this promise will be resolved and
     * token of user session will be erased.
     *
     * @returns {Promise} to work with
     */
    signOut() {
        return new Promise((resolve, reject) => {
            const xmlHttpRequest = new XMLHttpRequest();

            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    resolve();
                    this.token = null;
                } else {
                    reject();
                }
            };

            xmlHttpRequest.open("DELETE", "/auth");
            xmlHttpRequest.setRequestHeader("X-Todo-Token", this.token);
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

