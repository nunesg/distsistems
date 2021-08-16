package com.server;

import java.net.*;
import java.io.ObjectOutputStream;

public class ClientServer {
  public static void main(String[] args) {

    try {
      ServerSocket server = new ServerSocket(12345);
      System.out.println("Server listening on port 12345.");

      try {

        while (true) {
          Socket client = server.accept();
          System.out.println("Client connected on port 12345!");

          ObjectOutputStream stream = new ObjectOutputStream(client.getOutputStream());
          stream.flush();
          stream.writeObject("Hi client!");
          stream.close();
          client.close();
        }
      } catch (Exception e) {
        System.out.println("Server error. Message: " + e.getMessage());
      }

      server.close();
    } catch (Exception e) {
      System.out.println("Error trying to setup server on port 12345");
    }
  }
}
