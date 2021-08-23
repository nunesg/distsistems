package cloudnotes.client;

import java.util.Scanner;

import cloudnotes.proto.NotesRequest;
import cloudnotes.proto.NotesRequest.RequestType;

public class InputHandler {
  public static NotesRequest getRequest() {
    System.out.println("Type the requestType:\n0- unknown\n1- create\n2- update\n3- delete\n4- get\n5- getAll\n");
    Scanner in = new Scanner(System.in);
    int val = in.nextInt();
    System.out.println("Value typed: " + val);
    return NotesRequest.newBuilder().setType(getTypeFromInt(val)).build();
  }

  private static NotesRequest.RequestType getTypeFromInt(int val) {
    switch(val) {
      case 1:
        return NotesRequest.RequestType.CREATE;
      case 2:
        return NotesRequest.RequestType.UPDATE;
      case 3:
        return NotesRequest.RequestType.DELETE;
      case 4:
        return NotesRequest.RequestType.GET;
      case 5:
        return NotesRequest.RequestType.GET_ALL;
      default:
        return NotesRequest.RequestType.UNKNOWN;
    }
  }
}
