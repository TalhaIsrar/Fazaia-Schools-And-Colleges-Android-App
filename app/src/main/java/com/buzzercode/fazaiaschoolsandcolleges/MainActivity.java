package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.NAME;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.SHARED_PREFS;

public class MainActivity extends AppCompatActivity {
    public static final String CLASS = "class";
    public static final String SECTION = "section";

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String name = sharedPreferences.getString(NAME, null);
        final String sv_classes = sharedPreferences.getString(CLASS, null);
        final String sv_section = sharedPreferences.getString(SECTION, null);

        final ImageView search = findViewById(R.id.search);
        ImageButton profile = findViewById(R.id.profile);
        ImageButton classes = findViewById(R.id.classes);
        ImageButton timetable = findViewById(R.id.timetable);
        ImageButton notes = findViewById(R.id.notes);
        ImageButton chat = findViewById(R.id.chat);

        if (sv_classes == null || sv_section == null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference user = db.collection("School").document("Accouts").collection("students").document(name);

            user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        String firebaseclass = (String) doc.get("class");
                        String firebasesection = (String) doc.get("section");
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(CLASS, firebaseclass);
                        editor.putString(SECTION, firebasesection);
                        editor.apply();
                    }
                }

            });
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference verify = db.collection("School").document("Accouts").collection("students").document("Admin");
        verify.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    String password = (String) doc.get("password");
                    String verification = (String) doc.get("auth");
                    if (verification.equals("false")) {
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(NAME, null);
                        editor.putString(CLASS, null);
                        editor.putString(SECTION, null);
                        editor.apply();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, TeachermainActivity.class);
                        MainActivity.this.startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    }
                }
            }

        });


        TextView welcome = findViewById(R.id.welcome_text);
        TextView quote = findViewById(R.id.quote);
        String welcome_text = "Weclome back " + name + "!";
        welcome.setText(welcome_text);
        final int random = new Random().nextInt(5) + 1;

        final ArrayList<String> quotes_student = new ArrayList<String>();
        quotes_student.add("Education is the most powerful weapon which you can use to change the world.");
        quotes_student.add("Education is for improving the lives of others and for leaving your community and world better than you found it.");
        quotes_student.add("An investment in knowledge pays the best interest.");
        quotes_student.add("Education is what remains after one has forgotten what one has learned in school.");
        quotes_student.add("Education’s purpose is to replace an empty mind with an open one.");
        quotes_student.add("Education is the passport to the future, for tomorrow belongs to those who prepare for it today.");

        String final_quote = quotes_student.get(random);
        quote.setText(final_quote);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText search_for = findViewById(R.id.search_for);
                String search_name = search_for.getText().toString();

                if (search_name.matches("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, SearchprofileActivity.class);
                    intent.putExtra("search_name", search_name);
                    MainActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                MainActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sv_classes = sharedPreferences.getString(CLASS, null);
                final String sv_section = sharedPreferences.getString(SECTION, null);
                if (sv_classes != null && sv_section != null) {
                    Intent intent = new Intent(MainActivity.this, TimetableActivity.class);
                    MainActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });
        classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sv_classes = sharedPreferences.getString(CLASS, null);
                final String sv_section = sharedPreferences.getString(SECTION, null);
                if (sv_classes != null && sv_section != null) {
                    Intent intent = new Intent(MainActivity.this, ClassActivity.class);
                    MainActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                MainActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sv_classes = sharedPreferences.getString(CLASS, null);
                final String sv_section = sharedPreferences.getString(SECTION, null);
                if (sv_classes != null && sv_section != null) {
                    Intent intent = new Intent(MainActivity.this, ClasschatActivity.class);
                    MainActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });

    }
}