import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcConfigKeys;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.RaftPeerId;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;
import org.apache.ratis.util.LifeCycle;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Server
{

    //Parametros: myId
    public static void main(String[] args) throws IOException, InterruptedException
    {
        // String raftGroupId = "raft_group___one"; // 16 caracteres.

        // //Setup for node all nodes.
        // Map<String,InetSocketAddress> id2addr = new HashMap<>();
        // id2addr.put("p1", new InetSocketAddress("127.0.0.1", 3000));
        // id2addr.put("p2", new InetSocketAddress("127.0.0.1", 3500));
        // id2addr.put("p3", new InetSocketAddress("127.0.0.1", 4000));

        try {
            ConfigFacade config = ConfigFacade.getInstance();
            
            String raftGroupId = "raft_group___one";
            System.out.println(raftGroupId);
            
            Map<String,InetSocketAddress> id2addr = new HashMap<>();
            config.getPeers().forEach(peer -> {
                id2addr.put(peer.getId(), new InetSocketAddress(peer.getIp(), peer.getPort()));
            });
            
            List<RaftPeer> addresses = id2addr
                .entrySet()
                .stream()
                .map(e -> RaftPeer.newBuilder().setId(e.getKey()).setAddress(e.getValue()).build())
                .collect(Collectors.toList());
            
            //Setup for this node.
            RaftPeerId myId = RaftPeerId.valueOf(args[0]);
            
            if (addresses.stream().noneMatch(p -> p.getId().equals(myId)))
            {
                System.out.println("Identificador " + args[0] + " ?? inv??lido.");
                System.exit(1);
            }
            
            RaftProperties properties = new RaftProperties();
            properties.setInt(GrpcConfigKeys.OutputStream.RETRY_TIMES_KEY, Integer.MAX_VALUE);
            GrpcConfigKeys.Server.setPort(properties, id2addr.get(args[0]).getPort());
            RaftServerConfigKeys.setStorageDir(properties, Collections.singletonList(new File("tmp/" + myId)));
            
            
            //Join the group of processes.
            final RaftGroup raftGroup = 
            RaftGroup.valueOf(RaftGroupId.valueOf(ByteString.copyFromUtf8(raftGroupId)), addresses);
            RaftServer raftServer = RaftServer.newBuilder()
                .setServerId(myId)
                .setStateMachine(new StateMachine()).setProperties(properties)
                .setGroup(raftGroup)
                .build();
            raftServer.start();
            
            while(raftServer.getLifeCycleState() != LifeCycle.State.CLOSED) {
                TimeUnit.SECONDS.sleep(1);
            }
            // raftServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
