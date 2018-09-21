# Bank Sample

A sample project for bank accounts and transactions. 


## Introduction

Create a program to simulate financial transaction, it must be manage
the following accounts:

**Savings account**:

This account has the following restrictions:
- It has a handling fee of $2000 COP
- Charges from the third transaction
- The percent charged per transaction is 3%

**Current account**:

This account has the following restrictions:
- It has a handling fee of $3000 COP
- It doesn't have free transactions
- The percent charged per transaction is 3%

**Kids savings account**:

This account has the following benefits:
- It doesn't have handling fee
- There isn't charges per transaction

And just have a restriction:
- This account is enabled only for users under 14 years old


## Getting Started

To start you need install [JDK 8 or greater](http://openjdk.java.net/)
on your local machine.


## Development Time

The project use [Gradle](https://gradle.org/) as a manager and to automate 
tasks. Use `build` gradle task to download local dependencies and compile 
the project and use `clean` to clean it.

For clean and compile the project:
```
./gradlew clean build
```

### Running Server
This project uses gretty to mount the API and be able to make the 
corresponding requests locally.
```
./gradlew run
```
After the server is running, you can access the project through the browser 
in the next url (http://localhost:8080/api.hulkstore).

### Running Tests
The tests in this project are make with JUnit. You need use the 
following maven task:
```
./gradlew test
```

## Deployment
Just build the project you can get the packaged file (*.war) in the folder 
`/build/libs`, with the name of the project and the corresponding version.

But if in some case you are not able to run the following command and 
generate the file.
```
./gradlew assemble
```

All development rights are reserved by **Gustavo Pacheco**.