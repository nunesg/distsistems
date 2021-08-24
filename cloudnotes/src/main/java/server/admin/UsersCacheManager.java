package cloudnotes.server;

import cloudnotes.server.UsersCacheInterface;

import cloudnotes.proto.User;
import cloudnotes.proto.UserId;
import cloudnotes.proto.UsersCollection;

public class UsersCacheManager implements UsersCacheInterface {
  public void create(User user) {
    System.out.println("Create user on cacheManager!");
  }
  
  public void update(User user) {
    System.out.println("Update user on cacheManager!");
  }
  
  public void delete(UserId userId) {
    System.out.println("Delete user on cacheManager!");
  }

  public User get(UserId userId) {
    return User.newBuilder().build();
  }

  public UsersCollection getAll() {
    return UsersCollection.newBuilder().build();
  }
}