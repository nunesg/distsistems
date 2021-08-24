package cloudnotes.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.net.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import cloudnotes.server.UsersCacheManager;

public class AdminPortalServer {
  private final int port;
  private final Server server;

  public AdminPortalServer(int port) {
    this.port = port;
    this.server = ServerBuilder.forPort(port)
      .addService(
        new AdminPortalService(new UsersCacheManager()))
      .build();
  }

  public void start() throws IOException {
    server.start();
    System.out.println("AdminPortalServer listening on port " + this.port);
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

  public static void main(String args[]) throws Exception {
    int port = new Scanner(args[1]).nextInt();
    AdminPortalServer portalAdminServer = new AdminPortalServer(port);
    portalAdminServer.start();
    portalAdminServer.blockUntilShutdown();
  }
}
