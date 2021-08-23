package cloudnotes.server;

import cloudnotes.proto.AdminPortalGrpc;
import cloudnotes.proto.AdminPortalGrpc.AdminPortalImplBase;
import cloudnotes.proto.CommonMessage;
import cloudnotes.proto.EmptyMessage;
import cloudnotes.proto.OperationStatus;
import cloudnotes.proto.User;
import cloudnotes.proto.UserId;
import cloudnotes.proto.UsersCollection;

import io.grpc.stub.StreamObserver;
import java.util.Scanner;

public class AdminPortalService extends AdminPortalGrpc.AdminPortalImplBase {
    private final UserIdManager userIdManager;

    AdminPortalService() {
      this.userIdManager = new UserIdManager();
    }

    @Override
    public void getValidId(EmptyMessage emptyRequest, StreamObserver<UserId> responseObserver) {
      System.out.println("getValidId request");
      responseObserver.onNext(userIdManager.createId());
      responseObserver.onCompleted();
    }

    @Override
    public void createUser(User userRequest, StreamObserver<OperationStatus> responseObserver) {
      System.out.println("createUser request");
      responseObserver.onNext(
        OperationStatus.newBuilder()
          .setType(OperationStatus.StatusType.SUCCESS)
          .build());
      responseObserver.onCompleted();
    }

    @Override
    public void updateUser(User userRequest, StreamObserver<OperationStatus> responseObserver) {
      System.out.println("updateUser request");
      OperationStatus status = OperationStatus.newBuilder()
        .setType(OperationStatus.StatusType.SUCCESS)
        .build();
      responseObserver.onNext(status);
      responseObserver.onCompleted();
    }

    @Override
    public void deleteUser(UserId userIdRequest, StreamObserver<OperationStatus> responseObserver) {
      System.out.println("deleteUser request");
      responseObserver.onNext(
        OperationStatus.newBuilder()
          .setType(OperationStatus.StatusType.SUCCESS)
          .build());
      responseObserver.onCompleted();
    }
    
    @Override
    public void getUser(UserId userIdRequest, StreamObserver<User> responseObserver) {
      System.out.println("getUser request");
      responseObserver.onNext(
        User.newBuilder()
          .setId(
            UserId.newBuilder()
              .setValue(userIdRequest.getValue())
              .build())
          .build());
      responseObserver.onCompleted();
    }
    
    @Override
    public void getAllUsers(EmptyMessage emptyReq, StreamObserver<UsersCollection> responseObserver) {
      System.out.println("getAllUsers request");
      responseObserver.onNext(
        UsersCollection.newBuilder()
          .addValues(
            User.newBuilder().build())
          .addValues(
            User.newBuilder().build())
          .build());
      responseObserver.onCompleted();
    }
  }
