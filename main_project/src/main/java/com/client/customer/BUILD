load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
    name = "customer_app",
    srcs = ["CustomerApp.java"],
    main_class = "com.client.CustomerApp",
    deps = ["//src/main/java/com/proto:common_java_proto"],
)
