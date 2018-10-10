export default [
    {
        input: 'js/test/lib/lib-test.js',
        output: {
            file: 'js/test/lib/lib-test-bundle.js',
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
        input: 'js/test/model/todo-list-test.js',
        output: {
            file: 'js/test/model/todo-list-test-bundle.js',
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