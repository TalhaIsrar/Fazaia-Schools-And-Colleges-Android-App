package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.NAME;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.SHARED_PREFS;
import static com.buzzercode.fazaiaschoolsandcolleges.MainActivity.CLASS;
import static com.buzzercode.fazaiaschoolsandcolleges.MainActivity.SECTION;

public class ClassActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String name = sharedPreferences.getString(NAME, null);
        final String classes = sharedPreferences.getString(CLASS, null);
        final String section = sharedPreferences.getString(SECTION, null);

        ImageView search_class = findViewById(R.id.search_class);
        final ProgressBar loading_class = findViewById(R.id.loading_class);
        final TextView class_display = findViewById(R.id.class_display);
        final EditText search_for_class = findViewById(R.id.search_for_class);

        final ArrayList<String> mylist = new ArrayList<String>();
        String full_class = classes + " " + section;
        class_display.setText(full_class);
        loading_class.setVisibility(View.VISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("School").document("Classes").collection(classes).document(section).collection("students").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            loading_class.setVisibility(View.GONE);

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> note_info = document.getData();
                                String student_name = (String) note_info.get("name");
                                mylist.add(student_name);
                            }
                            display(mylist);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        search_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String class_name = search_for_class.getText().toString();
                String final_class_name = class_name.trim().replace(" ", "");
                if (final_class_name.length() == 3) {
                    Intent intent = new Intent(ClassActivity.this, SearchclassActivity.class);
                    intent.putExtra("search_class", final_class_name);
                    ClassActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                } else {
                    Toast.makeText(getApplicationContext(), "Enter a valid class", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void display(ArrayList<String> my_list) {
        final ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, my_list);
        listView = (ListView) findViewById(R.id.class_list);
        listView.setAdapter(itemsAdapter);
        listView.setVisibility(View.VISIBLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = parent.getItemAtPosition(position).toString().trim();
                Intent intent = new Intent(ClassActivity.this, SearchprofileActivity.class);
                intent.putExtra("search_name", clickedItem);
                ClassActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

    }
}
