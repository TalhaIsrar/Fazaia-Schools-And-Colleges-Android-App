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

public class AddstudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudent);
        final EditText name = findViewById(R.id.add_name);
        final EditText parent = findViewById(R.id.add_parent);
        final EditText password = findViewById(R.id.add_password);
        final EditText parent_password = findViewById(R.id.add_parent_password);
        final EditText age = findViewById(R.id.add_age);
        final EditText phone = findViewById(R.id.add_phone);

        final EditText classes = findViewById(R.id.add_class);
        final EditText section = findViewById(R.id.add_section);
        final EditText id = findViewById(R.id.add_id);

        final Button add_student = findViewById(R.id.add_student);
        final ProgressBar adding_student = findViewById(R.id.adding_student);
        add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_student.setVisibility(View.GONE);
                adding_student.setVisibility(View.VISIBLE);
                final String final_name = name.getText().toString();
                final String final_parent = parent.getText().toString();
                final String final_password = password.getText().toString();
                final String final_parent_password = parent_password.getText().toString();
                final String final_age = age.getText().toString();
                final String final_phone = phone.getText().toString();
                final String final_classes = classes.getText().toString();
                final String final_section = section.getText().toString().toUpperCase();
                final String final_id = id.getText().toString();
                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                if (final_name.isEmpty() || final_parent.isEmpty() || final_password.isEmpty() || final_parent_password.isEmpty() ||
                        final_age.isEmpty() || final_phone.isEmpty() || final_classes.isEmpty() || final_section.isEmpty() || final_id.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter All Values", Toast.LENGTH_SHORT).show();
                    add_student.setVisibility(View.VISIBLE);
                    adding_student.setVisibility(View.GONE);
                } else {
                    if (final_password.length() < 6 || final_parent_password.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Passwords are to small", Toast.LENGTH_SHORT).show();
                        add_student.setVisibility(View.VISIBLE);
                        adding_student.setVisibility(View.GONE);
                    } else {
                        DocumentReference docIdRef =db.collection("School").document("Accouts").collection("students").document(final_name);
                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Toast.makeText(getApplicationContext(), "User Name Exists", Toast.LENGTH_SHORT).show();
                                        add_student.setVisibility(View.VISIBLE);
                                        adding_student.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                        Map<String, Object> newStudent = new HashMap<>();
                                        newStudent.put("name", final_name);
                                        newStudent.put("parent", final_parent);
                                        newStudent.put("password", final_password);
                                        newStudent.put("age", final_age);
                                        newStudent.put("phone", final_phone);
                                        newStudent.put("class", final_classes);
                                        newStudent.put("section", final_section);
                                        newStudent.put("id", final_id);
                                        newStudent.put("image", "");
                                        db.collection("School").document("Accouts").collection("students").document(final_name).set(newStudent);
                                        Map<String, Object> newParent = new HashMap<>();
                                        newParent.put("password", final_parent_password);
                                        db.collection("School").document("Accouts").collection("parents").document(final_name).set(newParent);
                                        Map<String, Object> newClassStudent = new HashMap<>();
                                        newClassStudent.put("name", final_name);
                                        add_student.setVisibility(View.VISIBLE);
                                        adding_student.setVisibility(View.GONE);
                                        db.collection("School").document("Classes").collection(final_classes).document(final_section).collection("students").document(final_name).set(newClassStudent);
                                        Intent intent = new Intent(AddstudentActivity.this, AdminmainActivity.class);
                                        AddstudentActivity.this.startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);                                    }
                                } else {
                                }
                            }
                        });

                        add_student.setVisibility(View.VISIBLE);
                        adding_student.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(AddstudentActivity.this);
        builder.setMessage("Student has not been registered.Leave anyways?");
        builder.setTitle("Alert!");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AddstudentActivity.this, AdminmainActivity.class);
                        AddstudentActivity.this.startActivity(intent);
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
