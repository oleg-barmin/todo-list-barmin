# To-do list application

To-do list application is an application which allows you to manage your tasks in web interface

### Application allows end-users to:
- Sign-in into the system.
- Create new to-do lists.
- Add tasks to create new tasks in to-do lists.
- Mark tasks as completed.
- Update uncompleted tasks with new description.

###  Modules
1. **Model**
  Responsible for all business logic of application, 
  connection with database (currently in memory database).
2. **Web**
 Depends on **Model** module and responsible for REST API of the application.
3. **JS**
 Responsible for client side of application. It is rich client, so it 
manages to-do lists and tasks on its own and then uploads it to the server (currently un implemented).
 
### Prerequisites
* [JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or higher.
* [npm](https://www.npmjs.com/) - to build JS module.

### Installation
Before work with application you have to resolve all npm dependencies:
```sh
$ npm build
```
and Gradle dependencies.
```sh
$ gradle build
```

### Running
To run server you have to execute following command:
```sh
$ gradle run --args <port>
```
If port number was not provided application will start on port 4567.
