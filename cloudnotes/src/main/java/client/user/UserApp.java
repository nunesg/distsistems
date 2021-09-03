package cloudnotes.client;

import com.google.protobuf.util.JsonFormat;
import java.net.*;
import java.time.LocalTime;
import java.io.ObjectInputStream;
import java.util.Scanner;

import cloudnotes.proto.NotesResponse;

public class UserApp {
  private static String toJson(NotesResponse entry) {
    String json = new String();
    try {
      json = JsonFormat.printer().print(entry);
    } catch (Exception e) {
      System.out.println("Error while converting message to json: " + entry.toString());
    }
    return json;
  }

  public static void main(String args[]) {
    try {
      int port = new Scanner(args[1]).nextInt();
      Socket server = new Socket("localhost", port);
      // System.out.println("Time: " + LocalTime.now());
      InputHandler.getRequest().writeDelimitedTo(server.getOutputStream());
      // server.shutdownOutput();
      System.out.println(
        "Status message received: " + 
        toJson(NotesResponse.parseDelimitedFrom(server.getInputStream())));
      server.close();
    } catch (Exception e) {
      System.out.println("Exception on client. Message: " + e.toString());
    }

  }
}
