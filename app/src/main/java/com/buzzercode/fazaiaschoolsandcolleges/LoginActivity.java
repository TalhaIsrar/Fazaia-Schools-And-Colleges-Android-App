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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "myprefs";
    public static final String SIGNED_IN = "signed_in";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    private Button login;

    private boolean signed_in;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        final EditText usernamefield = findViewById(R.id.username);
        final EditText passwordfield = findViewById(R.id.password);
        mProgressBar = findViewById(R.id.progress);
        login = findViewById(R.id.login);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        signed_in = sharedPreferences.getBoolean(SIGNED_IN, false);
        type = sharedPreferences.getString(TYPE, null);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);

                final String username = usernamefield.getText().toString().trim();
                final String password = passwordfield.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Values", Toast.LENGTH_SHORT).show();
                    login.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference user = db.collection("School").document("Accouts").collection("students").document(username);
                    user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot doc = task.getResult();
                                String firebasepassword = (String) doc.get("password");

                                if (password.equals(firebasepassword)) {
                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(SIGNED_IN, true);
                                    editor.putString(NAME, username);
                                    editor.putString(TYPE, "student");
                                    editor.apply();

                                    String final_email = username.trim().replace(" ", "/");
                                    String email = "s/" + final_email + "@fazaiaschoolsandcolleges.com";
                                    sign_up(email, firebasepassword, username);

                                } else {
                                    Toast.makeText(getApplicationContext(), "Please Enter Correct Values", Toast.LENGTH_SHORT).show();
                                    login.setVisibility(View.VISIBLE);
                                    mProgressBar.setVisibility(View.GONE);
                                }
                            } else {
                                login.setVisibility(View.VISIBLE);
                                mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

        TextView parent = findViewById(R.id.parent);
        TextView teacher = findViewById(R.id.teacher);

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ExtraloginActivity.class);
                intent.putExtra("account", "Parent");
                LoginActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
            }
        });
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ExtraloginActivity.class);
                intent.putExtra("account", "Teacher");
                LoginActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (signed_in) {

            if (type.equals("student")) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
            }
            if (type.equals("teacher")) {
                Intent intent = new Intent(LoginActivity.this, TeachermainActivity.class);
                LoginActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
            }
            if (type.equals("parent")) {
                Intent intent = new Intent(LoginActivity.this, ParentmainActivity.class);
                LoginActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
            }
            if (type.equals("admin")) {
                Intent intent = new Intent(LoginActivity.this, AdminmainActivity.class);
                LoginActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
            }
        }
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish();

        }
    }

    public void sign_up(final String email, final String password, final String username) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            user.updateProfile(profileUpdate);
                            sign_in(email, password);

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                sign_in(email, password);
                            }
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Log.e("Exception", e.getMessage());
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Log.e("Exception", e.getMessage());
                            } catch (FirebaseAuthUserCollisionException e) {
                                Log.e("Exception", e.getMessage());
                            } catch (Exception e) {
                                Log.e("Exception", e.getMessage());
                            }
                        }

                    }
                });
    }

    public void sign_in(String email, String password) {
        mProgressBar = findViewById(R.id.progress);
        login = findViewById(R.id.login);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            mProgressBar.setVisibility(View.GONE);
                            login.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign In failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            mProgressBar.setVisibility(View.GONE);
                            login.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
