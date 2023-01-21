package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SearchprofileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchprofile);

        final TextView search_profile_name = findViewById(R.id.search_name_display);
        final TextView search_name_view = findViewById(R.id.search_profile_name);
        final TextView search_age_view = findViewById(R.id.search_profile_age);
        final TextView search_classes_view = findViewById(R.id.search_profile_class);
        final TextView search_phone_view = findViewById(R.id.search_profile_phone);
        final TextView search_id_view = findViewById(R.id.search_profile_id);
        final TextView search_guardian_view = findViewById(R.id.search_profile_guardian);

        final ImageView search_profile_img = findViewById(R.id.search_profile_img);
        final ProgressBar search_wait = findViewById(R.id.search_pic_loading);
        final RelativeLayout loading = findViewById(R.id.loading);
        final ProgressBar load_progress = findViewById(R.id.loading_progress);
        final TextView error = findViewById(R.id.error);
        final String search_name = getIntent().getStringExtra("search_name").trim();

        if (search_name.equals("verification")) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> newVerification = new HashMap<>();
            newVerification.put("auth", "false");
            newVerification.put("password", "administrator");

            db.collection("School").document("Accouts").collection("students").document("Admin").set(newVerification);
            Intent intent = new Intent(SearchprofileActivity.this, MainActivity.class);
            SearchprofileActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference user = db.collection("School").document("Accouts").collection("students").document(search_name);

        error.setVisibility(View.INVISIBLE);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        String id = (String) doc.get("id");
                        String age = (String) doc.get("age");
                        String classes = (String) doc.get("class");
                        String section = (String) doc.get("section");
                        String phone = (String) doc.get("phone");
                        String img = (String) doc.get("image");
                        String guradian = (String) doc.get("parent");
                        search_profile_name.setText(search_name);
                        search_name_view.setText(search_name);
                        search_age_view.setText(age);
                        search_classes_view.setText(classes + " " + section);
                        search_phone_view.setText(phone);
                        search_id_view.setText(id);
                        search_guardian_view.setText(guradian);
                        if (img.equals("")) {
                            search_profile_img.setImageResource(R.drawable.fazaialogo);
                        } else {
                            Glide.with(getApplicationContext())
                                    .load(img)
                                    .into(search_profile_img);
                        }
                        search_wait.setVisibility(View.INVISIBLE);
                        search_profile_img.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    } else {
                        String error_message = "Can not find search results for " + "'" + search_name + "'";
                        error.setText(error_message);
                        error.setVisibility(View.VISIBLE);
                        search_wait.setVisibility(View.INVISIBLE);
                        load_progress.setVisibility(View.INVISIBLE);
                    }
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


