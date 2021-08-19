package cloudnotes.server;

import cloudnotes.proto.AdminPortalGrpc;
import cloudnotes.proto.AdminPortalGrpc.AdminPortalImplBase;
import cloudnotes.proto.CommonMessage;
import cloudnotes.proto.EmptyMessage;
import cloudnotes.proto.OperationStatus;
import cloudnotes.proto.User;
import cloudnotes.proto.UserId;

import io.grpc.stub.StreamObserver;
import java.util.Scanner;

public class AdminPortalService extends AdminPortalGrpc.AdminPortalImplBase {
    private final UserIdManager userIdManager;

    AdminPortalService() {
      this.userIdManager = new UserIdManager();
    }

    @Override
    public StreamObserver<CommonMessage> talk(StreamObserver<CommonMessage> responseObserver) {
      return new StreamObserver<CommonMessage>() {
        @Override
        public void onNext(CommonMessage msg) {
          System.out.println("Message received: " + msg.getContent());
          if (msg.getContent().equals("stop")) return;
          Scanner in = new Scanner(System.in);
          responseObserver.onNext(CommonMessage.newBuilder().setContent(in.nextLine()).build());
        }
        
        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onCompleted() {
          System.out.println("Finished reading inputs from client.");
          // responseObserver.onNext(CommonMessage.newBuilder().setContent("Thanks for the talk!").build());
          responseObserver.onCompleted();
        }
      };
    }

    @Override
    public void getValidId(EmptyMessage emptyRequest, StreamObserver<UserId> responseObserver) {
      System.out.println("getValidId request");
      responseObserver.onNext(userIdManager.createId());
      responseObserver.onCompleted();
    }

    @Override
    public void createUser(User userRequest, StreamObserver<OperationStatus> responseObserver) {
      System.out.println("getCreateUser request");
      OperationStatus.Builder statusBuilder = OperationStatus.newBuilder();
      if (isNewIdValid(userRequest.getId()) && userRequest.hasData()) {
        insertUser(userRequest);
        statusBuilder.setType(OperationStatus.StatusType.SUCCESS);
      } else {
        statusBuilder.setType(OperationStatus.StatusType.FAILED);
      }

      responseObserver.onNext(statusBuilder.build());
      responseObserver.onCompleted();
    }

    // rpc updateUser (User) returns (OperationStatus) {}
    
    // rpc getUser (UserId) returns (User) {}
    // rpc getAllUsers (EmptyMessage) returns (UsersCollection) {}
    
    // rpc deleteUser (UserId) returns (OperationStatus) {}


    private boolean isNewIdValid(UserId id) {
      return userIdManager.getLastId().getValue() == id.getValue();
    }

    private void insertUser(User user) {
      System.out.println("User inserted!");
    }
  }