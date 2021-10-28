package cloudnotes.server;

import java.math.BigInteger;

import cloudnotes.server.RatisFacade;

public class IdManager {
  private BigInteger nextId;
  private final RatisFacade db;
  private final String maxIdKey;

  IdManager(RatisFacade db, String name) {
    this.db = db;
    maxIdKey = name + "MaxId";

    String lastId = db.get(maxIdKey);
    System.out.println("lastId = " + lastId + ".");
    if (lastId.startsWith("null")) {
      db.add(maxIdKey, "0");
      String other = db.get(maxIdKey);
      System.out.println("after adding to db: " + other);
      lastId = other;
    }

    if (lastId.startsWith("0")) {
      nextId = BigInteger.valueOf(0);
    } else {
      nextId = new BigInteger(lastId);
    }
  }

  public String create() {
    increaseId();
    return nextId.toString();
  }
  
  private void increaseId() {
    nextId = nextId.add(BigInteger.ONE);
    db.add(maxIdKey, nextId.toString());
  }
}