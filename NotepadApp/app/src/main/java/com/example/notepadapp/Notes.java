package com.example.notepadapp;

import java.io.Serializable;

/**
 * Created by monashreer on 08/02/18.
 */

public class Notes implements Serializable {
    private String createdDate;
    private String modifiedDate;
    private String noteContent;
    private long noteId;
    private String noteTitle;

    public Notes(long noteId, String noteTitle, String noteContent, String createdDate, String modifiedDate) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public long getNoteId() {
        return this.noteId;
    }

    public String getNoteTitle() {
        return this.noteTitle;
    }

    public String getNoteContent() {
        return this.noteContent;
    }

    public String getModifiedDate() {
        return this.modifiedDate;
    }
}

