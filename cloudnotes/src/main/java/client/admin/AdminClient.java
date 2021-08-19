package cloudnotes.client;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AdminClient {
  private static final int PORT_NUMBER = 12340;
  private static final String HOST_ADDRESS = "localhost";
  private static final String TARGET = HOST_ADDRESS + ":" + PORT_NUMBER;

  public static void main(String[] args) {
    
    ManagedChannel channel = ManagedChannelBuilder.forTarget(TARGET).usePlaintext().build();
    Admin admin = new Admin(channel);

    try {
      admin.run();
    } catch (Exception e) {
      System.out.println("Error on adminClient: " + e);
    }
  }
}
