import org.apache.ratis.client.RaftClient;
import org.apache.ratis.conf.Parameters;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcFactory;
import org.apache.ratis.protocol.*;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Client
{
    

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        // String raftGroupId = "raft_group____um"; // 16 caracteres.

        // Map<String,InetSocketAddress> id2addr = new HashMap<>();
        // id2addr.put("p1", new InetSocketAddress("127.0.0.1", 3000));
        // id2addr.put("p2", new InetSocketAddress("127.0.0.1", 3500));
        // id2addr.put("p3", new InetSocketAddress("127.0.0.1", 4000));
        try{
            ConfigFacade config = ConfigFacade.getInstance();
            
            String raftGroupId = "raft_group___one";
            System.out.println(raftGroupId);
        
            Map<String,InetSocketAddress> id2addr = new HashMap<>();
            config.getPeers().forEach(peer -> {
                System.out.println(peer.getId() + " " + peer.getIp() + " " + peer.getPort());
                id2addr.put(peer.getId(), new InetSocketAddress(peer.getIp(), peer.getPort()));
            });
            
            List<RaftPeer> addresses = id2addr.entrySet()
            .stream()
                    .map(e -> RaftPeer.newBuilder().setId(e.getKey()).setAddress(e.getValue()).build())
                    .collect(Collectors.toList());
                    
            final RaftGroup raftGroup = RaftGroup.valueOf(RaftGroupId.valueOf(ByteString.copyFromUtf8(raftGroupId)), addresses);
            RaftProperties raftProperties = new RaftProperties();

            RaftClient client = RaftClient.newBuilder()
                                        .setProperties(raftProperties)
                                        .setRaftGroup(raftGroup)
                                        .setClientRpc(new GrpcFactory(new Parameters())
                                        .newRaftClientRpc(ClientId.randomId(), raftProperties))
                                        .build();



            int port = config.getClient().getPort();
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Server listening on port " + port);
            
                try {
            
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        System.out.println("Client connected on port " + port + "!");
                        
                        String req = readInput(in);
                        System.out.println("req: " + req);
                        if (req != null) {
                            out.println(serve(req.split("\\|"), client));
                        }
                        clientSocket.close();
                    }
                } catch (Exception e) {
                    System.out.println("Server error. Message: " + e.getMessage());
                }
                
                serverSocket.close();
            } catch (Exception e) {
                System.out.println("Error trying to setup server on port " + port);
            }
            
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String serve(String[] args, RaftClient client) throws Exception {
        RaftClientReply getValue;
        CompletableFuture<RaftClientReply> compGetValue;
        String response;
        System.out.println("args: " + Arrays.toString(args));
        switch (args[0]){
            case "add":
                getValue = client.io().send(Message.valueOf("add|" + args[1] + "|" + args[2]));
                response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
                System.out.println("Resposta: " + response);
                return response;
            case "get":
                getValue = client.io().sendReadOnly(Message.valueOf("get|" + args[1]));
                response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
                System.out.println("Resposta: " + response);
                return response;
            case "getall":
                getValue = client.io().sendReadOnly(Message.valueOf("getall"));
                response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
                System.out.println("Resposta: " + response);
                return response;
            case "remove":
                getValue = client.io().send(Message.valueOf("remove|" + args[1]));
                response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
                System.out.println("Resposta: " + response);
                return response;
            // case "add_async":
            //     compGetValue = client.async().send(Message.valueOf("add:" + args[1] + ":" + args[2]));
            //     getValue = compGetValue.get();
            //     response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
            //     System.out.println("Resposta: " + response);
            //     return response;
            // case "get_stale":
            //     getValue = client.io().sendStaleRead(Message.valueOf("get:" + args[1]), 0, RaftPeerId.valueOf(args[2]));
            //     response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
            //     System.out.println("Resposta: " + response);
            //     return response;
            default:
                System.out.println("Comando inválido");
                return "Invalid command";
        }
    }

    private static String readInput(BufferedReader in) {
        try {
            char[] buffer = new char[1024];
            in.read(buffer, 0, 1024);
            return new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
