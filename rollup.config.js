export default [
    {
        input: 'js/test/model/todo-test.js',
        output: {
            file: 'js/test/model/todo-test-bundle.js',
            format: 'umd',
        },
    },
    {
        input: 'js/test/event/even-bus-test.js',
        output: {
            file: 'js/test/event/even-bus-test-bundle.js',
            format: 'umd',
        },
    },
    {
        input: 'js/src/todo-list-app.js',
        output: {
            name: "bundle",
            file: 'js/public/js/bundle.js',
            format: 'umd',
        }
    }
];