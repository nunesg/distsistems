package cloudnotes.server;

import java.net.*;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import cloudnotes.proto.CommonMessage;
import cloudnotes.server.CacheManager;

public class UserPortal {
  private static final int PORT_NUMBER = 12345;
  private final CacheManager cacheManager;

  public UserPortal() {
    cacheManager = new CacheManager();
  }

  public void insert(CommonMessage message) {
    cacheManager.insert(message);
  }

  public CommonMessage getMessage(int id) {
    try {
      return cacheManager.getMessage(id);
    } catch (Exception e) {
      System.out.println("Invalid message index {" + id + "}");
    }
    return CommonMessage.newBuilder().setContent("").build();
  }

  public void printAllMessages() {
    ArrayList<CommonMessage> messages = cacheManager.getAll();

    System.out.println("Messages received:");
    for(CommonMessage msg : messages) {
      System.out.println(msg.getContent());
    }
    System.out.println();
  }

  public static void main(String[] args) {
    UserPortal portal = new UserPortal();

    try {
      ServerSocket server = new ServerSocket(PORT_NUMBER);
      System.out.println("Server listening on port " + PORT_NUMBER);

      try {

        while (true) {
          Socket client = server.accept();
          System.out.println("Client connected on port " + PORT_NUMBER + "!");
          CommonMessage msg = CommonMessage.parseFrom(client.getInputStream());
          if (msg != null) {
            portal.insert(msg);
            System.out.println("Message received from client: " + msg.getContent());

            portal.printAllMessages();

            // CommonMessage.newBuilder().setContent("Hi user! Message received :)").build().writeTo(client.getOutputStream());
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
