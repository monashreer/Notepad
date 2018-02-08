package com.example.notepadapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by monashreer on 08/02/18.
 */

public class CustomRecyclerViewAdapter extends Adapter<CustomRecyclerViewAdapter.NotesViewHolder> {
    private Context context;
    private Fragment fragment;
    private Notes[] notes;

    public CustomRecyclerViewAdapter(Context context, Fragment activity, Notes[] notes) {
        this.context = context;
        this.notes = notes;
        this.fragment = activity;
    }

    public int getItemCount() {
        return this.notes.length;
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        Notes selectedNote = this.notes[position];
        holder.titleView.setText(selectedNote.getNoteTitle());
        holder.noteContentView.setText(selectedNote.getNoteContent());
        holder.dateView.setText(setDateView(selectedNote.getModifiedDate()));

    }

    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.note_list_view, null);
        final NotesViewHolder notesViewHolder = new NotesViewHolder(view);
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Notes selectedNote = notes[notesViewHolder.getAdapterPosition()];
                fragment.getFragmentManager().beginTransaction().
                        replace(R.id.frame, ReadNoteFragment.newInstance(selectedNote)).
                        addToBackStack("ReadNoteFragment").
                        commit();
            }
        });
        return notesViewHolder;
    }


    private String setDateView(String date) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(Calendar.getInstance().getTime());
        if (timeStamp.substring(0, 10).equals(date.substring(0, 10))) {
            return date.substring(11, 16);
        }
        if (timeStamp.substring(6, 10).equals(date.substring(6, 10))) {
            return date.substring(8, 10) + "/" + date.substring(5, 7);
        }
        return date.substring(8, 10) + "/" + date.substring(5, 7) + "/" + date.substring(0, 4);
    }

    public class NotesViewHolder extends ViewHolder {
        private TextView dateView;
        private TextView noteContentView;
        private TextView titleView;

        private NotesViewHolder(View view) {
            super(view);
            this.dateView = view.findViewById(R.id.date);
            this.titleView =  view.findViewById(R.id.title);
            this.noteContentView = view.findViewById(R.id.content);
        }
    }
}

