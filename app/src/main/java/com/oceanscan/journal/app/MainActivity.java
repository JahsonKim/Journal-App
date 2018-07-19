package com.oceanscan.journal.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.oceanscan.journal.app.adapter.NotesAdapter;
import com.oceanscan.journal.app.data.DatabaseHelper;
import com.oceanscan.journal.app.data.MyPreferences;
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
    private FirebaseAuth mAuth;
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleSignInClient mGoogleSignInClient;
    MyPreferences preferences;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDb = DatabaseHelper.getAppDatabase(this);
        preferences = new MyPreferences(MainActivity.this);


        //TODO use esspresso
        //TODO fine tune the app intro flipper

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.CLIENT_ID)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        user = mAuth.getCurrentUser();
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
        //notes = mDb.notesDao().getAllNotes();


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

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("notes").child(user.getUid());

//        Query queryRef = myRef.child(user.getUid()).orderByChild("title").equalTo("Croatia vs Denmark")
//                .orderByChild("id").equalTo(1);

                //.orderByChild("title").equalTo("Croatia vs Denmark");
       // myRef.child(user.getUid());
//
//        if (database != null)
//            database.setPersistenceEnabled(true);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!notes.isEmpty())
                    notes.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Note value = postSnapshot.getValue(Note.class);
                    Log.i(TAG, "Value is: " + value.noteId);
                    notes.add(value);
                }
                noteRecyclerView.setAdapter(new NotesAdapter(getApplicationContext(), notes));


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i(TAG, "Failed to read value.", error.toException());
            }
        });
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Note note = dataSnapshot.getValue(Note.class);

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Note newNote = dataSnapshot.getValue(Note.class);
                Log.i(TAG, "Note " + newNote.title);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.i(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                Note note = dataSnapshot.getValue(Note.class);
                Log.i(TAG, "Note " + note.title);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        myRef.addChildEventListener(childEventListener);

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
            signOut();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Logging out");
        dialog.show();
        // Firebase sign out
        mAuth.signOut();
        preferences.setFirstTimeLaunch(true);
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //
                        dialog.dismiss();
                        finish();
                        Toast.makeText(getApplicationContext(), "Logged out!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                    }
                });
    }
}
