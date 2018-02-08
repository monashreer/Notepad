package com.example.notepadapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by monashreer on 08/02/18.
 */

public class NotesDatabaseAdapter {

    private Context context;
    private SQLiteDatabase sqliteDatabase;
    private static NotesDatabaseAdapter notesDatabaseAdapter;
    private static String sortOrder = "Nothing";

    private static String CREATE_TABLE = "CREATE TABLE notes(note_id INTEGER PRIMARY KEY, title TEXT NOT NULL, content TEXT, created DATETIME DEFAULT CURRENT_TIMESTAMP, modified DATETIME DEFAULT CURRENT_TIMESTAMP )";

    private static final String DB_NAME = "Notes_db";
    private static final String NOTES_TABLE = "notes";
    private static final String NOTE_CONTENT = "content";
    private static final String NOTE_CREATED_ON = "created";
    private static final String NOTE_ID = "note_id";
    private static final String NOTE_MODIFIED_ON = "modified";
    private static final String NOTE_TITLE = "title";



    public NotesDatabaseAdapter(Context context) {
        this.context = context;
        this.sqliteDatabase = new NotesDBHelper(context, DB_NAME, null, 1).getWritableDatabase();
    }

    public static NotesDatabaseAdapter getNotesDatabaseAdapter(Context context) {
        if (notesDatabaseAdapter == null) {
            return new NotesDatabaseAdapter(context);
        }
        return notesDatabaseAdapter;
    }

    public long insert(String title) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(Calendar.getInstance().getTime());
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_TITLE, title);
        contentValues.put(NOTE_CONTENT, "");
        contentValues.put(NOTE_CREATED_ON, timeStamp);
        contentValues.put(NOTE_MODIFIED_ON, timeStamp);
        return sqliteDatabase.insert(NOTES_TABLE, null, contentValues);
    }

    public long insert(String title, String content) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(Calendar.getInstance().getTime());
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_TITLE, title);
        contentValues.put(NOTE_CONTENT, content);
        contentValues.put(NOTE_CREATED_ON, timeStamp);
        contentValues.put(NOTE_MODIFIED_ON, timeStamp);
        return sqliteDatabase.insert(NOTES_TABLE, null, contentValues);
    }

    public long insert(ContentValues contentValues) {
        return sqliteDatabase.insert(NOTES_TABLE, null, contentValues);
    }

    public boolean delete(long id) {
        return sqliteDatabase.delete(NOTES_TABLE, new StringBuilder().append("note_id=").append(id).toString(), null) > 0;
    }

    public int delete(String select, String[] selectionArgs) {
        return sqliteDatabase.delete(NOTES_TABLE, select, selectionArgs);
    }

    public int update(long id, String title, String content) {
        ContentValues values = new ContentValues();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(Calendar.getInstance().getTime());
        values.put(NOTE_TITLE, title);
        values.put(NOTE_CONTENT, content);
        values.put(NOTE_MODIFIED_ON, timeStamp);
        return sqliteDatabase.update(NOTES_TABLE, values, "note_id=" + id, null);
    }

    public int update(ContentValues values, String where, String[] strings) {
        return sqliteDatabase.update(NOTES_TABLE, values, where, strings);
    }

    public Cursor getNotesList(String sortOption) {
        if (sortOption.equals("SortByName")) {
            sortOrder = sortOption;
            return this.sqliteDatabase.query(NOTES_TABLE, new String[]{NOTE_ID, NOTE_TITLE, NOTE_CONTENT, NOTE_CREATED_ON, NOTE_MODIFIED_ON}, null, null, null, null, "title COLLATE NOCASE ASC");
        } else if (sortOption.equals("SortByDate")) {
            sortOrder = sortOption;
            return this.sqliteDatabase.query(NOTES_TABLE, new String[]{NOTE_ID, NOTE_TITLE, NOTE_CONTENT, NOTE_CREATED_ON, NOTE_MODIFIED_ON}, null, null, null, null, "modified DESC");
        } else if (sortOrder.equals("SortByName")) {
            return this.sqliteDatabase.query(NOTES_TABLE, new String[]{NOTE_ID, NOTE_TITLE, NOTE_CONTENT, NOTE_CREATED_ON, NOTE_MODIFIED_ON}, null, null, null, null, "title COLLATE NOCASE ASC");
        } else {
            return this.sqliteDatabase.query(NOTES_TABLE, new String[]{NOTE_ID, NOTE_TITLE, NOTE_CONTENT, NOTE_CREATED_ON, NOTE_MODIFIED_ON}, null, null, null, null, "modified DESC");
        }
    }

    public Cursor getNotesCount() {
        return this.sqliteDatabase.rawQuery("SELECT COUNT(*) FROM notes", null);
    }

    private class NotesDBHelper extends SQLiteOpenHelper {
        public NotesDBHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(NotesDatabaseAdapter.CREATE_TABLE);
        }

        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        }
    }
}

