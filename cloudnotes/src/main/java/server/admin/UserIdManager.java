package cloudnotes.server;

import java.util.HashSet;

import cloudnotes.proto.UserId;

public class UserIdManager {
  private int nextId;

  UserIdManager() {
    nextId = 0;
  }

  public UserId createId() {
    return UserId.newBuilder().setValue(nextId++).build();
  }

  public boolean hasId(UserId id) {
    return id.getValue() < nextId;
  }

  public UserId getLastId() {
    return UserId.newBuilder().setValue(nextId-1).build();
  }
}