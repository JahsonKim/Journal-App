package com.oceanscan.journal.app.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.oceanscan.journal.app.model.Note;

import static com.oceanscan.journal.app.utils.Constants.DATABASE;

@Database(entities = {Note.class}, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {

    private static DatabaseHelper INSTANCE;

    public abstract NotesDao notesDao();

    public static DatabaseHelper getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseHelper.class, DATABASE)
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}