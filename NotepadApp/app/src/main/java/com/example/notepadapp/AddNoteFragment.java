package com.example.notepadapp;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by monashreer on 08/02/18.
 */

public class AddNoteFragment extends Fragment {
    private EditText content;
    private NotesDatabaseAdapter notesDatabaseAdapter;
    private EditText title;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.notesDatabaseAdapter = NotesDatabaseAdapter.getNotesDatabaseAdapter(getContext());
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.title = getActivity().findViewById(R.id.note_Title);
        this.content = getActivity().findViewById(R.id.note_Content);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.save_menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.saveOption) {
            return false;
        }
        final String titleValue = this.title.getText().toString();
        final String contentValue = this.content.getText().toString();
        if (titleValue.isEmpty()) {
            Toast.makeText(getContext(), R.string.save_note_without_title, Toast.LENGTH_SHORT).show();
            return false;
        }
        Builder builder1 = new Builder(getContext());
        builder1.setMessage(R.string.save_note_confirmation);
        builder1.setCancelable(true);
        builder1.setPositiveButton( "Yes", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                File myDir = new File(Environment.getExternalStorageDirectory(), "/MyNotes");
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }
                File myFile = new File(myDir, titleValue + ".txt");
                try {
                    myFile.createNewFile();
                    FileWriter fileWriter = new FileWriter(myFile, true);
                    fileWriter.append(contentValue);
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (contentValue.isEmpty()) {
                    AddNoteFragment.this.notesDatabaseAdapter.insert(titleValue);
                } else {
                    AddNoteFragment.this.notesDatabaseAdapter.insert(titleValue, contentValue);
                }
                Toast.makeText(AddNoteFragment.this.getContext(), "Note is saved now  (.txt : " + myDir.getPath() + " )", Toast.LENGTH_SHORT).show();
                AddNoteFragment.this.getFragmentManager().beginTransaction().replace(R.id.frame, new NotesListFragment()).commit();
            }
        });
        builder1.setNegativeButton("No", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                if (contentValue.isEmpty()) {
                    AddNoteFragment.this.notesDatabaseAdapter.insert(titleValue);
                } else {
                    AddNoteFragment.this.notesDatabaseAdapter.insert(titleValue, contentValue);
                }
                Toast.makeText(AddNoteFragment.this.getContext(), R.string.note_saved, Toast.LENGTH_SHORT).show();
                AddNoteFragment.this.getFragmentManager().beginTransaction().replace(R.id.frame, new NotesListFragment()).commit();
            }
        });
        builder1.create().show();
        return true;
    }
}

