package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.NAME;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.SHARED_PREFS;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.SIGNED_IN;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.TYPE;
import static com.buzzercode.fazaiaschoolsandcolleges.MainActivity.CLASS;
import static com.buzzercode.fazaiaschoolsandcolleges.MainActivity.SECTION;



public class TeachermainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachermain);
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String name = sharedPreferences.getString(NAME, null);
        final String sv_classes = sharedPreferences.getString(CLASS, null);
        final String sv_section = sharedPreferences.getString(SECTION, null);

        final ImageView search = findViewById(R.id.search_teacher);
        ImageButton classes = findViewById(R.id.classes);
        ImageButton notes = findViewById(R.id.notes);
        ImageButton timetable = findViewById(R.id.timetable_teacher);
        ImageButton chat = findViewById(R.id.chat);

        if (sv_classes == null || sv_section == null) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference user = db.collection("School").document("Accouts").collection("teachers").document(name);

            user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        String firebaseclassextra = (String) doc.get("class_teacher");
                        String finalclass = firebaseclassextra.trim().replace(" ", "");
                        String firebaseclass = String.valueOf(finalclass.charAt(0)) + String.valueOf(finalclass.charAt(1));
                        String firebasesection = String.valueOf(finalclass.charAt(2)).toUpperCase();
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(CLASS, firebaseclass);
                        editor.putString(SECTION, firebasesection);
                        editor.apply();

                    }
                }

            });
        }
        TextView welcome = findViewById(R.id.teacher_welcome_text);
        TextView quote = findViewById(R.id.teacher_quote);
        String welcome_text = "Weclome back " + name + "!";
        welcome.setText(welcome_text);
        final int random = new Random().nextInt(5) + 1;

        final ArrayList<String> quotes_teacher = new ArrayList<String>();
        quotes_teacher.add("Teaching is not a lost art, but the regard for it is a lost tradition.");
        quotes_teacher.add("A good teacher is like a candle – it consumes itself to light the way for others.");
        quotes_teacher.add("Teaching kids to count is fine, but teaching them what counts is best.");
        quotes_teacher.add("Teaching is the one profession that creates all other professions.");
        quotes_teacher.add("I like a teacher who gives you something to take home to think about besides homework.");
        quotes_teacher.add("What the teacher is, is more important than what he teaches.");
        String final_quote = quotes_teacher.get(random);
        quote.setText(final_quote);

        ImageView signout_teacher = findViewById(R.id.signout_teacher);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText search_for = findViewById(R.id.search_for_teacher);
                String search_name = search_for.getText().toString();
                if (search_name.matches("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(TeachermainActivity.this, SearchprofileActivity.class);
                    intent.putExtra("search_name", search_name);
                    TeachermainActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });
        signout_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SIGNED_IN, false);
                editor.putString(NAME, null);
                editor.putString(TYPE, null);
                editor.putString(CLASS, null);
                editor.putString(SECTION, null);
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                FirebaseAuth auth = FirebaseAuth.getInstance();

                FirebaseUser user = auth.getCurrentUser();

                Intent intent = new Intent(TeachermainActivity.this, LoginActivity.class);
                TeachermainActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sv_classes = sharedPreferences.getString(CLASS, null);
                final String sv_section = sharedPreferences.getString(SECTION, null);
                if (sv_classes != null && sv_section != null) {

                    Intent intent = new Intent(TeachermainActivity.this, ClassActivity.class);
                    TeachermainActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }

            }
        });
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeachermainActivity.this, NoteActivity.class);
                TeachermainActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

            }
        });
        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent intent = new Intent(TeachermainActivity.this, TimetableActivity.class);
                    TeachermainActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sv_classes = sharedPreferences.getString(CLASS, null);
                final String sv_section = sharedPreferences.getString(SECTION, null);
                if (sv_classes != null && sv_section != null) {

                    Intent intent = new Intent(TeachermainActivity.this, ClasschatActivity.class);
                TeachermainActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);}
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

}
