package cloudnotes.server;

public interface ListenerCallback {
  public void run(byte[] message) throws Exception;
}