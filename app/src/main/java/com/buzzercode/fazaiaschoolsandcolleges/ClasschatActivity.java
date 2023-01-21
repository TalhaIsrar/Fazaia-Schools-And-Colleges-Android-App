package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.buzzercode.fazaiaschoolsandcolleges.DataFiles.Message;
import com.buzzercode.fazaiaschoolsandcolleges.DataFiles.MessageAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.SHARED_PREFS;
import static com.buzzercode.fazaiaschoolsandcolleges.MainActivity.CLASS;
import static com.buzzercode.fazaiaschoolsandcolleges.MainActivity.SECTION;

public class ClasschatActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore database;
    Query query;
    private FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder> adapter;
    private EditText input;
    private ProgressBar pgBar;
    private String userId,userName;
    String classes,section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classchat);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        classes = sharedPreferences.getString(CLASS, null);
        section = sharedPreferences.getString(SECTION, null);

        ImageView btnSend = findViewById(R.id.send_message);
        btnSend.setOnClickListener(this);
        input = findViewById(R.id.write_message);
        pgBar = findViewById(R.id.loading_messages);
        final RecyclerView recyclerView = findViewById(R.id.message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        userId = user.getUid();
        userName = user.getDisplayName();
        database = FirebaseFirestore.getInstance();
        query = database.collection("School").document("Classes").collection(classes).document(section).collection("messages").orderBy("messageTime");
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (queryDocumentSnapshots != null || queryDocumentSnapshots.isEmpty()) {
                    pgBar.setVisibility(View.GONE);

                }
                recyclerView.scrollToPosition(queryDocumentSnapshots.size() - 1);
            }
        });

        adapter = new MessageAdapter(query, userId, ClasschatActivity.this);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_message) {
            String message = input.getText().toString();
            if (TextUtils.isEmpty(message)) {
                Toast.makeText(ClasschatActivity.this, "Post is empty", Toast.LENGTH_LONG).show();
                return;
            }
            database.collection("School").document("Classes").collection(classes).document(section).collection("messages").add(new Message(userName, message, userId));
            input.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}