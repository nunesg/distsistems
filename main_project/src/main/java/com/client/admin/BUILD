load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
    name = "app",
    srcs = ["AdminApp.java"],
    main_class = "com.client.AdminApp",
    deps = ["//src/main/java/com/proto:admin_portal_grpc_proto"],
)
