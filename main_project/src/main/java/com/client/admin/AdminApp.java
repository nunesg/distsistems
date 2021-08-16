package com.client;

import java.net.*;
import java.io.ObjectInputStream;

import com.proto.CommonMessage;

public class AdminApp {
  private static final int PORT_NUMBER = 12340;

  public static void main(String[] args) {

    try {
      Socket server = new Socket("localhost", PORT_NUMBER);
      System.out.println("Message received: " + CommonMessage.parseFrom(server.getInputStream()).getContent());
      server.close();
    } catch (Exception e) {
      System.out.println("Exception on client. Message: " + e.toString());
    }

  }
}
