package com.oceanscan.journal.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oceanscan.journal.app.data.DatabaseHelper;
import com.oceanscan.journal.app.model.Note;
import com.oceanscan.journal.app.utils.Constants;
import com.oceanscan.journal.app.utils.DateUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddEntryActivity extends AppCompatActivity {
    EditText title;
    EditText content;
    TextView updatedOn;
    private DatabaseHelper mDb;
    private static String TAG = AddEntryActivity.class.getSimpleName();
    String noteId;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        mDb = DatabaseHelper.getAppDatabase(this);

        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        updatedOn = (TextView) findViewById(R.id.updated_on);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //check if intent has extras
        if (getIntent().hasExtra(Constants.Extras.TITLE)) {
            title.setText(getIntent().getStringExtra(Constants.Extras.TITLE));
            content.setText(getIntent().getStringExtra(Constants.Extras.CONTENT));
            updatedOn.setText("Last updated: " + DateUtils.formatDate(AddEntryActivity.this, getIntent().getLongExtra(Constants.Extras.DATE_CREATED, 0)));
            updatedOn.setVisibility(View.VISIBLE);
            noteId = getIntent().getStringExtra(Constants.Extras.JOURNAL_ID);

        }


    }

    public void updateEntry(final String id, final String title, final String content) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... params) {

                mDb.notesDao().update(title, content, System.currentTimeMillis(), id);
//                List<Note> notes = mDb.notesDao().getAllNotes();
//                Log.i(TAG, "Number of records " + notes.size());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                //Toast.makeText(getApplicationContext(), "Note updated", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    public void saveEntry(final String title, final String content) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... params) {

                String noteId = UUID.randomUUID().toString();
                Note note = new Note(noteId, title, content, System.currentTimeMillis());
                createNewNoteFirebase(note);
                mDb.notesDao().createNewNote(note);
//                List<Note> notes = mDb.notesDao().getAllNotes();
//                Log.i(TAG, "Number of records " + notes.size());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();

                //Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void createNewNoteFirebase(Note note) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("notes").push().getKey();
        Map<String, Object> postValues = note.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + note.noteId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //save or update note when on back key is pressed
        if (getIntent().hasExtra(Constants.Extras.JOURNAL_ID)) {
            //update note
            Log.i(TAG, "Note id " + noteId);
            updateEntry(noteId,
                    title.getText().toString(),
                    content.getText().toString());

        } else if (!TextUtils.isEmpty(title.getText().toString())
                || !TextUtils.isEmpty(content.getText().toString())) {
            saveEntry(title.getText().toString(), content.getText().toString());

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            //save/update note
            if (TextUtils.isEmpty(content.getText().toString())
                    && TextUtils.isEmpty(title.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Can't save empty note", Toast.LENGTH_LONG).show();
            } else {
                if (getIntent().hasExtra(Constants.Extras.JOURNAL_ID)) {
                    //update note
                    updateEntry(noteId,
                            title.getText().toString(),
                            content.getText().toString());
                    return true;


                }
                saveEntry(title.getText().toString(), content.getText().toString());
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
