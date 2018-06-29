package com.oceanscan.journal.app.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.oceanscan.journal.app.model.Note;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM Notes")
    List<Note> getAllNotes();

    @Query("SELECT * FROM Notes where noteId = :noteId order by updated desc")
    Note getNoteById(int noteId);


    @Insert
    void createNewNote(Note... notes);

    @Delete
    void delete(Note note);
    @Query("UPDATE Notes SET title=:title,content=:content,updated=:updatedOn WHERE noteId = :noteId")
    void update(String title,String content,long updatedOn, String noteId);
}
