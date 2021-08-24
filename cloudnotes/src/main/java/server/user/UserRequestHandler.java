package cloudnotes.server;

import java.net.*;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import cloudnotes.proto.NotesRequest;
import cloudnotes.proto.NotesRequest.RequestType;
import cloudnotes.proto.NotesResponse;
import cloudnotes.proto.OperationStatus;
import cloudnotes.server.NotesCacheInterface;

public class UserRequestHandler {
  private final NotesCacheInterface cacheManager;

  public UserRequestHandler(NotesCacheInterface cacheManager) {
    this.cacheManager = cacheManager;
  }

  public NotesResponse handle(NotesRequest request) {
    switch (request.getType()) {
      case CREATE:
        return handleCreateNote(request);
      case UPDATE:
        return handleUpdateNote(request);
      case DELETE:
        return handleDeleteNote(request);
      case GET:
        return handleGetNote(request);
      case GET_ALL:
        return handleGetAllNotes(request);
      default:
        return NotesResponse.newBuilder()
          .setStatus(
            OperationStatus.newBuilder()
              .setType(OperationStatus.StatusType.FAILED)
              .build())
          .build();
    }
  }

  private NotesResponse handleCreateNote(NotesRequest request) {
    System.out.println("handleCreateNote()!");
    return NotesResponse.newBuilder()
        .setType(request.getType())
        .setStatus(
          OperationStatus.newBuilder()
            .setType(OperationStatus.StatusType.SUCCESS)
            .build())
        .build();
  }
  
  private NotesResponse handleUpdateNote(NotesRequest request) {
    System.out.println("handleUpdateNote()!");
    return NotesResponse.newBuilder()
        .setType(request.getType())
        .setStatus(
          OperationStatus.newBuilder()
            .setType(OperationStatus.StatusType.SUCCESS)
            .build())
        .build();
  }
  
  private NotesResponse handleDeleteNote(NotesRequest request) {
    System.out.println("handleDeleteNote()!");
    return NotesResponse.newBuilder()
        .setType(request.getType())
        .setStatus(
          OperationStatus.newBuilder()
            .setType(OperationStatus.StatusType.SUCCESS)
            .build())
        .build();
  }
  
  private NotesResponse handleGetNote(NotesRequest request) {
    System.out.println("handleGetNote()!");
    return NotesResponse.newBuilder()
        .setType(request.getType())
        .setStatus(
          OperationStatus.newBuilder()
            .setType(OperationStatus.StatusType.SUCCESS)
            .build())
        .build();
  }
  
  private NotesResponse handleGetAllNotes(NotesRequest request) {
    System.out.println("handleGetAllNotes()!");
    return NotesResponse.newBuilder()
        .setType(request.getType())
        .setStatus(
          OperationStatus.newBuilder()
            .setType(OperationStatus.StatusType.SUCCESS)
            .build())
        .build();
  }
}
