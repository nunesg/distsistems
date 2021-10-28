package cloudnotes.server;

import cloudnotes.server.UsersCacheInterface;
import cloudnotes.server.RatisFacade;
import cloudnotes.proto.User;
import cloudnotes.proto.UserId;
import cloudnotes.proto.UsersCollection;

import java.util.HashMap;
import com.google.protobuf.util.JsonFormat;

public class UsersCacheManager implements UsersCacheInterface {
  private HashMap<String, String> cache;
  private RatisFacade db;
  private IdManager idManager;

  public UsersCacheManager() {
    cache = new HashMap<String, String>();
    idManager = new IdManager();
    db = RatisFacade.getInstance();
  }

  public boolean has(UserId id) {
    return cache.containsKey(id.getValue());
  }

  public UserId create(User user) {
    System.out.println("Create user on cacheManager!");
    user = user.toBuilder()
        .setId(getNewId())
        .build();
    System.out.println("Users cache manager create user! JSON: " + toJson(user));
    String key = user.getId().getValue();
    String value = toJson(user);
    cache.put(key, value);
    db.add(key, value);
    return user.getId();
  }
  
  public void update(User user) {
    System.out.println("Update user on cacheManager!");
    String key = user.getId().getValue();
    String value = toJson(user);
    cache.put(key, value);
    db.add(key, value);
  }
  
  public void delete(UserId userId) {
    System.out.println("Delete user on cacheManager!");
    String key = userId.getValue();
    cache.remove(key);
    db.remove(key);
  }

  public User get(UserId userId) {
    System.out.println("Get user on cacheManager!");
    String key = userId.getValue();
    // String json = cache.get(key);
    String json = db.get(key);
    return fromJson(json);
  }

  public UsersCollection getAll() {
    System.out.println("Users cache manager getAll!");
    UsersCollection.Builder builder = UsersCollection.newBuilder();
    // cache.forEach((k, v) -> builder.addValues(fromJson(v)));
    db.getAll().forEach((k, v) -> builder.addValues(fromJson(v)));
    return builder.build();
  }

  private UserId getNewId() {
    return UserId.newBuilder().setValue("user_" + idManager.create()).build();
  }

  private String toJson(User user) {
    String json = new String();
    try {
      json = JsonFormat.printer().print(user);
    } catch (Exception e) {
      System.out.println("Error while converting message to json: " + user.toString());
    }
    return json;
  }
  
  private User fromJson(String json) {
    User.Builder builder = User.newBuilder();
    try {
      JsonFormat.parser().ignoringUnknownFields().merge(json, builder);
    } catch (Exception e) {
      System.out.println("Error while parsing json: " + json);
    }
    return builder.build();
  }
}