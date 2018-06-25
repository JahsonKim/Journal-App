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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oceanscan.journal.app.data.DatabaseHelper;
import com.oceanscan.journal.app.model.Note;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddEntryActivity extends AppCompatActivity {
    EditText title;
    EditText content;
    TextView updatedOn;
    private DatabaseHelper mDb;
    private static String TAG = AddEntryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        mDb = DatabaseHelper.getAppDatabase(this);

        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        updatedOn = (TextView) findViewById(R.id.updated_on);

    }

    public void updateEntry(int id, final String title, final String content) {

    }

    public void saveEntry(final String title, final String content) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... params) {

                String noteId = UUID.randomUUID().toString();

                mDb.notesDao().createNewNote(new Note(noteId, title, content, System.currentTimeMillis()));
                List<Note> notes = mDb.notesDao().getAllNotes();
                Log.i(TAG, "Number of records " + notes.size());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Note updated", Toast.LENGTH_SHORT).show();
            }
        }.execute();
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
                saveEntry(title.getText().toString(), content.getText().toString());
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
