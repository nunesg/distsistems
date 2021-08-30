package cloudnotes.server;

import java.net.*;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import cloudnotes.proto.NotesRequest;
import cloudnotes.server.NotesCacheManager;

public class UserPortalServer {
  public static void main(String args[]) {
    UserRequestHandler requestHandler = new UserRequestHandler(new NotesCacheManager());
    int port = new Scanner(args[1]).nextInt();

    try {
      ServerSocket server = new ServerSocket(port);
      System.out.println("Server listening on port " + port);

      try {

        while (true) {
          Socket client = server.accept();
          System.out.println("Client connected on port " + port + "!");
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
      System.out.println("Error trying to setup server on port " + port);
    }
  }
}
