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

public class ExtraloginActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "myprefs";
    public static final String SIGNED_IN = "signed_in";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBarExtra;
    private Button loginextra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extralogin);
        mAuth = FirebaseAuth.getInstance();

        String account = getIntent().getStringExtra("account");
        TextView display = findViewById(R.id.display);
        String value = "Sign in as " + account;
        display.setText(value);

        final EditText usernamefieldextra = findViewById(R.id.username_extra);
        final EditText passwordfieldextra = findViewById(R.id.password_extra);
        mProgressBarExtra = findViewById(R.id.progress_extra);
        loginextra = findViewById(R.id.login_extra);

        if (account.equals("Parent")) {
            loginextra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProgressBarExtra.setVisibility(View.VISIBLE);
                    loginextra.setVisibility(View.GONE);

                    final String usernameextra = usernamefieldextra.getText().toString();
                    final String passwordextra = passwordfieldextra.getText().toString();
                    if (usernameextra.isEmpty() || passwordextra.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Enter Values", Toast.LENGTH_SHORT).show();
                        loginextra.setVisibility(View.VISIBLE);
                        mProgressBarExtra.setVisibility(View.GONE);
                    } else {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference user = db.collection("School").document("Accouts").collection("parents").document(usernameextra);
                        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    String firebasepasswordextra = (String) doc.get("password");

                                    if (passwordextra.equals(firebasepasswordextra)) {
                                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean(SIGNED_IN, true);
                                        editor.putString(NAME, usernameextra);
                                        editor.putString(TYPE, "parent");
                                        editor.apply();
                                        String final_email = usernameextra.trim().replace(" ", "/");
                                        String email = "p/" + final_email + "@fazaiaschoolsandcolleges.com";
                                        sign_up(email, passwordextra, "parent", usernameextra);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please Enter Correct Values", Toast.LENGTH_SHORT).show();
                                        loginextra.setVisibility(View.VISIBLE);
                                        mProgressBarExtra.setVisibility(View.GONE);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                    loginextra.setVisibility(View.VISIBLE);
                                    mProgressBarExtra.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }
            });
        }
        if (account.equals("Teacher")) {
            loginextra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProgressBarExtra.setVisibility(View.VISIBLE);
                    loginextra.setVisibility(View.GONE);

                    final String usernameextra = usernamefieldextra.getText().toString();
                    final String passwordextra = passwordfieldextra.getText().toString();
                    if (usernameextra.isEmpty() || passwordextra.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Enter Values", Toast.LENGTH_SHORT).show();
                        loginextra.setVisibility(View.VISIBLE);
                        mProgressBarExtra.setVisibility(View.GONE);
                    } else {
                        if (usernameextra.toLowerCase().trim().equals("admin")) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference user = db.collection("School").document("Accouts");
                            user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot doc = task.getResult();
                                        String firebasepasswordextra = (String) doc.get("admin");
                                        if (passwordextra.equals(firebasepasswordextra)) {
                                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean(SIGNED_IN, true);
                                            editor.putString(NAME, usernameextra);
                                            editor.putString(TYPE, "admin");
                                            editor.apply();
                                            Intent intent = new Intent(ExtraloginActivity.this, AdminmainActivity.class);
                                            ExtraloginActivity.this.startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                                            finish();

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please Enter Correct Values", Toast.LENGTH_SHORT).show();
                                            loginextra.setVisibility(View.VISIBLE);
                                            mProgressBarExtra.setVisibility(View.GONE);
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                        loginextra.setVisibility(View.VISIBLE);
                                        mProgressBarExtra.setVisibility(View.GONE);
                                    }
                                }
                            });

                        } else {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference user = db.collection("School").document("Accouts").collection("teachers").document(usernameextra);
                            user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot doc = task.getResult();
                                        String firebasepasswordextra = (String) doc.get("password");
                                        if (passwordextra.equals(firebasepasswordextra)) {

                                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean(SIGNED_IN, true);
                                            editor.putString(NAME, usernameextra);
                                            editor.putString(TYPE, "teacher");
                                            editor.apply();
                                            String final_email = usernameextra.trim().replace(" ", "/");
                                            String email = "t/" + final_email + "@fazaiaschoolsandcolleges.com";
                                            sign_up(email, passwordextra, "teacher", usernameextra);

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please Enter Correct Values", Toast.LENGTH_SHORT).show();
                                            loginextra.setVisibility(View.VISIBLE);
                                            mProgressBarExtra.setVisibility(View.GONE);
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                        loginextra.setVisibility(View.VISIBLE);
                                        mProgressBarExtra.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }

                    }
                }
            });
        }
    }


    private void updateUI(FirebaseUser user, String person_type) {

        if (person_type.equals("teacher")) {
            Intent intent = new Intent(ExtraloginActivity.this, TeachermainActivity.class);
            ExtraloginActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish();

        }
        if (person_type.equals("parent")) {
            Intent intent = new Intent(ExtraloginActivity.this, ParentmainActivity.class);
            ExtraloginActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            finish();
        }
    }

    public void sign_up(final String email, final String password, final String person, final String username) {
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
                            sign_in(email, password, person);

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                sign_in(email, password, person);

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

    public void sign_in(String email, String password, final String person_type) {
        mProgressBarExtra = findViewById(R.id.progress_extra);
        loginextra = findViewById(R.id.login_extra);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginextra.setVisibility(View.VISIBLE);
                            mProgressBarExtra.setVisibility(View.GONE);
                            updateUI(user, person_type);

                        } else {
                            Toast.makeText(ExtraloginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            loginextra.setVisibility(View.VISIBLE);
                            mProgressBarExtra.setVisibility(View.GONE);
                            updateUI(null, "None");

                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ExtraloginActivity.this, LoginActivity.class);
        ExtraloginActivity.this.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

    }
}
