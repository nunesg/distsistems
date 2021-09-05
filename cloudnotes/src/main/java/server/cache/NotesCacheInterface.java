package cloudnotes.server;

import cloudnotes.proto.NotesRequest;
import cloudnotes.proto.Note;
import cloudnotes.proto.NoteId;
import cloudnotes.proto.NotesCollection;

public interface NotesCacheInterface {
  public boolean has(NoteId id);

  public NoteId create(NotesRequest notesRequest);
  
  public void update(NotesRequest notesRequest);
  
  public void delete(NotesRequest notesRequest) throws Exception;
  
  public Note get(NotesRequest notesRequest);

  public NotesCollection getAll(NotesRequest notesRequest);
}