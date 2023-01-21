package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddteacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addteacher);
        final EditText name = findViewById(R.id.add_name);
        final EditText password = findViewById(R.id.add_password);
        final EditText phone = findViewById(R.id.add_phone);

        final EditText class_teacher = findViewById(R.id.add_class_teacher);
        final EditText subject = findViewById(R.id.add_subject);
        final EditText extra_subject = findViewById(R.id.add_extra_subjects);

        final Button add_teacher = findViewById(R.id.add_teacher);
        final ProgressBar adding_teacher = findViewById(R.id.adding_teacher);
        add_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_teacher.setVisibility(View.GONE);
                adding_teacher.setVisibility(View.VISIBLE);
                final String final_name = name.getText().toString();
                final String final_password = password.getText().toString();
                final String final_phone = phone.getText().toString();
                final String final_class_teacher = class_teacher.getText().toString();
                final String final_subject = subject.getText().toString().toUpperCase();
                final String final_extra_subject = extra_subject.getText().toString();
                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                if (final_name.isEmpty() || final_password.isEmpty() || final_phone.isEmpty() || final_class_teacher.isEmpty() ||
                        final_subject.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter All Values", Toast.LENGTH_SHORT).show();
                    add_teacher.setVisibility(View.VISIBLE);
                    adding_teacher.setVisibility(View.GONE);
                } else {
                    if (final_password.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Passwords are to small", Toast.LENGTH_SHORT).show();
                        add_teacher.setVisibility(View.VISIBLE);
                        adding_teacher.setVisibility(View.GONE);
                    } else {
                        DocumentReference docIdRef =db.collection("School").document("Accouts").collection("teachers").document(final_name);
                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Toast.makeText(getApplicationContext(), "User Name Exists", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                        Map<String, Object> newTeacher = new HashMap<>();
                                        newTeacher.put("name", final_name);
                                        newTeacher.put("password", final_password);
                                        newTeacher.put("phone", final_phone);
                                        newTeacher.put("class_teacher", final_class_teacher);

                                        newTeacher.put("subject", final_subject);
                                        newTeacher.put("extra_subjects", final_extra_subject);
                                        newTeacher.put("timetable", "");
                                        db.collection("School").document("Accouts").collection("teachers").document(final_name).set(newTeacher);

                                        add_teacher.setVisibility(View.VISIBLE);
                                        adding_teacher.setVisibility(View.GONE);
                                        Intent intent = new Intent(AddteacherActivity.this, AdminmainActivity.class);
                                        AddteacherActivity.this.startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);                                                                   }
                                } else {
                                }
                            }
                        });

                        add_teacher.setVisibility(View.VISIBLE);
                        adding_teacher.setVisibility(View.GONE);

                    }

                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(AddteacherActivity.this);
        builder.setMessage("Teacher has not been registered.Leave anyways?");
        builder.setTitle("Alert!");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AddteacherActivity.this, AdminmainActivity.class);
                        AddteacherActivity.this.startActivity(intent);
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
