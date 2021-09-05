package cloudnotes.server;

import cloudnotes.proto.AdminPortalGrpc;
import cloudnotes.proto.AdminPortalGrpc.AdminPortalImplBase;
import cloudnotes.proto.CommonMessage;
import cloudnotes.proto.EmptyMessage;
import cloudnotes.proto.OperationStatus;
import cloudnotes.proto.User;
import cloudnotes.proto.UserId;
import cloudnotes.proto.UsersCollection;
import cloudnotes.proto.UserRequest;
import cloudnotes.proto.UserResponse;

import cloudnotes.server.UsersCacheInterface;
import cloudnotes.server.mosquitto.Listener;
import cloudnotes.server.mosquitto.Publisher;
import cloudnotes.server.mosquitto.Topics;

import io.grpc.stub.StreamObserver;
import java.util.Scanner;
import java.time.LocalTime;
import com.google.protobuf.InvalidProtocolBufferException;

public class AdminPortalService extends AdminPortalGrpc.AdminPortalImplBase {
    private final UsersCacheInterface cacheManager;
    private Publisher publisher;
    private Listener listener;

    AdminPortalService(UsersCacheInterface cacheManager) {
      super();
      this.cacheManager = cacheManager;
      String id = LocalTime.now().toString();
      publisher = new Publisher("adminPublisher#" + id);
      listener = new Listener("adminListener#" + id);
      subscribeToTopics();
    }

    @Override
    public void createUser(User userRequest, StreamObserver<OperationStatus> responseObserver) {
      System.out.println("createUser request");
      OperationStatus.Builder builder = OperationStatus.newBuilder();

      try {
        builder.setUserId(cacheManager.create(userRequest));
        markAsSuccess(builder);
        publisher.publish(
          Topics.CREATE_USER, 
          UserRequest.newBuilder()
            .setUser(userRequest)
            .setSender(publisher.getId())
            .build()
            .toByteArray());

      } catch (Exception e) {
        markAsFailure(builder, e.getMessage());
      }
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    }

    @Override
    public void updateUser(User userRequest, StreamObserver<OperationStatus> responseObserver) {
      System.out.println("updateUser request");

      OperationStatus.Builder builder = OperationStatus.newBuilder();

      try {
        if (!hasUser(userRequest.getId())) {
          throw new Exception("User doesn't exist");
        }
        cacheManager.update(userRequest);
        markAsSuccess(builder);
        publisher.publish(
          Topics.UPDATE_USER, 
          UserRequest.newBuilder()
            .setUser(userRequest)
            .setSender(publisher.getId())
            .build()
            .toByteArray());
      } catch (Exception e) {
        markAsFailure(builder, e.getMessage());
      }
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    }

    @Override
    public void deleteUser(UserId userIdRequest, StreamObserver<OperationStatus> responseObserver) {
      System.out.println("deleteUser request");
      
      OperationStatus.Builder builder = OperationStatus.newBuilder();

      try {
        if (!hasUser(userIdRequest)) {
          throw new Exception("User doesn't exist");
        }
        cacheManager.delete(userIdRequest);
        markAsSuccess(builder);
        publisher.publish(
          Topics.DELETE_USER, 
          UserRequest.newBuilder()
            .setUser(
              User.newBuilder().setId(userIdRequest).build())
            .setSender(publisher.getId())
            .build()
            .toByteArray());
      } catch (Exception e) {
        markAsFailure(builder, e.getMessage());
      }
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    }
    
    @Override
    public void getUser(UserId userIdRequest, StreamObserver<UserResponse> responseObserver) {
      System.out.println("getUser request");
      UserResponse.Builder builder = UserResponse.newBuilder();
      OperationStatus.Builder statusBuilder = OperationStatus.newBuilder();

      if (!hasUser(userIdRequest)) {
        markAsFailure(statusBuilder, "Invalid user");
        responseObserver.onNext(builder.setStatus(statusBuilder.build()).build());
      } else {
        markAsSuccess(statusBuilder);
        responseObserver.onNext(
          builder
            .setStatus(statusBuilder.build())
            .setUser(cacheManager.get(userIdRequest))
            .build());
      }
      responseObserver.onCompleted();
    }
    
    @Override
    public void getAllUsers(EmptyMessage emptyReq, StreamObserver<UsersCollection> responseObserver) {
      System.out.println("getAllUsers request");
      responseObserver.onNext(cacheManager.getAll());
      responseObserver.onCompleted();
    }

    private void subscribeToTopics() {
      listener.subscribe(Topics.CREATE_USER, (byte[] payload) -> {
        if (getSenderFromPayload(payload).equals(publisher.getId())) {
          return;
        }
        cacheManager.create(UserRequest.parseFrom(payload).getUser());
      });
      listener.subscribe(Topics.UPDATE_USER, (byte[] payload) -> {
        if (getSenderFromPayload(payload).equals(publisher.getId())) {
          return;
        }
        cacheManager.update(UserRequest.parseFrom(payload).getUser());
      });
      listener.subscribe(Topics.DELETE_USER, (byte[] payload) -> {
        if (getSenderFromPayload(payload).equals(publisher.getId())) {
          return;
        }
        cacheManager.delete(UserRequest.parseFrom(payload).getUser().getId());
      });
    }

    private boolean hasUser(UserId id) {
      return cacheManager.has(id);
    }

    private String getSenderFromPayload(byte[] payload) throws Exception {
      return UserRequest.parseFrom(payload).getSender();
    }

    private void markAsFailure(OperationStatus.Builder builder, String msg) {
      builder.setType(OperationStatus.StatusType.FAILED).setMessage(msg);
    }

    private void markAsSuccess(OperationStatus.Builder builder) {
      builder.setType(OperationStatus.StatusType.SUCCESS);
    }
  }
