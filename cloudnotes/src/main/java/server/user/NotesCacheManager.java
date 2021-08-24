package cloudnotes.server;

import cloudnotes.server.NotesCacheInterface;

import cloudnotes.proto.NotesRequest;
import cloudnotes.proto.Note;
import cloudnotes.proto.NotesCollection;

public class NotesCacheManager implements NotesCacheInterface {
  public void create(NotesRequest notesRequest) {}
  
  public void update(NotesRequest notesRequest) {}
  
  public void delete(NotesRequest notesRequest) {}
  
  public Note get(NotesRequest notesRequest) {
    return Note.newBuilder().build();
  }

  public NotesCollection getAll(NotesRequest notesRequest) {
    return NotesCollection.newBuilder().build();
  }
}