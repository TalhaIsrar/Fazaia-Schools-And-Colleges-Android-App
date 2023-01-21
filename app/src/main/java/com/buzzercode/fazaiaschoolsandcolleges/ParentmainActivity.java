package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ParentmainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentmain);
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String name = sharedPreferences.getString(NAME, null);
        final String sv_classes = sharedPreferences.getString(CLASS, null);
        final String sv_section = sharedPreferences.getString(SECTION, null);

        final ImageView search = findViewById(R.id.search_parent);
        ImageButton classes = findViewById(R.id.classes);
        final ImageButton timetable = findViewById(R.id.timetable_parent);

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
        TextView quote = findViewById(R.id.parent_quote);
        final int random = new Random().nextInt(5) + 1;

        final ArrayList<String> quotes_parent = new ArrayList<String>();
        quotes_parent.add("Parental love is the only love that is truly selfless, unconditional and forgiving. ");
        quotes_parent.add("We never know the love of a parent till we become parents ourselves.");
        quotes_parent.add("When you look into your mother's eyes, you know that is the purest love you can find on this earth. ");
        quotes_parent.add("No matter how far we come, our parents are always in us. ");
        quotes_parent.add("A parent's love is whole no matter how many times divided.");
        quotes_parent.add("Parents of young children should realize that few people, and maybe no one, will find their children as enchanting as they do. ");
        String final_quote = quotes_parent.get(random);
        quote.setText(final_quote);

        ImageView signout_parent = findViewById(R.id.signout_parent);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText search_for = findViewById(R.id.search_for_parent);
                String search_name = search_for.getText().toString();
                if (search_name.matches("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ParentmainActivity.this, SearchprofileActivity.class);
                    intent.putExtra("search_name", search_name);
                    ParentmainActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });
        signout_parent.setOnClickListener(new View.OnClickListener() {
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

                Intent intent = new Intent(ParentmainActivity.this, LoginActivity.class);
                ParentmainActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sv_classes = sharedPreferences.getString(CLASS, null);
                final String sv_section = sharedPreferences.getString(SECTION, null);
                if (sv_classes != null && sv_section != null) {

                    Intent intent = new Intent(ParentmainActivity.this, ClassActivity.class);
                    ParentmainActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }

            }
        });

        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sv_classes = sharedPreferences.getString(CLASS, null);
                final String sv_section = sharedPreferences.getString(SECTION, null);
                if (sv_classes != null && sv_section != null) {
                    Intent intent = new Intent(ParentmainActivity.this, TimetableActivity.class);
                    ParentmainActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

}
