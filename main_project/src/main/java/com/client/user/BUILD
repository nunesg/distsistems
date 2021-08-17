load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
    name = "app",
    srcs = ["UserApp.java"],
    main_class = "com.client.UserApp",
    deps = ["//src/main/java/com/proto:common_java_proto"],
)
