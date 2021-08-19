package cloudnotes.client;

import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import java.util.Scanner;

import cloudnotes.proto.AdminPortalGrpc;
import cloudnotes.proto.AdminPortalGrpc.AdminPortalBlockingStub;
import cloudnotes.proto.AdminPortalGrpc.AdminPortalStub;
import cloudnotes.proto.CommonMessage;

public class Admin {
  private AdminPortalBlockingStub blockingStub;
  private AdminPortalStub asyncStub;
  private ManagedChannel managedChannel;

  Admin(ManagedChannel channel) {
    managedChannel = channel;
    blockingStub = AdminPortalGrpc.newBlockingStub(channel);
    asyncStub = AdminPortalGrpc.newStub(channel);
  }

  public void run() {
    
    StreamObserver<CommonMessage> input = new StreamObserver<CommonMessage>() {
        @Override
        public void onNext(CommonMessage msg) {
          System.out.println("Message received from server: " + msg.getContent());
        }
        
        @Override
        public void onError(Throwable t) {}

        @Override
        public void onCompleted() {
          System.out.println("Server finished conversation.");
        }
    };

    StreamObserver<CommonMessage> output = asyncStub.talk(input);

    String msg;
    Scanner in = new Scanner(System.in);
    while(true) {
      msg = in.nextLine();
      output.onNext(CommonMessage.newBuilder().setContent(msg).build());
      if (msg.equals("stop")) {
        output.onCompleted();
        break;
      }
    }
  
  }



  public void shutdown() {
    managedChannel.shutdown();
  }
}