package cloudnotes.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.net.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.Scanner;

import cloudnotes.server.UsersCacheManager;

public class RatisTest {
  public static void main(String args[]) throws Exception {
    ConfigFacade config = ConfigFacade.getInstance();
      
    Socket server = new Socket(config.getClient().getIp(), config.getClient().getPort());
    PrintWriter out = new PrintWriter(server.getOutputStream(), true);
    BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));

    out.println(String.join(" ", args));
    System.out.println(
      "Status message received: " + in.readLine());
    server.close();
  }
}
