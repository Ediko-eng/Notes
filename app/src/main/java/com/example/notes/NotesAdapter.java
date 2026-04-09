package com.example.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> implements Filterable {

    private List<Note> notes;
    private List<Note> notesFull;
    private OnNoteClickListener listener;
    private boolean isSelectionMode = false;

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
        void onNoteLongClick(Note note);
        void onSelectionChanged(int count);
    }

    public NotesAdapter(List<Note> notes, OnNoteClickListener listener) {
        this.notes = notes;
        this.notesFull = new ArrayList<>(notes);
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note, listener, isSelectionMode);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        this.notesFull = new ArrayList<>(notes);
        notifyDataSetChanged();
    }

    public void setSelectionMode(boolean selectionMode) {
        isSelectionMode = selectionMode;
        if (!selectionMode) {
            for (Note note : notesFull) {
                note.setSelected(false);
            }
        }
        notifyDataSetChanged();
    }

    public boolean isSelectionMode() {
        return isSelectionMode;
    }

    public List<Note> getSelectedNotes() {
        List<Note> selected = new ArrayList<>();
        for (Note note : notesFull) {
            if (note.isSelected()) {
                selected.add(note);
            }
        }
        return selected;
    }

    public int getSelectedCount() {
        int count = 0;
        for (Note note : notesFull) {
            if (note.isSelected()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Filter getFilter() {
        return noteFilter;
    }

    private final Filter noteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(notesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Note note : notesFull) {
                    if (note.getTitle().toLowerCase().contains(filterPattern) ||
                            note.getContent().toLowerCase().contains(filterPattern)) {
                        filteredList.add(note);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notes = (List<Note>) results.values;
            notifyDataSetChanged();
        }
    };

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView contentTextView;
        private final ImageView pinImageView;
        private final MaterialCardView cardView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTitle);
            contentTextView = itemView.findViewById(R.id.textViewContent);
            pinImageView = itemView.findViewById(R.id.imageViewPin);
            cardView = itemView.findViewById(R.id.cardView);
        }

        public void bind(Note note, OnNoteClickListener listener, boolean isSelectionMode) {
            titleTextView.setText(note.getTitle());
            contentTextView.setText(note.getContent());
            pinImageView.setVisibility(note.isPinned() ? View.VISIBLE : View.GONE);
            cardView.setChecked(note.isSelected());

            itemView.setOnClickListener(v -> {
                if (isSelectionMode) {
                    note.setSelected(!note.isSelected());
                    cardView.setChecked(note.isSelected());
                    listener.onSelectionChanged(getSelectedCount());
                } else {
                    listener.onNoteClick(note);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (!isSelectionMode) {
                    listener.onNoteLongClick(note);
                }
                return true;
            });
        }
    }
}
