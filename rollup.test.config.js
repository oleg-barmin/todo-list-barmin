export default {
    input: 'test/todo-test.js',
    output: {
        file: 'test/todo-test-bundle.js',
        format: 'umd',
    },
    globals:{
      'jquery': 'JQuery'
    }
};