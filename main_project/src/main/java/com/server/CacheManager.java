package com.server;

import java.util.ArrayList;

import com.proto.CommonMessage;

public class CacheManager {
  private ArrayList<CommonMessage> messages;

  public void insert(CommonMessage message) {
    messages.add(message);
  }

  public CommonMessage getMessage(int id) throws Exception {
    if (id < 0 || id >= messages.size()) throw new Exception();
    return messages.get(id);
  }

  public ArrayList<CommonMessage> getAll() {
    return (ArrayList<CommonMessage>)messages.clone();
  }
}