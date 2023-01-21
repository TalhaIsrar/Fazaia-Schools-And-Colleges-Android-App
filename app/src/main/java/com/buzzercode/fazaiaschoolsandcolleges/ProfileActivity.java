package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.NAME;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.SHARED_PREFS;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.SIGNED_IN;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.TYPE;
import static com.buzzercode.fazaiaschoolsandcolleges.MainActivity.CLASS;
import static com.buzzercode.fazaiaschoolsandcolleges.MainActivity.SECTION;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String name = sharedPreferences.getString(NAME, null);

        final ImageView signout = findViewById(R.id.signout);
        final TextView profile_name = findViewById(R.id.name_display);
        final TextView name_view = findViewById(R.id.profile_name);
        final TextView age_view = findViewById(R.id.profile_age);
        final TextView classes_view = findViewById(R.id.profile_class);
        final TextView phone_view = findViewById(R.id.profile_phone);
        final TextView id_view = findViewById(R.id.profile_id);
        final TextView guardian_view = findViewById(R.id.profile_guardian);
        final ImageView profile_img = findViewById(R.id.profile_img);
        final ProgressBar wait = findViewById(R.id.pic_loading);

        profile_name.setText(name);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putBoolean(SIGNED_IN, false);
                editor.putString(NAME, null);
                editor.putString(TYPE, null);
                editor.putString(CLASS, null);
                editor.putString(SECTION, null);
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                ProfileActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference user = db.collection("School").document("Accouts").collection("students").document(name);

        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    String id = (String) doc.get("id");
                    String age = (String) doc.get("age");
                    String classes = (String) doc.get("class");
                    String section = (String) doc.get("section");
                    String phone = (String) doc.get("phone");
                    String img = (String) doc.get("image");
                    String guardian = (String) doc.get("parent");

                    editor.putString(CLASS, classes);
                    editor.putString(SECTION, section);
                    editor.apply();
                    name_view.setText(name);
                    age_view.setText(age);
                    String full_class=classes+" " +section;
                    classes_view.setText(full_class);
                    phone_view.setText(phone);
                    id_view.setText(id);
                    guardian_view.setText(guardian);
                    if(img.equals("")){
                        profile_img.setImageResource(R.drawable.fazaialogo);
                    }else {
                        Glide.with(getApplicationContext())
                                .load(img)
                                .into(profile_img);
                    }
                    wait.setVisibility(View.INVISIBLE);
                    profile_img.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

    }
}
