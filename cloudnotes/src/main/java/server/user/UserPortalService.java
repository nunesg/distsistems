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
import cloudnotes.proto.User;
import cloudnotes.proto.UserId;

import cloudnotes.server.NotesCacheInterface;
import cloudnotes.server.UsersCacheInterface;
import cloudnotes.server.mosquitto.Listener;
import cloudnotes.server.mosquitto.Publisher;
import cloudnotes.server.mosquitto.Topics;

public class UserPortalService {
  private final NotesCacheInterface cacheNotes;
  private final UsersCacheInterface cacheUsers;
  private final Publisher publisher;
  private final Listener listener;

  public UserPortalService(NotesCacheInterface cacheNotes, UsersCacheInterface cacheUsers) {
    this.cacheNotes = cacheNotes;
    this.cacheUsers = cacheUsers;
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

    NotesResponse.Builder builder = 
      NotesResponse.newBuilder().setType(request.getType());
    try {
      request = request.toBuilder().setSender(publisher.getId()).build();
      cacheNotes.create(request);
      markAsSuccess(builder);
      
      publisher.publish(Topics.CREATE_NOTE, request.toByteArray());
    } catch (Exception e) {
      markAsFailure(builder);
    }
    return builder.build();
  }
  
  private NotesResponse handleUpdateNote(NotesRequest request) {
    System.out.println("handleUpdateNote()!");

    NotesResponse.Builder builder = 
      NotesResponse.newBuilder().setType(request.getType());
    try {
      request = request.toBuilder().setSender(publisher.getId()).build();
      cacheNotes.update(request);
      markAsSuccess(builder);
      
      publisher.publish(Topics.UPDATE_NOTE, request.toByteArray());
    } catch (Exception e) {
      markAsFailure(builder);
    }
    return builder.build();
  }
  
  private NotesResponse handleDeleteNote(NotesRequest request) {
    System.out.println("handleDeleteNote()!");

    NotesResponse.Builder builder = 
      NotesResponse.newBuilder().setType(request.getType());
    try {
      request = request.toBuilder().setSender(publisher.getId()).build();
      cacheNotes.delete(request);
      markAsSuccess(builder);
      
      publisher.publish(Topics.DELETE_NOTE, request.toByteArray());
    } catch (Exception e) {
      markAsFailure(builder);
    }
    return builder.build();
  }
  
  private NotesResponse handleGetNote(NotesRequest request) {
    System.out.println("handleGetNote()!");
    return NotesResponse.newBuilder()
        .setType(request.getType())
        .setStatus(
          OperationStatus.newBuilder()
            .setType(OperationStatus.StatusType.SUCCESS)
            .build())
        .addValues(cacheNotes.get(request))
        .build();
  }
  
  private NotesResponse handleGetAllNotes(NotesRequest request) {
    System.out.println("handleGetAllNotes()!");
    NotesCollection notesCollection = cacheNotes.getAll(request);
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
    listener.subscribe(Topics.CREATE_NOTE, (byte[] payload) -> {
      if (getSenderFromPayload(payload).equals(publisher.getId())) {
        return;
      }
      cacheNotes.create(NotesRequest.parseFrom(payload));
    });
    listener.subscribe(Topics.UPDATE_NOTE, (byte[] payload) -> {
      if (getSenderFromPayload(payload).equals(publisher.getId())) {
        return;
      }
      cacheNotes.update(NotesRequest.parseFrom(payload));
    });
    listener.subscribe(Topics.DELETE_NOTE, (byte[] payload) -> {
      if (getSenderFromPayload(payload).equals(publisher.getId())) {
        return;
      }
      cacheNotes.delete(NotesRequest.parseFrom(payload));
    });
    listener.subscribe(Topics.CREATE_USER, (byte[] payload) -> {
      cacheUsers.create(User.parseFrom(payload));
    });
    listener.subscribe(Topics.UPDATE_USER, (byte[] payload) -> {
      cacheUsers.update(User.parseFrom(payload));
    });
    listener.subscribe(Topics.DELETE_USER, (byte[] payload) -> {
      cacheUsers.delete(UserId.parseFrom(payload));
    });
  }

  private String getSenderFromPayload(byte[] payload) throws Exception {
    return NotesRequest.parseFrom(payload).getSender();
  }

  private void markAsSuccess(NotesResponse.Builder builder) {
    builder.setStatus(
      OperationStatus.newBuilder()
        .setType(OperationStatus.StatusType.SUCCESS)
        .build());
  }

  private void markAsFailure(NotesResponse.Builder builder) {
    builder.setStatus(
      OperationStatus.newBuilder()
        .setType(OperationStatus.StatusType.FAILED)
        .build());
  }
}
