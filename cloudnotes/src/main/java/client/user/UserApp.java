package cloudnotes.client;

import java.net.*;
import java.time.LocalTime;
import java.io.ObjectInputStream;

import cloudnotes.proto.CommonMessage;

public class UserApp {
  private static final int PORT_NUMBER = 12345;

  public static void main(String[] args) {

    try {
      Socket server = new Socket("localhost", PORT_NUMBER);
      System.out.println("Time: " + LocalTime.now());
      CommonMessage.newBuilder()
        .setContent("Hello from time: " + LocalTime.now())
        .build()
        .writeTo(server.getOutputStream());
      // server.shutdownOutput();
      // System.out.println("Message received: " + CommonMessage.parseFrom(server.getInputStream()).getContent());
      server.close();
    } catch (Exception e) {
      System.out.println("Exception on client. Message: " + e.toString());
    }

  }
}
