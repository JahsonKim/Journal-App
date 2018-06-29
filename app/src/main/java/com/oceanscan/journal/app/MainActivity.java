package com.oceanscan.journal.app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.oceanscan.journal.app.adapter.NotesAdapter;
import com.oceanscan.journal.app.data.DatabaseHelper;
import com.oceanscan.journal.app.model.Note;
import com.oceanscan.journal.app.utils.Constants;
import com.oceanscan.journal.app.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView noteRecyclerView;
    private DatabaseHelper mDb;
    FloatingActionButton add;
    List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDb = DatabaseHelper.getAppDatabase(this);

        //TODO refresh notes list after adding.
        //TODO connect to firebase db
        //TODO use esspresso
        //TODO fine tune the app intro flipper

        add = (FloatingActionButton) findViewById(R.id.add_note);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddEntryActivity.class));
            }
        });
        noteRecyclerView = (RecyclerView) findViewById(R.id.notes_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        noteRecyclerView.setLayoutManager(layoutManager);
        noteRecyclerView.setItemAnimator(new DefaultItemAnimator());
        noteRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(MainActivity.this));

        notes = new ArrayList<>();
        notes = mDb.notesDao().getAllNotes();
        noteRecyclerView.setAdapter(new NotesAdapter(getApplicationContext(), notes));

        noteRecyclerView.addOnItemTouchListener(new NotesAdapter.RecyclerTouchListener(getApplicationContext(), noteRecyclerView, new NotesAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Note note = notes.get(position);
                Intent intent = new Intent(getApplicationContext(), AddEntryActivity.class);
                intent.putExtra(Constants.Extras.TITLE, note.title);
                intent.putExtra(Constants.Extras.CONTENT, note.content);
                intent.putExtra(Constants.Extras.JOURNAL_ID, note.noteId);
                intent.putExtra(Constants.Extras.DATE_CREATED, note.getUpdatedOn());
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            //logout


            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
