package cloudnotes.server;

import cloudnotes.server.NotesCacheInterface;

import cloudnotes.proto.NotesRequest;
import cloudnotes.proto.Note;
import cloudnotes.proto.NotesCollection;

public class NotesCacheManager implements NotesCacheInterface {
  public void create(NotesRequest notesRequest) {
    System.out.println("Notes cache manager create note!");
  }
  
  public void update(NotesRequest notesRequest) {
    System.out.println("Notes cache manager update note!");
  }
  
  public void delete(NotesRequest notesRequest) {
    System.out.println("Notes cache manager delete note!");
  }
  
  public Note get(NotesRequest notesRequest) {
    return Note.newBuilder().build();
  }

  public NotesCollection getAll(NotesRequest notesRequest) {
    return NotesCollection.newBuilder().build();
  }
}