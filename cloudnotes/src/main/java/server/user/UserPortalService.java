package cloudnotes.server;

import java.net.*;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.time.LocalTime;

import cloudnotes.proto.Note;
import cloudnotes.proto.NotesCollection;
import cloudnotes.proto.NotesRequest;
import cloudnotes.proto.NotesRequest.RequestType;
import cloudnotes.proto.NotesResponse;
import cloudnotes.proto.OperationStatus;
import cloudnotes.server.NotesCacheInterface;
import cloudnotes.server.mosquitto.Listener;
import cloudnotes.server.mosquitto.Publisher;

public class UserPortalService {
  private static final String CREATE_NOTE_TOPIC = "user/create/note";
  private static final String UPDATE_NOTE_TOPIC = "user/update/note";
  private static final String DELETE_NOTE_TOPIC = "user/delete/note";
  private final NotesCacheInterface cacheManager;
  private final Publisher publisher;
  private final Listener listener;

  public UserPortalService(NotesCacheInterface cacheManager) {
    this.cacheManager = cacheManager;
    String id = LocalTime.now().toString();
    publisher = new Publisher("userPublisher#" + id);
    listener = new Listener("userListener#" + id);
    subscribeToTopics();
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
    publisher.publish(CREATE_NOTE_TOPIC, request.toByteArray());
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
    publisher.publish(UPDATE_NOTE_TOPIC, request.toByteArray());
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
    publisher.publish(DELETE_NOTE_TOPIC, request.toByteArray());
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
        .addValues(cacheManager.get(request))
        .build();
  }
  
  private NotesResponse handleGetAllNotes(NotesRequest request) {
    System.out.println("handleGetAllNotes()!");
    NotesCollection notesCollection = cacheManager.getAll(request);
    NotesResponse.Builder builder = NotesResponse.newBuilder()
      .setType(request.getType())
      .setStatus(
        OperationStatus.newBuilder()
          .setType(OperationStatus.StatusType.SUCCESS)
          .build());

    for (int i=0; i<notesCollection.getNotesCount(); i++) {
      builder.addValues(notesCollection.getNotes(i));
      System.out.println("add note");
    }

    return builder.build();
  }

  private void subscribeToTopics() {
    listener.subscribe(CREATE_NOTE_TOPIC, (byte[] payload) -> {
      cacheManager.create(NotesRequest.parseFrom(payload));
    });
    listener.subscribe(UPDATE_NOTE_TOPIC, (byte[] payload) -> {
      cacheManager.update(NotesRequest.parseFrom(payload));
    });
    listener.subscribe(DELETE_NOTE_TOPIC, (byte[] payload) -> {
      cacheManager.delete(NotesRequest.parseFrom(payload));
    });
  }
}
