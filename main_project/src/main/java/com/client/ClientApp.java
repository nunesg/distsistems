package com.client;

import java.net.*;
import java.io.ObjectInputStream;

public class ClientApp {
  public static void main(String[] args) {

    try {
      Socket server = new Socket("localhost", 12345);
      ObjectInputStream stream = new ObjectInputStream(server.getInputStream());
      String message = stream.readObject().toString();

      System.out.println("Message received: " + message);
      server.close();
    } catch (Exception e) {
      System.out.println("Exception on client. Message: " + e.toString());
    }

  }
}
