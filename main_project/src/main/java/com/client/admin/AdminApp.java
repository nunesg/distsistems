package com.client;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import com.server.AdminPortalGrpc;
import com.server.AdminPortalGrpc.AdminPortalBlockingStub;
import com.server.HelloRequest;
import com.server.HelloReply;

public class AdminApp {
  private static final int PORT_NUMBER = 12340;
  private static final String HOST_ADDRESS = "localhost";
  private static final String TARGET = HOST_ADDRESS + ":" + PORT_NUMBER;

  private final AdminPortalBlockingStub blockingStub;

  public AdminApp(Channel channel) {
    blockingStub = AdminPortalGrpc.newBlockingStub(channel);
  }

  public void sayHello() {
    HelloRequest req = HelloRequest.newBuilder().setName("world").build();
    HelloReply reply = blockingStub.sayHello(req);
    System.out.println("Reply: " + reply.getMessage());
  }

  public static void main(String[] args) {
    ManagedChannel channel = ManagedChannelBuilder.forTarget(TARGET).usePlaintext().build();
    AdminApp client = new AdminApp(channel);

    try {
      client.sayHello();
    } finally {
      channel.shutdown();
    }
  }
}
