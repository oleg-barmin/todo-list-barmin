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
        this._token = null;
        this.tokenHeader = "X-Todo-Token";
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
            const token = localStorage.getItem(this.tokenHeader);
            if (token) {
                const xmlHttpRequest = new XMLHttpRequest();
                xmlHttpRequest.onload = () => {
                    if (xmlHttpRequest.status === 200) {
                        this._token = token;
                        resolve()
                    } else {
                        localStorage.removeItem(this.tokenHeader);
                        this._token = null;
                        reject();
                    }
                };
                xmlHttpRequest.open("GET", "/auth");
                xmlHttpRequest.setRequestHeader(this.tokenHeader, token);
                xmlHttpRequest.send();
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

            const xmlHttpRequest = new XMLHttpRequest();

            xmlHttpRequest.onload = () => {
                if (xmlHttpRequest.status === 200) {
                    this._token = JSON.parse(xmlHttpRequest.response).value;
                    localStorage.setItem(this.tokenHeader, this._token);
                    resolve(this._token)
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
                    this._token = null;
                } else {
                    reject();
                }
                localStorage.removeItem(this.tokenHeader);
            };

            xmlHttpRequest.open("DELETE", "/auth");
            xmlHttpRequest.setRequestHeader(this.tokenHeader, this._token);
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

