package cloudnotes.client;

import java.net.*;
import java.time.LocalTime;
import java.io.ObjectInputStream;
import java.util.Scanner;

import cloudnotes.proto.NotesResponse;

public class UserApp {
  public static void main(String args[]) {
    try {
      int port = new Scanner(args[1]).nextInt();
      Socket server = new Socket("localhost", port);
      // System.out.println("Time: " + LocalTime.now());
      InputHandler.getRequest().writeDelimitedTo(server.getOutputStream());
      // server.shutdownOutput();
      System.out.println(
        "Status message received: " + 
        NotesResponse.parseDelimitedFrom(server.getInputStream())
          .getStatus()
          .getType()
          .getValueDescriptor()
          .getName());
      server.close();
    } catch (Exception e) {
      System.out.println("Exception on client. Message: " + e.toString());
    }

  }
}
