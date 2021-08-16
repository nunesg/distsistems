package com.server;

import java.net.*;
import java.io.ObjectOutputStream;

import com.proto.CommonMessage;

public class CustomerPortal {
  public static void main(String[] args) {

    try {
      ServerSocket server = new ServerSocket(12345);
      System.out.println("Server listening on port 12345.");

      try {

        while (true) {
          Socket client = server.accept();
          System.out.println("Client connected on port 12345!");
          CommonMessage.newBuilder().setContent("Hi client!").build().writeTo(client.getOutputStream());
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
