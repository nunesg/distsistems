package cloudnotes.server;

import cloudnotes.proto.AdminPortalGrpc;
import cloudnotes.proto.AdminPortalGrpc.AdminPortalImplBase;
import cloudnotes.proto.CommonMessage;
import cloudnotes.proto.EmptyMessage;
import cloudnotes.proto.OperationStatus;
import cloudnotes.proto.User;
import cloudnotes.proto.UserId;
import cloudnotes.proto.UsersCollection;

import cloudnotes.server.UsersCacheInterface;
import cloudnotes.server.mosquitto.Listener;
import cloudnotes.server.mosquitto.Publisher;

import io.grpc.stub.StreamObserver;
import java.util.Scanner;
import java.util.Random;
import com.google.protobuf.InvalidProtocolBufferException;

public class AdminPortalService extends AdminPortalGrpc.AdminPortalImplBase {
    private static final String CREATE_USER_TOPIC = "admin/create/user";
    private static final String UPDATE_USER_TOPIC = "admin/update/user";
    private static final String DELETE_USER_TOPIC = "admin/delete/user";
    private final UserIdManager userIdManager;
    private final UsersCacheInterface cacheManager;
    private Publisher publisher;
    private Listener listener;

    AdminPortalService(UsersCacheInterface cacheManager) {
      super();
      this.userIdManager = new UserIdManager();
      this.cacheManager = cacheManager;
      publisher = new Publisher("adminPublisher#" + new Random().nextInt(5));
      listener = new Listener("adminListener#" + new Random().nextInt(5));
      subscribeToTopics();
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
      publisher.publish(CREATE_USER_TOPIC, userRequest.toByteArray());
      responseObserver.onNext(
        OperationStatus.newBuilder()
          .setType(OperationStatus.StatusType.SUCCESS)
          .build());
      responseObserver.onCompleted();
    }

    @Override
    public void updateUser(User userRequest, StreamObserver<OperationStatus> responseObserver) {
      System.out.println("updateUser request");
      publisher.publish(UPDATE_USER_TOPIC, userRequest.toByteArray());
      OperationStatus status = OperationStatus.newBuilder()
        .setType(OperationStatus.StatusType.SUCCESS)
        .build();
      responseObserver.onNext(status);
      responseObserver.onCompleted();
    }

    @Override
    public void deleteUser(UserId userIdRequest, StreamObserver<OperationStatus> responseObserver) {
      System.out.println("deleteUser request");
      publisher.publish(DELETE_USER_TOPIC, userIdRequest.toByteArray());
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

    private void subscribeToTopics() {
      listener.subscribe(CREATE_USER_TOPIC, (byte[] payload) -> {
        cacheManager.create(User.parseFrom(payload));
      });
      listener.subscribe(UPDATE_USER_TOPIC, (byte[] payload) -> {
        cacheManager.update(User.parseFrom(payload));
      });
      listener.subscribe(DELETE_USER_TOPIC, (byte[] payload) -> {
        cacheManager.delete(UserId.parseFrom(payload));
      });
    }
  }
