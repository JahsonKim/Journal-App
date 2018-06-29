package com.oceanscan.journal.app.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "Notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "noteId")
    public String noteId;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "content")
    public String content;
    @ColumnInfo(name = "updated")
    private long updatedOn;


    public Note() {
    }

    public Note(String noteId, String title, String content, long updatedOn) {

        this.setNoteId(noteId);
        this.setTitle(title);
        this.setContent(content);
        this.setUpdatedOn(updatedOn);
    }

//    public Note(int noteId,String title, String content, String updatedOn) {
//        this.noteId=noteId;
//        this.setTitle(title);
//        this.setContent(content);
//        this.setUpdatedOn(updatedOn);
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public long getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(long updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("noteId", noteId);
        result.put("title", title);
        result.put("content", content);
        result.put("updated", updatedOn);

        return result;
    }
}
