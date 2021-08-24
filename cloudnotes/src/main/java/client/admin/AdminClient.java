package cloudnotes.client;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Scanner;

public class AdminClient {
  private static int port;
  private static String host = "localhost";

  public static void main(String args[]) {
    int port = new Scanner(args[1]).nextInt();
    String target = host+":"+port;
    ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
    Admin admin = new Admin(channel);

    try {
      admin.run();
    } catch (Exception e) {
      System.out.println("Error on adminClient: " + e);
    }
  }
}
