package it.thomas.todoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;
import java.util.Objects;

import it.thomas.todoapplication.Entity.Note;
import it.thomas.todoapplication.databinding.ActivityAddNoteBinding;

public class AddNote extends AppCompatActivity {
    private ActivityAddNoteBinding binding;
    private Note note;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.saveToolbar);
        initUI();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null){
            //Update note
            note = (Note) intent.getSerializableExtra("note");
            if (note != null) {
                binding.etTitle.setText(note.getTitle());
                binding.etDescription.setText(note.getDescription());
                binding.numberPicker.setValue(note.getPriority());
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note) {
            saveNote();
        }
        return true;
    }

    private void saveNote() {
        String title = binding.etTitle.getText().toString();
        String desc = binding.etDescription.getText().toString();
        int priority = binding.numberPicker.getValue();
        if (!title.isEmpty() && !desc.isEmpty()) {
            if (note == null) {
                note = new Note(title, desc, priority);
            } else {
                note.setTitle(title);
                note.setDescription(desc);
                note.setPriority(priority);
            }
            Intent intent = new Intent();
            intent.putExtra("note", note);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Fill in the blanks ... ", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        binding.numberPicker.setMaxValue(10);
        binding.numberPicker.setMinValue(0);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_arrow);
        
    }
}