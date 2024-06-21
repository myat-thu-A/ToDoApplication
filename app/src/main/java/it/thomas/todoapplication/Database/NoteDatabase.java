package it.thomas.todoapplication.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import it.thomas.todoapplication.Dao.NoteDao;
import it.thomas.todoapplication.Entity.Note;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao getNoteDao();
    private static NoteDatabase noteDatabase;
    private static final String DATABASE_NAME = "todo_app";

    public static synchronized NoteDatabase getInstance(Context context) {
        if (noteDatabase == null) {
            noteDatabase = Room.databaseBuilder(context, NoteDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        }
        return noteDatabase;
    }

}
