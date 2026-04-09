package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesAdapter.OnNoteClickListener {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Note> notesList;
    private LinearLayout emptyState;
    private ExtendedFloatingActionButton fab;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        notesList = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emptyState = findViewById(R.id.emptyState);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesAdapter(notesList, this);
        recyclerView.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivityForResult(intent, 1);
        });

        setupSwipeToDelete();
        loadNotes();
    }

    private void setupSwipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Note note = notesList.get(position);
                dbHelper.deleteNote(note.getId());
                notesList.remove(position);
                adapter.notifyItemRemoved(position);
                checkEmptyState();
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void loadNotes() {
        notesList = dbHelper.getAllNotes();
        adapter.setNotes(notesList);
        checkEmptyState();
    }

    private void checkEmptyState() {
        if (notesList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem deleteItem = menu.findItem(R.id.action_delete_selected);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        boolean isSelectionMode = adapter != null && adapter.isSelectionMode();
        
        deleteItem.setVisible(isSelectionMode);
        searchItem.setVisible(!isSelectionMode);
        
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_selected) {
            deleteSelectedNotes();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSelectedNotes() {
        List<Note> selectedNotes = adapter.getSelectedNotes();
        if (selectedNotes.isEmpty()) return;

        new AlertDialog.Builder(this)
                .setTitle("Delete Notes")
                .setMessage("Delete " + selectedNotes.size() + " selected notes?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    for (Note note : selectedNotes) {
                        dbHelper.deleteNote(note.getId());
                    }
                    adapter.setSelectionMode(false);
                    invalidateOptionsMenu();
                    loadNotes();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
        intent.putExtra("note_id", note.getId());
        intent.putExtra("note_title", note.getTitle());
        intent.putExtra("note_content", note.getContent());
        intent.putExtra("note_pinned", note.isPinned());
        startActivityForResult(intent, 1);
    }

    @Override
    public void onNoteLongClick(Note note) {
        adapter.setSelectionMode(true);
        note.setSelected(true);
        adapter.notifyDataSetChanged();
        invalidateOptionsMenu();
    }

    @Override
    public void onSelectionChanged(int count) {
        if (count == 0) {
            adapter.setSelectionMode(false);
            invalidateOptionsMenu();
        } else {
            toolbar.setTitle(count + " selected");
        }
    }

    @Override
    public void onBackPressed() {
        if (adapter.isSelectionMode()) {
            adapter.setSelectionMode(false);
            toolbar.setTitle("Notes");
            invalidateOptionsMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadNotes();
        }
    }
}