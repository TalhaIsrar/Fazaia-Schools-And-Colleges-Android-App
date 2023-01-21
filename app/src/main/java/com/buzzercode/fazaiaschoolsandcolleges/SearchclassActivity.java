package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class SearchclassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchclass);

        final String search_name = getIntent().getStringExtra("search_class").trim();
        String classes = String.valueOf(search_name.charAt(0)) + String.valueOf(search_name.charAt(1));
        String section = String.valueOf(search_name.charAt(2)).toUpperCase();
        TextView search_class_display = findViewById(R.id.search_class_display);
        final String final_display = classes + " " + section;
        search_class_display.setText(final_display);

        final ArrayList<String> class_list = new ArrayList<String>();

        final TextView class_error = findViewById(R.id.class_error);
        final ProgressBar class_loading_process = findViewById(R.id.class_loading_progress);
        class_error.setVisibility(View.INVISIBLE);
        final RelativeLayout class_loading = findViewById(R.id.class_loading);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("School").document("Classes").collection(classes).document(section).collection("students").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot doc = task.getResult();
                    if (!doc.isEmpty()) {
                        class_loading.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> note_info = document.getData();
                            String student_name = (String) note_info.get("name");
                            class_list.add(student_name);

                        }
                    } else {
                        String error_message = "Can not find search results for " + "'" + final_display + "'";
                        class_error.setText(error_message);
                        class_error.setVisibility(View.VISIBLE);
                        class_loading_process.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });
        final ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, class_list);
        ListView listView = (ListView) findViewById(R.id.search_class_list);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = parent.getItemAtPosition(position).toString().trim();
                Intent intent = new Intent(SearchclassActivity.this, SearchprofileActivity.class);
                intent.putExtra("search_name",clickedItem);
                SearchclassActivity.this.startActivity(intent);
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
