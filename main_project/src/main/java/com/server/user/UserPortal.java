package com.server;

import java.net.*;
import java.io.ObjectOutputStream;

import com.proto.CommonMessage;

public class UserPortal {
  private static final int PORT_NUMBER = 12345;

  public static void main(String[] args) {

    try {
      ServerSocket server = new ServerSocket(PORT_NUMBER);
      System.out.println("Server listening on port " + PORT_NUMBER);

      try {

        while (true) {
          Socket client = server.accept();
          System.out.println("Client connected on port " + PORT_NUMBER + "!");
          CommonMessage.newBuilder().setContent("Hi user!").build().writeTo(client.getOutputStream());
          client.close();
        }
      } catch (Exception e) {
        System.out.println("Server error. Message: " + e.getMessage());
      }

      server.close();
    } catch (Exception e) {
      System.out.println("Error trying to setup server on port " + PORT_NUMBER);
    }
  }
}
