package com.oceanscan.journal.app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.oceanscan.journal.app.adapter.NotesAdapter;
import com.oceanscan.journal.app.data.DatabaseHelper;
import com.oceanscan.journal.app.model.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView noteRecyclerView;
    private DatabaseHelper mDb;
    FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDb = DatabaseHelper.getAppDatabase(this);


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

      List<Note> notes = new ArrayList<>();
        notes = mDb.notesDao().getAllNotes();
        noteRecyclerView.setAdapter(new NotesAdapter(getApplicationContext(), notes));

        noteRecyclerView.addOnItemTouchListener(new NotesAdapter.RecyclerTouchListener(getApplicationContext(), noteRecyclerView, new NotesAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}
