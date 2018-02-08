package com.example.notepadapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by monashreer on 08/02/18.
 */

public class NotesListFragment extends Fragment {
    private Notes[] notes;
    private NotesDatabaseAdapter notesDatabaseAdapter;
    private RecyclerView recyclerView;
    private TextView textView;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_list_fragment, container, false);
        this.notesDatabaseAdapter = NotesDatabaseAdapter.getNotesDatabaseAdapter(getContext());
        this.recyclerView = view.findViewById(R.id.recyclerView);
        this.textView = view.findViewById(R.id.emptyListTV);
        this.notes = getNotesArray(this.notesDatabaseAdapter.getNotesList("No_Sort"));
        populateList();
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton fab =  getActivity().findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                NotesListFragment.this.getFragmentManager().beginTransaction().replace(R.id.frame, new AddNoteFragment()).addToBackStack("AddNoteFragment").commit();
            }
        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sortDate) {
            this.notes = getNotesArray(this.notesDatabaseAdapter.getNotesList("SortByDate"));
            populateList();
            return true;
        } else if (id != R.id.action_sortName) {
            return super.onOptionsItemSelected(item);
        } else {
            this.notes = getNotesArray(this.notesDatabaseAdapter.getNotesList("SortByName"));
            populateList();
            return true;
        }
    }

    public void populateList() {
        if (this.notes != null) {
            this.recyclerView.setVisibility(View.VISIBLE);
            this.textView.setVisibility(View.INVISIBLE);
            CustomRecyclerViewAdapter customRecyclerViewAdapter = new CustomRecyclerViewAdapter(getContext(), this, this.notes);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            this.recyclerView.addItemDecoration(new DividerItemDecoration(this.recyclerView.getContext(), layoutManager.getOrientation()));
            this.recyclerView.setLayoutManager(layoutManager);
            this.recyclerView.setAdapter(customRecyclerViewAdapter);
            return;
        }
        this.recyclerView.setVisibility(View.INVISIBLE);
        this.textView.setVisibility(View.VISIBLE);
    }

    private Notes[] getNotesArray(Cursor cursor) {
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        int i = 0;
        Notes[] notesArr = new Notes[cursor.getCount()];
        while (cursor.moveToNext()) {
            notesArr[i] = new Notes(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            i++;
        }
        return notesArr;
    }
}
