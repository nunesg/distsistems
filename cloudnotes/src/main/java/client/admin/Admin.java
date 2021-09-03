package cloudnotes.client;

import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import java.util.Scanner;

import cloudnotes.proto.AdminPortalGrpc;
import cloudnotes.proto.AdminPortalGrpc.AdminPortalBlockingStub;
import cloudnotes.proto.OperationStatus;
import cloudnotes.proto.User;
import cloudnotes.proto.UserData;
import cloudnotes.proto.UserId;
import cloudnotes.proto.UsersCollection;
import cloudnotes.proto.EmptyMessage;

public class Admin {
  private AdminPortalBlockingStub blockingStub;
  private ManagedChannel managedChannel;

  Admin(ManagedChannel channel) {
    managedChannel = channel;
    blockingStub = AdminPortalGrpc.newBlockingStub(channel);
  }

  public void shutdown() {
    managedChannel.shutdown();
  }

  public void run() {
    Scanner in = new Scanner(System.in);
    
    System.out.println("Type the desired operation:\n0- createUser\n1- update user\n2- delete user\n3- get user\n4- get all users");
    int val = in.nextInt();
    OperationStatus status = handleOperation(val);
    System.out.println("Request status: " + status.getType().getValueDescriptor().getName());
  }

  // change to get an enum instead of int
  public OperationStatus handleOperation(int type) {
    switch(type) {
      case 0:
        return createUser();
      case 1:
        return updateUser();
      case 2:
        return deleteUser();
      case 3:
        return getUser();
      case 4:
        return getAllUsers();
      default:
        return OperationStatus.newBuilder()
          .setType(OperationStatus.StatusType.FAILED).build();
    }
  }

  private OperationStatus createUser() {
    int userId;
    String userName;
    Scanner in = new Scanner(System.in);
    System.out.printf("User name: ");
    userName = in.nextLine();

    return blockingStub.createUser(
      User.newBuilder()
        .setData(
          UserData.newBuilder()
            .setName(userName)
            .build())
        .build());
  }

  private OperationStatus updateUser() {
    int userId;
    String userName;
    Scanner in = new Scanner(System.in);
    System.out.printf("User id: ");
    userId = in.nextInt();
    System.out.printf("User name: ");
    in.nextLine();
    userName = in.nextLine();

    return blockingStub.updateUser(
      User.newBuilder()
        .setData(
          UserData.newBuilder()
            .setName(userName)
            .build())
        .setId(
          UserId.newBuilder()
            .setValue(userId)
            .build())
        .build());
  }

  private OperationStatus deleteUser() {
    int userId;
    Scanner in = new Scanner(System.in);
    System.out.printf("User id: ");
    userId = in.nextInt();

    return blockingStub.deleteUser(
        UserId.newBuilder()
          .setValue(userId)
          .build());
  }

  private OperationStatus getUser() {
    int noteId, userId;
    Scanner in = new Scanner(System.in);
    System.out.printf("User id: ");
    userId = in.nextInt();

    User user = blockingStub.getUser(
      UserId.newBuilder().setValue(userId).build());
    System.out.println("User retrieved: " + user.toString());
    return OperationStatus.newBuilder().setType(OperationStatus.StatusType.SUCCESS).build();
  }

  private OperationStatus getAllUsers() {
    UsersCollection users = blockingStub.getAllUsers(
      EmptyMessage.newBuilder().build());
    System.out.println("Users retrieved: " + users.toString());
    return OperationStatus.newBuilder().setType(OperationStatus.StatusType.SUCCESS).build();
  }

  private UserId getValidId() {
    return blockingStub.getValidId(EmptyMessage.newBuilder().build());
  }
}