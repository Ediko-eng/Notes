package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText;
    private Button saveButton, backButton, deleteButton, pinButton, shareButton;
    private DatabaseHelper dbHelper;
    private int noteId = -1;
    private boolean isPinned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);
        deleteButton = findViewById(R.id.deleteButton);
        pinButton = findViewById(R.id.pinButton);
        shareButton = findViewById(R.id.shareButton);
        dbHelper = new DatabaseHelper(this);

        noteId = getIntent().getIntExtra("note_id", -1);
        if (noteId != -1) {
            String title = getIntent().getStringExtra("note_title");
            String content = getIntent().getStringExtra("note_content");
            isPinned = getIntent().getBooleanExtra("note_pinned", false);
            titleEditText.setText(title);
            contentEditText.setText(content);
            updatePinButton();
            setTitle("Edit Note");
            deleteButton.setVisibility(View.VISIBLE);
            shareButton.setVisibility(View.VISIBLE);
        } else {
            setTitle("New Note");
            deleteButton.setVisibility(View.GONE);
            shareButton.setVisibility(View.GONE);
        }

        backButton.setOnClickListener(v -> finish());

        shareButton.setOnClickListener(v -> shareNote());

        pinButton.setOnClickListener(v -> {
            isPinned = !isPinned;
            updatePinButton();
        });

        deleteButton.setOnClickListener(v -> {
            if (noteId != -1) {
                dbHelper.deleteNote(noteId);
                Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String content = contentEditText.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(AddNoteActivity.this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (noteId == -1) {
                Note note = new Note(title, content);
                note.setPinned(isPinned);
                dbHelper.insertNote(note);
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
            } else {
                Note note = new Note(noteId, title, content, System.currentTimeMillis(), isPinned);
                dbHelper.updateNote(note);
                Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
            }
            setResult(RESULT_OK);
            finish();
        });
    }

    private void shareNote() {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        String shareText = title + "\n\n" + content;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, "Share Note via"));
    }

    private void updatePinButton() {
        if (isPinned) {
            pinButton.setText("Pinned");
            pinButton.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_star_big_on, 0, 0, 0);
        } else {
            pinButton.setText("Pin");
            pinButton.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_star_big_off, 0, 0, 0);
        }
    }
}