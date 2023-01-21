package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.buzzercode.fazaiaschoolsandcolleges.DataFiles.Note;
import com.buzzercode.fazaiaschoolsandcolleges.DataFiles.NoteAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.NAME;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.SHARED_PREFS;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.TYPE;

public class NoteActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String name = sharedPreferences.getString(NAME, null);
        final String type = sharedPreferences.getString(TYPE, null);

        final ProgressBar loading_notes = findViewById(R.id.loading_notes);
        final TextView no_notes = findViewById(R.id.no_notes);
        loading_notes.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Note> notes = new ArrayList<Note>();
        if (type == "teacher") {
            db.collection("School").document("Accouts").collection("teachers").document(name).collection("notes").orderBy("timestamp").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                loading_notes.setVisibility(View.GONE);
                                if (task.getResult().isEmpty()) {
                                    no_notes.setVisibility(View.VISIBLE);
                                }
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> note_info = document.getData();
                                    String heading = (String) note_info.get("heading");
                                    String description = (String) note_info.get("description");
                                    String time = (String) note_info.get("time");
                                    String priority = (String) note_info.get("priority");
                                    long timestamp = (long) note_info.get("timestamp");
                                    String doc_id = (String) document.getId();

                                    notes.add(new Note(heading, description, time, priority, timestamp, doc_id));
                                    NoteAdapter adapter = new NoteAdapter(NoteActivity.this, notes);

                                    ListView listView = (ListView) findViewById(R.id.list);
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            String clicked_id = notes.get(position).getmId();
                                            delete_note("teachers", clicked_id);
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            db.collection("School").document("Accouts").collection("students").document(name).collection("notes").orderBy("timestamp").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                loading_notes.setVisibility(View.GONE);
                                if (task.getResult().isEmpty()) {
                                    no_notes.setVisibility(View.VISIBLE);
                                }
                                for (final QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> note_info = document.getData();
                                    String heading = (String) note_info.get("heading");
                                    String description = (String) note_info.get("description");
                                    String time = (String) note_info.get("time");
                                    String priority = (String) note_info.get("priority");
                                    long timestamp = (long) note_info.get("timestamp");
                                    String doc_id = (String) document.getId();

                                    notes.add(new Note(heading, description, time, priority, timestamp, doc_id));
                                    NoteAdapter adapter = new NoteAdapter(NoteActivity.this, notes);

                                    ListView listView = (ListView) findViewById(R.id.list);
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            String clicked_id = notes.get(position).getmId();
                                            delete_note("students", clicked_id);
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        ImageView add_note = findViewById(R.id.addnote);
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, addnoteActivity.class);
                NoteActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
    }

    private void delete_note(final String type, final String document) {
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String name = sharedPreferences.getString(NAME, null);

        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(NoteActivity.this);
        builder.setMessage("Do you want to delete this note?");
        builder.setTitle("Warning!");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == "teachers") {
                            delete_process("teachers", document, name);
                        } else {
                            delete_process("students", document, name);
                        }
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void delete_process(final String type, final String document, final String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("School").document("Accouts").collection(type).document(username).collection("notes").document(document)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(NoteActivity.this, NoteActivity.class);
                        NoteActivity.this.startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    @Override
    public void onBackPressed() {
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String type = sharedPreferences.getString(TYPE, null);
        if (type.equals("teacher")) {
            Intent intent = new Intent(NoteActivity.this, TeachermainActivity.class);
            NoteActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        } else {
            Intent intent = new Intent(NoteActivity.this, MainActivity.class);
            NoteActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        }

    }

}
