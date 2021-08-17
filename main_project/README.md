# Overview

This project is a model of a distributed system which has a bunch of entities that communicate to each other in order to maintain a database of tasks destined to each customer.

We have two types of entities, the `customer` and the `admin`. Each one has a `client` and a `server` application, that communicate to each other in order to maintain the shared database (everything must talk to each other remotely using the network).

The applications to be developed are:
- `AdminApp`: client application for the admins
- `CustomerApp`: client application for the customers
- `AdminPortal`: server application for the admins
- `CustomerPortal`: server application for the customers

For the communication between `AdminApp` and `AdminPortal` we use `gRPC`, while for the `CustomerApp` and `CustomerPortal` we use `sockets` directly.

# Dependencies

To build the project we've chosen to use the Google's compiler `Bazel`. All the dependencies needed are listed below:

1. install [bazel](https://docs.bazel.build/versions/main/install.html)
2. install Java JDK (you can skip this if you already have some Java JDK installed in your machine).

# Execution

Execute the server and the client on different terminals to see they exchanging messages.

## Servers
On folder `src/main/java/com/server` run:
- `bazel run customer:portal` to start the `CustomerPortal` application.
- `bazel run admin:portal` to start the `AdminPortal` application.

## Client
On folder `src/main/java/com/client` run:
- `bazel run customer:app` to start the `CustomerApp` application.
- `bazel run admin:app` to start the `AdminApp` application.

The applications on client side (`CustomerApp` and `AdminApp`) should print on the screen the message received from the respective server.
