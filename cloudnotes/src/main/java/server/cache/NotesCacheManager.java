package cloudnotes.server;

import java.util.HashMap;
import com.google.protobuf.util.JsonFormat;

import cloudnotes.server.NotesCacheInterface;

import cloudnotes.proto.NotesRequest;
import cloudnotes.proto.Note;
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

  public boolean has(NoteId id) {
    return cache.containsKey(id.getValue());
  }

  public void create(NotesRequest notesRequest) {
    Note note = notesRequest.getNote().toBuilder().setId(getNewId()).build();
    System.out.println("Notes cache manager create note! JSON: " + toJson(note));
    cache.put(note.getId().getValue(), toJson(note));
    System.out.println("get: " + cache.get(note.getId().getValue()));
  }
  
  public void update(NotesRequest notesRequest) {
    System.out.println("Notes cache manager update note!");
    cache.put(notesRequest.getNote().getId().getValue(), toJson(notesRequest.getNote()));
  }
  
  public void delete(NotesRequest notesRequest) {
    System.out.println("Notes cache manager delete note " + notesRequest.getNote().getId().getValue() + "!");
    Note note = fromJson(cache.get(notesRequest.getNote().getId().getValue()));
    if (note.getUserId().getValue() == notesRequest.getNote().getUserId().getValue()) {
      cache.remove(note.getId().getValue());
      return;
    }
    System.out.println("Note was not removed. UserId didn't match.");
  }
  
  public Note get(NotesRequest notesRequest) {
    System.out.println("Notes cache manager get note! id: " + notesRequest.getNote().getId().getValue());
    String json = cache.get(notesRequest.getNote().getId().getValue());
    return fromJson(json);
  }

  public NotesCollection getAll(NotesRequest notesRequest) {
    System.out.println("Notes cache manager getAll! userId: " + notesRequest.getNote().getUserId().getValue());
    NotesCollection.Builder builder = NotesCollection.newBuilder();
    cache.forEach((k, v) -> {
      Note note = fromJson(v);
      if (notesRequest.getNote().getUserId().getValue() == note.getUserId().getValue()) {
        builder.addNotes(note);
      }
    });
    return builder.build();
  }

  private NoteId getNewId() {
    return NoteId.newBuilder().setValue(lastId++).build();
  }

  private String toJson(Note entry) {
    String json = new String();
    try {
      json = JsonFormat.printer().print(entry);
    } catch (Exception e) {
      System.out.println("Error while converting message to json: " + entry.toString());
    }
    return json;
  }
  
  private Note fromJson(String json) {
    Note.Builder builder = Note.newBuilder();
    try {
      JsonFormat.parser().ignoringUnknownFields().merge(json, builder);
    } catch (Exception e) {
      System.out.println("Error while parsing json: " + json);
    }
    return builder.build();
  }
}