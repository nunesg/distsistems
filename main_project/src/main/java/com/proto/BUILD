load("@rules_java//java:defs.bzl", "java_proto_library")
load("@rules_proto//proto:defs.bzl", "proto_library")
load("@rules_proto_grpc//java:defs.bzl", "java_grpc_library")

package(default_visibility = ["//visibility:public"])

proto_library(
    name = "common_proto",
    srcs = ["Common.proto"],
)

proto_library(
    name = "admin_portal_proto",
    srcs = ["AdminPortal.proto"],
)

java_proto_library(
    name = "common_java_proto",
    deps = [":common_proto"],
)

java_grpc_library(
    name = "admin_portal_grpc_proto",
    deps = [":admin_portal_proto"],
)
