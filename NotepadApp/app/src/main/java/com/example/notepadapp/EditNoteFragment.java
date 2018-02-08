package com.example.notepadapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by monashreer on 08/02/18.
 */

public class EditNoteFragment extends Fragment {
    private EditText content;
    private Notes editNote;
    private NotesDatabaseAdapter notesDatabaseAdapter;
    private EditText title;

    public static EditNoteFragment newInstance(Notes editNote) {
        EditNoteFragment fragment = new EditNoteFragment();
        fragment.editNote = editNote;
        return fragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.notesDatabaseAdapter = NotesDatabaseAdapter.getNotesDatabaseAdapter(getContext());
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        this.title =  getActivity().findViewById(R.id.note_Title);
        this.content =  getActivity().findViewById(R.id.note_Content);
        this.title.setText(this.editNote.getNoteTitle());
        this.content.setText(this.editNote.getNoteContent());
        super.onActivityCreated(savedInstanceState);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.edit_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.editOption) {
            return false;
        }
        String titleValue = this.title.getText().toString();
        String contentValue = this.content.getText().toString();
        if (titleValue.equals(this.editNote.getNoteTitle()) && contentValue.equals(this.editNote.getNoteContent())) {
            Toast.makeText(getContext(), R.string.note_null_update_toast, Toast.LENGTH_SHORT).show();
        } else if (titleValue.isEmpty()) {
            Toast.makeText(getContext(), R.string.note_update_without_title, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            this.notesDatabaseAdapter.update(this.editNote.getNoteId(), titleValue, contentValue);
            Toast.makeText(getContext(), R.string.note_updated_toast, Toast.LENGTH_SHORT).show();
        }
        getFragmentManager().beginTransaction().replace(R.id.frame, new NotesListFragment()).commit();
        return true;
    }
}

