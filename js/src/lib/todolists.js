/**
 * Provides static methods to clone arrays of tasks and single tasks.
 */
export class TasksClone {

    /**
     * Deep copies given `Array` of `Task`.
     *
     * @param {Array} array array to copy
     * @returns {Array} arrayCopy copy of given array
     */
    static cloneArray(array) {
        if (!(array instanceof Array)) {
            throw new TypeError("Array expected. Actual value: " + array)
        }
        let arrayCopy = [];

        for (let i = 0; i < array.length; i++) {
            let currentElement = array[i];
            if (typeof currentElement === "object") {
                arrayCopy[i] = TasksClone.cloneObject(currentElement);
                continue;
            }
            arrayCopy[i] = currentElement;
        }
        arrayCopy.__proto__ = Array.prototype;
        return arrayCopy;
    }

    /**
     * Deep copies given `Task`.
     *
     * @param {*} objectToClone object to clone
     * @returns {*} copy of given `Task`
     */
    static cloneObject(objectToClone) {
        if (typeof objectToClone !== "object") {
            return objectToClone;
        }
        let objCopy = {};

        for (let key in objectToClone) {
            let currentProperty = objectToClone[key];
            if (typeof currentProperty === "object") {
                if (currentProperty instanceof Array) {
                    objCopy[key] = TasksClone.cloneArray(currentProperty);
                    continue;
                }
                objCopy[key] = TasksClone.cloneObject(currentProperty);
                continue
            }
            objCopy[key] = currentProperty;
        }
        objCopy.__proto__ = objectToClone.constructor.prototype;
        return objCopy;
    }
}