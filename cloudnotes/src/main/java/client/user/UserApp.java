package cloudnotes.client;

import java.net.*;
import java.time.LocalTime;
import java.io.ObjectInputStream;

import cloudnotes.proto.NotesResponse;

public class UserApp {
  private static final int PORT_NUMBER = 12345;

  public static void main(String[] args) {
    try {
      Socket server = new Socket("localhost", PORT_NUMBER);
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
