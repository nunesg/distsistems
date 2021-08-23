package cloudnotes.server;

import java.net.*;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import cloudnotes.proto.NotesRequest;

public class UserPortalServer {
  private static final int PORT_NUMBER = 12345;

  public static void main(String[] args) {
    UserRequestHandler requestHandler = new UserRequestHandler();

    try {
      ServerSocket server = new ServerSocket(PORT_NUMBER);
      System.out.println("Server listening on port " + PORT_NUMBER);

      try {

        while (true) {
          Socket client = server.accept();
          System.out.println("Client connected on port " + PORT_NUMBER + "!");
          NotesRequest req = NotesRequest.parseDelimitedFrom(client.getInputStream());
          if (req != null) {
            requestHandler.handle(req).writeDelimitedTo(client.getOutputStream());
          } else {
            throw new Exception("NotesRequest cannot be null!");
          }
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
