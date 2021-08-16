# Dependencies
1. install [bazel](https://docs.bazel.build/versions/main/install.html)
2. install Java JDK

# Execution
## Server
On folder `src/main/java/com/server` run:
- bazel run :client_server
## Client
On folder `src/main/java/com/client` run in a different terminal:
- bazel run :client_app

The `client_app` should print on the screen the message received from the server.
