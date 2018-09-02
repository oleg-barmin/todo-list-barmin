/**
 * Generates uuid v4 ID strings.
 */
export class IdGenerator {

    /**
     * Generates uuid v4 IDs.
     *
     * @returns {string} ID generated uuid v4 ID.
     */
    static generateID() {
        if (typeof(require) !== 'undefined') {
            return require('uuid/v4')();
        }
        return uuidv4();
    }
}