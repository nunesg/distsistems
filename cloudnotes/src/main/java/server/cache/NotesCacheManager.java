package cloudnotes.server;

import java.util.HashMap;
import com.google.protobuf.util.JsonFormat;

import cloudnotes.server.NotesCacheInterface;
import cloudnotes.server.RatisFacade;
import cloudnotes.proto.NotesRequest;
import cloudnotes.proto.Note;
import cloudnotes.proto.NoteId;
import cloudnotes.proto.NotesCollection;
import cloudnotes.proto.UserId;

public class NotesCacheManager implements NotesCacheInterface {
  private HashMap<String, String> cache;
  private RatisFacade db;
  private IdManager idManager;

  public NotesCacheManager() {
    cache = new HashMap<String, String>();
    idManager = new IdManager();
    db = RatisFacade.getInstance();
  }

  public boolean has(NoteId id) {
    return cache.containsKey(id.getValue());
  }

  public NoteId create(NotesRequest notesRequest) {
    Note note = notesRequest.getNote().toBuilder().setId(getNewId()).build();
    System.out.println("Notes cache manager create note! JSON: " + toJson(note));

    String key = note.getId().getValue();
    String val = toJson(note);
    cache.put(key, val);
    db.add(key, val);
    return note.getId();
  }
  
  public void update(NotesRequest notesRequest) throws Exception {
    System.out.println("Notes cache manager update note!");
    Note note = fromJson(cache.get(notesRequest.getNote().getId().getValue()));
    if (note.getUserId().getValue().equals(notesRequest.getNote().getUserId().getValue())) {
      String key = note.getId().getValue();
      String val = toJson(notesRequest.getNote());
      cache.put(key, val);
      db.add(key, val);
      return;
    }
    throw new Exception("Note was not updated. This note does not belong to the given user.");
  }
  
  public void delete(NotesRequest notesRequest) throws Exception {
    System.out.println("Notes cache manager delete note " + notesRequest.getNote().getId().getValue() + "!");
    Note note = fromJson(cache.get(notesRequest.getNote().getId().getValue()));
    if (note.getUserId().getValue().equals(notesRequest.getNote().getUserId().getValue())) {
      String key = note.getId().getValue();
      cache.remove(key);
      db.remove(key);
      return;
    }
    throw new Exception("Note was not removed. UserId did not match.");
  }
  
  public Note get(NotesRequest notesRequest) {
    System.out.println("Notes cache manager get note! id: " + notesRequest.getNote().getId().getValue());
    // String json = cache.get(notesRequest.getNote().getId().getValue());
    String json = db.get(notesRequest.getNote().getId().getValue());
    return fromJson(json);
  }

  public NotesCollection getAll(NotesRequest notesRequest) {
    System.out.println("Notes cache manager getAll! userId: " + notesRequest.getNote().getUserId().getValue());
    NotesCollection.Builder builder = NotesCollection.newBuilder();
    // cache.forEach((k, v) -> {
    db.getAll().forEach((k, v) -> {
      Note note = fromJson(v);
      if (notesRequest.getNote().getUserId().getValue().equals(note.getUserId().getValue())) {
        builder.addNotes(note);
      }
    });
    return builder.build();
  }

  private NoteId getNewId() {
    return NoteId.newBuilder().setValue(idManager.create()).build();
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