package com.example.notepadapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


public class NotesContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.notepadapp.notes";
    public static final Uri CONTENT_URI_1 = Uri.parse("content://com.example.notepadapp.notes/NOTES_LIST");
    public static final Uri CONTENT_URI_2 = Uri.parse("content://com.example.notepadapp.notes/NOTES_COUNT");
    public static final String NOTES_COUNT = "NOTES_COUNT";
    public static final String NOTES_LIST = "NOTES_LIST";

    public static final int NOTES_LIST_CODE = 1;
    public static final int NOTES_COUNT_CODE = 2;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    public NotesDatabaseAdapter notesDatabaseAdapter;

    static {
        matcher.addURI(AUTHORITY, NOTES_LIST, NOTES_LIST_CODE);
        matcher.addURI(AUTHORITY, NOTES_COUNT, NOTES_COUNT_CODE);
    }

    public boolean onCreate() {
        this.notesDatabaseAdapter = NotesDatabaseAdapter.getNotesDatabaseAdapter(getContext());
        return false;
    }

    public String getType(Uri uri) {
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case NOTES_LIST_CODE:
                return this.notesDatabaseAdapter.delete(selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    public Uri insert(Uri uri, ContentValues values) {
        switch (matcher.match(uri)) {
            case NOTES_LIST_CODE:
                long id = this.notesDatabaseAdapter.insert(values);
                try {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return Uri.parse("content://com.example.notepad.notes/NOTES_LIST/" + id);
                } catch (Exception ex) {
                    Log.d("ContentProvide : ", "Unable to notify content resolver" + ex);
                    break;
                }
        }
        throw new IllegalArgumentException("Unsupported URI: " + uri);
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (matcher.match(uri)) {
            case NOTES_LIST_CODE:
                return this.notesDatabaseAdapter.getNotesList("NoSort");
            case NOTES_COUNT_CODE:
                return this.notesDatabaseAdapter.getNotesCount();
            default:
                return null;
        }
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case NOTES_LIST_CODE:
                return this.notesDatabaseAdapter.update(values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
