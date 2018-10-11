/**
 * Authenticate user by his username and password and signs out users from the system.
 *
 * @author Oleg Barmin
 */
export class Authentication {

    /**
     * Creates `Authentication` instance.
     *
     * @param {Backend} backend to send requests
     */
    constructor(backend) {
        this._token = null;
        this.tokenHeader = "X-Todo-Token";
        this.tokenKey = "org.javaclasses.todo.token";
        this.backend = backend;
    }

    /**
     * Returns stored token.
     *
     * @return {string} token value
     */
    get token() {
        return this._token;
    }

    /**
     * @param value value attempted to set to token.
     * @throws Error if try to call this method.
     */
    static set token(value) {
        throw new Error("Authentication token cannot be set from outside of class.")
    }

    /**
     * Validates if session token exists in local storage,
     * if it is then sends request to validate is this token expired.
     *
     * @return {Promise} to work with, which is rejected
     * if token wasn't stored in local storage or stored token expired,
     * otherwise promise is resolved.
     */
    checkSignInUser() {
        return new Promise((resolve, reject) => {
            const token = localStorage.getItem(this.tokenKey);

            if (token) {
                this.backend.sendValidateTokenUser(token).then(() => {
                    this._token = token;
                    resolve()
                }).catch(() => {
                    localStorage.removeItem(this.tokenKey);
                    this._token = null;
                    reject();
                });
            } else {
                reject();
            }
        });
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

            return this.backend.signInUser("Authentication", `Basic ${encodedCredentials}`)
                .then((body) => {
                    this._token = JSON.parse(body).value;
                    localStorage.setItem(this.tokenKey, this._token);
                    resolve(this._token)
                }).catch(() => {
                    reject(new AuthenticationFailedException());
                });
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
        return new Promise((resolve) => {
            this.backend.signOutUser(this.token)
                .finally(() => {
                    this._token = null;
                    localStorage.removeItem(this.tokenKey);
                    resolve();
                });
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

