load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
    name = "portal",
    srcs = ["AdminPortalServer.java"],
    main_class = "com.server.AdminPortalServer",
    deps = ["//src/main/java/com/proto:admin_portal_grpc_proto"],
)
