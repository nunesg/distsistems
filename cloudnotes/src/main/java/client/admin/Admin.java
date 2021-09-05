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
import cloudnotes.proto.UserResponse;
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
    System.out.println("Request result: " + status.toString());
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
    String userName, userId;
    Scanner in = new Scanner(System.in);
    System.out.printf("User id: ");
    userId = in.nextLine();
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
    String userId;
    Scanner in = new Scanner(System.in);
    System.out.printf("User id: ");
    userId = in.nextLine();

    return blockingStub.deleteUser(
        UserId.newBuilder()
          .setValue(userId)
          .build());
  }

  private OperationStatus getUser() {
    String userId;
    Scanner in = new Scanner(System.in);
    System.out.printf("User id: ");
    userId = in.nextLine();

    UserResponse res = blockingStub.getUser(
      UserId.newBuilder().setValue(userId).build());
    System.out.println("User retrieved: " + res.toString());
    return res.getStatus();
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