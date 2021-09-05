package cloudnotes.server;

import cloudnotes.proto.User;
import cloudnotes.proto.UserId;
import cloudnotes.proto.UsersCollection;

public interface UsersCacheInterface {
  public boolean has(UserId id);

  public UserId create(User user);
  
  public void update(User user);
  
  public void delete(UserId userId);

  public User get(UserId userId);

  public UsersCollection getAll();
}