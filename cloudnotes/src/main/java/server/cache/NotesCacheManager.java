package cloudnotes.server;

import java.util.HashMap;
import com.google.protobuf.util.JsonFormat;

import cloudnotes.server.NotesCacheInterface;

import cloudnotes.proto.NotesRequest;
import cloudnotes.proto.Note;
import cloudnotes.proto.NoteEntry;
import cloudnotes.proto.NoteId;
import cloudnotes.proto.NotesCollection;
import cloudnotes.proto.UserId;

public class NotesCacheManager implements NotesCacheInterface {
  private HashMap<Integer, String> cache;
  private int lastId;

  public NotesCacheManager() {
    cache = new HashMap<Integer, String>();
    lastId = 0;
  }

  public void create(NotesRequest notesRequest) {
    NoteEntry entry = getNoteEntry(
      notesRequest.toBuilder()
        .setNote(
          notesRequest.getNote().toBuilder().setId(getNewId()).build())
        .build());
    System.out.println("Notes cache manager create note! JSON: " + toJson(entry));
    cache.put(entry.getNote().getId().getValue(), toJson(entry));
    System.out.println("get: " + cache.get(entry.getNote().getId().getValue()));
  }
  
  public void update(NotesRequest notesRequest) {
    System.out.println("Notes cache manager update note!");
    NoteEntry entry = getNoteEntry(notesRequest);
    cache.put(entry.getNote().getId().getValue(), toJson(entry));
  }
  
  public void delete(NotesRequest notesRequest) {
    System.out.println("Notes cache manager delete note " + notesRequest.getNote().getId().getValue() + "!");
    NoteEntry entry = fromJson(cache.get(notesRequest.getNote().getId().getValue()));
    if (entry.getUserId().getValue() == notesRequest.getUserId().getValue()) {
      cache.remove(notesRequest.getNote().getId().getValue());
      return;
    }
    System.out.println("Note was not removed. UserId didn't match.");
  }
  
  public Note get(NotesRequest notesRequest) {
    System.out.println("Notes cache manager get note! id: " + notesRequest.getNote().getId().getValue());
    String json = cache.get(notesRequest.getNote().getId().getValue());
    return fromJson(json).getNote();
  }

  public NotesCollection getAll(NotesRequest notesRequest) {
    System.out.println("Notes cache manager getAll! userId: " + notesRequest.getUserId().getValue());
    NotesCollection.Builder builder = NotesCollection.newBuilder();
    cache.forEach((k, v) -> {
      NoteEntry entry = fromJson(v);
      if (notesRequest.getUserId().getValue() == entry.getUserId().getValue()) {
        builder.addNotes(entry.getNote());
      }
    });
    return builder.build();
  }

  private NoteEntry getNoteEntry(NotesRequest req) {
    return NoteEntry.newBuilder()
      .setNote(req.getNote())
      .setUserId(req.getUserId())
      .build();
  }

  private NoteId getNewId() {
    return NoteId.newBuilder().setValue(lastId++).build();
  }

  private String toJson(NoteEntry entry) {
    String json = new String();
    try {
      json = JsonFormat.printer().print(entry);
    } catch (Exception e) {
      System.out.println("Error while converting message to json: " + entry.toString());
    }
    return json;
  }
  
  private NoteEntry fromJson(String json) {
    NoteEntry.Builder builder = NoteEntry.newBuilder();
    try {
      JsonFormat.parser().ignoringUnknownFields().merge(json, builder);
    } catch (Exception e) {
      System.out.println("Error while parsing json: " + json);
    }
    return builder.build();
  }
}