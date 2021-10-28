package cloudnotes.server;

public class PeerConfig {
  private String id;
  private String ip;
  private int port;

  public PeerConfig(String id, String ip, int port) {
    this.id = id;
    this.ip = ip;
    this.port = port;
  }

  public PeerConfig(String ip, int port) {
    this("peerId", ip, port);
  }

  public String getId() {
    return new String(this.id);
  }

  public String getIp() {
    return new String(this.ip);
  }

  public int getPort() {
    return this.port;
  }
}