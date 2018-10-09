# To-do list application

To-do list application is an application which allows you to manage your tasks in web interface

### Application allows end-users to:
- Sign-in into the system.
- Create new to-do lists.
- Add tasks to create new tasks in to-do lists.
- Mark tasks as completed.
- Update created tasks.

###  Modules
1. **Model**
  responsible for all business logic of application, 
  connection with database (currently in memory database).
2. **Web**
 depends on **Model** module and responsible for REST API of the application.
3. **JS**
 responsible for client side of application. It is rich client, so it 
manages to-do lists and tasks on its own and then uploads it to the server (currently un implemented).
 
### Prerequisites
* [JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or higher.
* [npm](https://www.npmjs.com/) - to build JS module.

### Installation
Before work with application you have to resolve all Gradle dependencies:
```sh
$ ./gradlew clean build
```

And npm dependencies:
```sh
$ npm install
```

### Running
To run server you have to perform two steps:

#####First step: build jar.
To build jar you have to execute following command.
```sh
$ ./gradlew buildJar
```
It will be build into build/jar

#####Second step: run build jar.
To start server with default setting (no preregistered users) you have to execute following command:
```sh
$ java -jar build/jar/todo-list-barmin-1.0-SNAPSHOT.jar
```
Server will start without users and on port 4567.

If you want to run server with preregistered user and on desired port run:
```sh
$ java -Dtodo.username=<username> -Dtodo.password=<password> -Dtodo.port=<port> -jar build/jar/todo-list-barmin-1.0-SNAPSHOT.jar
```
