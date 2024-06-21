package it.thomas.todoapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.thomas.todoapplication.Adapter.NoteAdapter;
import it.thomas.todoapplication.Adapter.onItemClickListener;
import it.thomas.todoapplication.Dao.NoteDao;
import it.thomas.todoapplication.Database.NoteDatabase;
import it.thomas.todoapplication.Entity.Note;
import it.thomas.todoapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements onItemClickListener {
    private ActivityMainBinding binding;
    private NoteDao noteDao;
    private NoteAdapter noteAdapter;
    private List<Note> notes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        initDatabase();
        initListener();
    }

    private void initListener() {
        binding.fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNote.class);
            startActivityForResult(intent, 123);
        });

        deleteNote();


    }

    private void deleteNote() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteDao.deleteNote(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(binding.rvNotes);
        refreshNotes();
    }

    private void initDatabase() {
        noteDao = NoteDatabase.getInstance(this).getNoteDao();
        noteAdapter = new NoteAdapter();
        noteAdapter.setListener(this);
        binding.rvNotes.setAdapter(noteAdapter);
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(this));
        refreshNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_delete_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.notes_delete) {
            noteDao.deleteAll();
            refreshNotes();
        }
        return true;
    }

    private void refreshNotes() {
        notes = noteDao.getAllNotes();
        noteAdapter.setNoteList(notes);
    }

    @Override
    public void onClick(Note note) {
        Intent intent = new Intent(this, AddNote.class);
        intent.putExtra("note", note);
        startActivityForResult(intent, 234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 123 && data != null) {
                Note note;
                //Add New note;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    note = data.getSerializableExtra("note", Note.class);
                } else {
                    note = (Note) data.getSerializableExtra("note");
                }
                noteDao.addNote(note);
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
            } else if (requestCode == 234) {
                //Update Existing Note
                Note note;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    assert data != null;
                    note = data.getSerializableExtra("note", Note.class);
                } else {
                    assert data != null;
                    note = (Note) data.getSerializableExtra("note");
                }
                noteDao.updateNote(note);
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
            }
            refreshNotes();
        }
    }
}