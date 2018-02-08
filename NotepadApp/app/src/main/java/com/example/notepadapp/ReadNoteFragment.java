package com.example.notepadapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by monashreer on 08/02/18.
 */



public class ReadNoteFragment extends Fragment implements View.OnClickListener{
    public static Notes selectedNote;
    private NotesDatabaseAdapter notesDatabaseAdapter;

    public static ReadNoteFragment newInstance(Notes selectedNote1) {
        Bundle args = new Bundle();
        ReadNoteFragment fragment = new ReadNoteFragment();
        selectedNote = selectedNote1;
        return fragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.notesDatabaseAdapter = NotesDatabaseAdapter.getNotesDatabaseAdapter(getContext());
        return inflater.inflate(R.layout.fragment_read_note, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView noteContentView =  getActivity().findViewById(R.id.read_content);
        TextView noteTitleView = getActivity().findViewById(R.id.read_title);
        noteContentView.setText(selectedNote.getNoteContent());
        noteTitleView.setText(selectedNote.getNoteTitle());

        FloatingActionButton delete, edit;
        delete = getActivity().findViewById(R.id.deleteButton);
        edit = getActivity().findViewById(R.id.editButton);

        delete.setOnClickListener(this);
        edit.setOnClickListener(this);
    }



    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.deleteButton:
                Builder builder1 = new Builder(getContext());
                builder1.setMessage( R.string.delete_note_confirmation);
                builder1.setCancelable(true);
                builder1.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        ReadNoteFragment.this.notesDatabaseAdapter.delete(ReadNoteFragment.selectedNote.getNoteId());
                        Toast.makeText(ReadNoteFragment.this.getContext(), "Deleted the node : " + ReadNoteFragment.selectedNote.getNoteTitle(), Toast.LENGTH_SHORT).show();
                        ReadNoteFragment.this.getFragmentManager().beginTransaction().replace(R.id.frame, new NotesListFragment()).commit();

                    }
                });
                builder1.setNegativeButton( "No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder1.create().show();

            case R.id.editButton:
                ReadNoteFragment.this.getFragmentManager().
                        beginTransaction().
                        replace(R.id.frame, EditNoteFragment.newInstance(ReadNoteFragment.selectedNote)).
                        addToBackStack("EditNoteFragment").
                        commit();

        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.read_note_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.shareNote) {
            Intent shareIntent = new Intent("android.intent.action.SEND");
            shareIntent.setType("text/plain");
            shareIntent.putExtra("android.intent.extra.SUBJECT", "Note titled : " + selectedNote.getNoteTitle());
            shareIntent.putExtra("android.intent.extra.TEXT", selectedNote.getNoteTitle() + "\n" + selectedNote.getNoteContent());
            startActivity(Intent.createChooser(shareIntent, "Sharing via"));
        }
        return super.onOptionsItemSelected(item);
    }
}

