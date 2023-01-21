package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.NAME;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.SHARED_PREFS;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.SIGNED_IN;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.TYPE;

public class AdminmainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminmain);

        Button add_student = findViewById(R.id.add_student_screen);
        Button add_teacher = findViewById(R.id.add_teacher_screen);

        ImageView signout_admin = findViewById(R.id.signout_admin);
        final ImageView search = findViewById(R.id.search_admin);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText search_for = findViewById(R.id.search_for_admin);
                String search_name = search_for.getText().toString();

                if (search_name.matches("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(AdminmainActivity.this, SearchprofileActivity.class);
                    intent.putExtra("search_name", search_name);
                    AdminmainActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                }
            }
        });

        add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminmainActivity.this, AddstudentActivity.class);
                AdminmainActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        add_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminmainActivity.this, AddteacherActivity.class);
                AdminmainActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        signout_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SIGNED_IN, false);
                editor.putString(NAME, null);
                editor.putString(TYPE, null);
                editor.apply();
                Intent intent = new Intent(AdminmainActivity.this, LoginActivity.class);
                AdminmainActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}
