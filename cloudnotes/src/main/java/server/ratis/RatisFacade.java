package cloudnotes.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.net.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

import cloudnotes.server.UsersCacheManager;

public class RatisFacade {
  private static RatisFacade instance;
  private Socket server;
  private PrintWriter out;
  private Scanner in;

  private RatisFacade() {}
  
  private void setup() {
    try {
      ConfigFacade config = ConfigFacade.getInstance();
      server = new Socket(config.getClient().getIp(), config.getClient().getPort());
      out = new PrintWriter(server.getOutputStream(), true);
      in = new Scanner(new InputStreamReader(server.getInputStream()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static RatisFacade getInstance() {
    if (RatisFacade.instance != null) {
      return RatisFacade.instance;
    }
    return RatisFacade.instance = new RatisFacade();
  }

  public String add(String key, String val) {
    setup();
    String res = null;
    try{
      out.println("add|" + key + "|" + val + "&");
      res = readInput();
      System.out.println(res);
    } catch (Exception e) {
      e.printStackTrace();
    }
    close();
    return res;
  }

  public String remove(String key) {
    setup();
    String res;
    try{
      out.println("remove|" + key + "|&");
      res = readInput();
      System.out.println(res);
      close();
      return res;
    } catch (Exception e) {
      e.printStackTrace();
    }
    close();
    return null;
  }

  public String get(String key) {
    setup();
    String[] res;
    String line;
    try {
      out.println("get|" + key + "|&");
      res = readInput().split("\\|");
      System.out.println(res);
      close();
      return res[1];
    } catch (Exception e) {
      e.printStackTrace();
    }
    close();
    return null;
  }
  
  public Map<String, String> getAll() {
    setup();
    Map<String, String> res;
    try{
      out.println("getall" + "|&");
      String mapAsString = readInput();
      System.out.println("map: " + mapAsString);
      res = Arrays.stream(mapAsString.split("\\|"))
        .map(entry -> entry.split("="))
        .filter(entry -> entry.length > 1)
        .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
      System.out.println(res.toString());
      close();
      return res;
    } catch (Exception e) {
      e.printStackTrace();
    }
    close();
    return null;
  }

  public void close() {
    try{
      server.close();
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String readInput() {
    try {
        StringBuffer buffer = new StringBuffer();
        String s;
        while(in.hasNextLine()) {
          s = in.nextLine();
          System.out.println("s = " + s);
          buffer.append(s);
        }
        String str = buffer.toString();
        System.out.println("str read is <" + str + ">");
        return str;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "";
  }
}
