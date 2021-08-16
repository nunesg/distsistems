package com.client;

import java.net.*;
import java.io.ObjectInputStream;

import com.proto.CommonMessage;

public class CustomerApp {
  public static void main(String[] args) {

    try {
      Socket server = new Socket("localhost", 12345);
      System.out.println("Message received: " + CommonMessage.parseFrom(server.getInputStream()).getContent());
      server.close();
    } catch (Exception e) {
      System.out.println("Exception on client. Message: " + e.toString());
    }

  }
}
