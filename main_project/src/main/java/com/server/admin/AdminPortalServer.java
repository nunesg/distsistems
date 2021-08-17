package com.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.net.*;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class AdminPortalServer {
  private static final int PORT_NUMBER = 12340;

  private final int port;
  private final Server server;

  public AdminPortalServer(int port) {
    this(ServerBuilder.forPort(port), port);
  }

  public AdminPortalServer(ServerBuilder<?> serverBuilder, int port) {
    this.port = port;
    this.server = serverBuilder.addService(new AdminPortalService()).build();
  }

  public void start() throws IOException {
    server.start();
    System.out.println("Listening on port " + this.port);
  }

  public void stop() throws InterruptedException {
    if (server != null) {
      server.shutdown();
    }
  }

  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  public static void main(String[] args) throws Exception {
    AdminPortalServer portalAdminServer = new AdminPortalServer(PORT_NUMBER);
    portalAdminServer.start();
    portalAdminServer.blockUntilShutdown();
  }

  private static class AdminPortalService extends AdminPortalGrpc.AdminPortalImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
      System.out.println("get request from " + request.getName());
      HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName() + "!").build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }
  }

}
