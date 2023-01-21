package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.NAME;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.SHARED_PREFS;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.TYPE;

public class addnoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String name = sharedPreferences.getString(NAME, null);
        final String type = sharedPreferences.getString(TYPE, null);

        Button add_note = findViewById(R.id.add_note);
        final EditText heading_view = findViewById(R.id.add_heading);
        final EditText description_view = findViewById(R.id.add_description);
        final Spinner priority_view = findViewById(R.id.add_priority);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
                String minute = String.valueOf(calendar.get(Calendar.MINUTE));
                String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
                String year = String.valueOf(calendar.get(Calendar.YEAR));
                String time = hour + ":" + minute + "   " + month + "/" + date + "/" + year;
                String heading = String.valueOf(heading_view.getText());
                String description = String.valueOf(description_view.getText());
                String priority = String.valueOf(priority_view.getSelectedItem());
                long timestamp= TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

                if (heading.isEmpty() || description.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Values", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> newNote = new HashMap<>();
                    newNote.put("description", description);
                    newNote.put("heading", heading);
                    newNote.put("time", time);
                    newNote.put("priority", priority);
                    newNote.put("timestamp", -timestamp);

                    if (type.equals("teacher")) {
                        db.collection("School").document("Accouts").collection("teachers").document(name).collection("notes").document().set(newNote);
                    }
                    else if (type.equals("student")) {
                        db.collection("School").document("Accouts").collection("students").document(name).collection("notes").document().set(newNote);

                    }
                    Intent intent = new Intent(addnoteActivity.this, NoteActivity.class);
                    addnoteActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(addnoteActivity.this);
        builder.setMessage("Note has not been published.Leave anyways?");
        builder.setTitle("Alert!");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(addnoteActivity.this, NoteActivity.class);
                        addnoteActivity.this.startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
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
}