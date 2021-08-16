load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
    name = "customer_portal",
    srcs = ["CustomerPortal.java"],
    main_class = "com.server.CustomerPortal",
    deps = ["//src/main/java/com/proto:common_java_proto"],
)
