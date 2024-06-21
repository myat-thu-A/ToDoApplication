package it.thomas.todoapplication.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.thomas.todoapplication.Entity.Note;

@Dao
public interface NoteDao {
    @Insert
    void addNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM notes ORDER BY priority DESC")
    List<Note> getAllNotes();

    @Query("DELETE FROM notes")
    void deleteAll();
}
