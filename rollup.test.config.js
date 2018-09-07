export default [{
    input: 'test/model/todo-test.js',
    output: {
        file: 'test/model/todo-test-bundle.js',
        format: 'umd',
    },
}, {
    input: 'test/event/even-bus-test.js',
    output: {
        file: 'test/event/even-bus-test-bundle.js',
        format: 'umd',
    },
}, {
    input: 'test/controller-test.js',
    output: {
        file: 'test/controller-test-bundle.js',
        format: 'umd',
    },
}];