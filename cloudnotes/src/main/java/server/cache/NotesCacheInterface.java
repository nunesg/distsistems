package cloudnotes.server;

import cloudnotes.proto.NotesRequest;
import cloudnotes.proto.Note;
import cloudnotes.proto.NotesCollection;

public interface NotesCacheInterface {
  public void create(NotesRequest notesRequest);
  
  public void update(NotesRequest notesRequest);
  
  public void delete(NotesRequest notesRequest);
  
  public Note get(NotesRequest notesRequest);

  public NotesCollection getAll(NotesRequest notesRequest);
}