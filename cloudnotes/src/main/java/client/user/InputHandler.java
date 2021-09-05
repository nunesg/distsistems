package cloudnotes.client;

import java.util.Scanner;

import cloudnotes.proto.Note;
import cloudnotes.proto.NoteContent;
import cloudnotes.proto.NoteId;
import cloudnotes.proto.NotesRequest;
import cloudnotes.proto.NotesRequest.RequestType;
import cloudnotes.proto.UserId;

public class InputHandler {
  public static NotesRequest getRequest() {
    System.out.println("Type the requestType:\n0- unknown\n1- create\n2- update\n3- delete\n4- get\n5- getAll\n");
    Scanner in = new Scanner(System.in);
    int val = in.nextInt();
    System.out.println("Value typed: " + val);
    return buildRequestForType(getTypeFromInt(val));
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

  private static NotesRequest buildRequestForType(NotesRequest.RequestType type) {
    switch(type) {
      case CREATE:
        return buildCreateRequest();
      case UPDATE:
        return buildUpdateRequest();
      case DELETE:
        return buildDeleteRequest();
      case GET:
        return buildGetRequest();
      case GET_ALL:
        return buildGetAllRequest();
      default:
        return NotesRequest.newBuilder().setType(NotesRequest.RequestType.UNKNOWN).build();
    }
  }

  private static NotesRequest buildCreateRequest() {
    String title, body;
    String userId;
    Scanner in = new Scanner(System.in);
    System.out.printf("Title: ");
    title = in.nextLine();
    System.out.printf("Body: ");
    body = in.nextLine();
    System.out.printf("User id: ");
    userId = in.nextLine();
    System.out.println("userId: " + userId);
    return NotesRequest.newBuilder()
      .setType(NotesRequest.RequestType.CREATE)
      .setNote(
        Note.newBuilder()
          .setContent(
            NoteContent.newBuilder()
              .setTitle(title)
              .setBody(body)
              .build())
          .setUserId(
            UserId.newBuilder().setValue(userId).build())
          .build())
      .build();
  }

  private static NotesRequest buildUpdateRequest() {
    String title, body;
    String userId, noteId;
    Scanner in = new Scanner(System.in);
    System.out.printf("Title: ");
    title = in.nextLine();
    System.out.printf("Body: ");
    body = in.nextLine();
    System.out.printf("User id: ");
    userId = in.nextLine();
    System.out.printf("Note id: ");
    noteId = in.nextLine();

    return NotesRequest.newBuilder()
      .setType(NotesRequest.RequestType.UPDATE)
      .setNote(
        Note.newBuilder()
          .setContent(
            NoteContent.newBuilder()
              .setTitle(title)
              .setBody(body)
              .build())
          .setId(
            NoteId.newBuilder().setValue(noteId).build())
          .setUserId(
            UserId.newBuilder().setValue(userId).build())
          .build())
      .build();
  }

  private static NotesRequest buildDeleteRequest() {
    String noteId, userId;
    Scanner in = new Scanner(System.in);
    System.out.printf("Note id: ");
    noteId = in.nextLine();
    System.out.printf("User id: ");
    userId = in.nextLine();

    return NotesRequest.newBuilder()
      .setType(NotesRequest.RequestType.DELETE)
      .setNote(
        Note.newBuilder()
          .setId(
            NoteId.newBuilder().setValue(noteId).build())
          .setUserId(
            UserId.newBuilder().setValue(userId).build())
          .build())
      .build();
  }

  private static NotesRequest buildGetRequest() {
    String noteId, userId;
    Scanner in = new Scanner(System.in);
    System.out.printf("Note id: ");
    noteId = in.nextLine();
    System.out.printf("User id: ");
    userId = in.nextLine();

    return NotesRequest.newBuilder()
      .setType(NotesRequest.RequestType.GET)
      .setNote(
        Note.newBuilder()
          .setId(
            NoteId.newBuilder().setValue(noteId).build())
          .setUserId(
            UserId.newBuilder().setValue(userId).build())
          .build())
      .build();
  }

  private static NotesRequest buildGetAllRequest() {
    String userId;
    Scanner in = new Scanner(System.in);
    System.out.printf("User id: ");
    userId = in.nextLine();

    return NotesRequest.newBuilder()
      .setType(NotesRequest.RequestType.GET_ALL)
      .setNote(
        Note.newBuilder()
          .setUserId(
            UserId.newBuilder().setValue(userId).build())
          .build())
      .build();
  }
}
