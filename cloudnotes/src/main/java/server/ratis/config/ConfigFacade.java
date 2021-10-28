package cloudnotes.server;

import java.io.FileReader;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ConfigFacade {
  private static ConfigFacade instance;
  private static String configPath = "src/main/resources/config.json";

  private JSONObject configObj;

  private ConfigFacade() throws Exception {
    init();
  }

  public static ConfigFacade getInstance()  throws Exception {
    if (ConfigFacade.instance != null) {
      return ConfigFacade.instance;
    }
    return ConfigFacade.instance = new ConfigFacade();
  }

  private void init() throws Exception {
    System.out.println("Working Directory = " + System.getProperty("user.dir"));
    JSONParser parser = new JSONParser();
    configObj = (JSONObject) parser.parse(new FileReader(configPath));
  }

  public ArrayList<PeerConfig> getPeers() {
    JSONArray arr = (JSONArray)configObj.get("peers");
    ArrayList<PeerConfig> peers = new ArrayList<PeerConfig>();
    
    arr.forEach(e -> {
      JSONObject elem  = (JSONObject)e;
      peers.add(
        new PeerConfig(
          (String) elem.get("id"), 
          (String) elem.get("ip"), 
          ((Long)elem.get("port")).intValue()
        )
      );
    });
    return peers;
  }

  public PeerConfig getClient() {
    JSONObject obj = (JSONObject) configObj.get("client");
    return new PeerConfig(
      (String) obj.get("ip"), 
      ((Long)obj.get("port")).intValue()
    );
  }

  public String getGroupId() {
    return (String) configObj.get("groupId");
  }
}