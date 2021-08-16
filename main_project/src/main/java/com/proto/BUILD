load("@rules_java//java:defs.bzl", "java_proto_library")
load("@rules_proto//proto:defs.bzl", "proto_library")

package(default_visibility = ["//visibility:public"])

proto_library(
    name = "common_proto",
    srcs = ["Common.proto"],
)

java_proto_library(
    name = "common_java_proto",
    deps = [":common_proto"],
)
