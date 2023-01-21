package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.NAME;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.SHARED_PREFS;
import static com.buzzercode.fazaiaschoolsandcolleges.LoginActivity.TYPE;
import static com.buzzercode.fazaiaschoolsandcolleges.MainActivity.CLASS;
import static com.buzzercode.fazaiaschoolsandcolleges.MainActivity.SECTION;

public class TimetableActivity extends AppCompatActivity {
    String[] enteries;

    String trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        Button full = findViewById(R.id.full_table);

        final LinearLayout timetable_error = findViewById(R.id.timetable_error);
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimetableActivity.this, FulltimetableActivity.class);
                intent.putExtra("timetable", trans);
                TimetableActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final String name = sharedPreferences.getString(NAME, null);
        final String classes = sharedPreferences.getString(CLASS, null);
        final String section = sharedPreferences.getString(SECTION, null);
        final String type = sharedPreferences.getString(TYPE, null);
        if (type.equals("teacher")) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference user = db.collection("School").document("Accouts").collection("teachers").document(name);
            user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        String timetable_url = (String) doc.get("timetable");

                        if (timetable_url.equals("")) {
                            timetable_error.setVisibility(View.VISIBLE);
                        } else {
                            new DownloadFilesTask().execute(timetable_url);
                        }
                    }
                }
            });
        } else {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference user = db.collection("School").document("Classes").collection(classes).document(section);
            user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        String timetable_url = (String) doc.get("timetable");
                        if (timetable_url.equals("")) {
                            timetable_error.setVisibility(View.VISIBLE);
                        } else {
                            new DownloadFilesTask().execute(timetable_url);
                        }

                    }
                }
            });
        }

    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... strings) {

            String string = strings[0];
            URL url = null;
            StringBuilder sb = new StringBuilder();
            try {
                url = new URL(string);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine).append(",");
                }
                in.close();
                return sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

        protected void onPostExecute(String result) {
            Button full = findViewById(R.id.full_table);

            full.setVisibility(View.VISIBLE);

            TextView day_display = findViewById(R.id.day);
            TextView text_display = findViewById(R.id.display_text);
            LinearLayout view = findViewById(R.id.view);
            LinearLayout rootView = (LinearLayout) findViewById(R.id.root_layout);
            ProgressBar loading_day = findViewById(R.id.loading_day);
            loading_day.setVisibility(View.INVISIBLE);
            day_display.setVisibility(View.VISIBLE);
            trans = result;

            String[] words = result.split(",");

            enteries = words;
            final ArrayList<String> mylist = new ArrayList<String>();
            for (int i = 0; i < enteries.length; i = i + 1) {
                mylist.add(enteries[i]);


            }
            String weekDay;
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
            Calendar calendar = Calendar.getInstance();
            weekDay = dayFormat.format(calendar.getTime()).trim();
            day_display.setText(weekDay);

            if (weekDay.equals("Sunday")) {
                view.setVisibility(View.VISIBLE);
                rootView.setVisibility(View.GONE);
                text_display.setVisibility(View.VISIBLE);
                text_display.setText("Go and enjoy.");

            } else {

                int index_day = mylist.indexOf(weekDay);
                int period = 1;
                for (int i = index_day + 1; i < index_day + 9; i = i + 1) {
                    TextView wordView = new TextView(TimetableActivity.this);
                    LinearLayout divider = new LinearLayout(TimetableActivity.this);

                    divider.setMinimumHeight(1);
                    divider.setPadding(2, 2, 2, 2);
                    divider.setBackgroundColor(getResources().getColor(R.color.grey));
                    wordView.setPadding(20, 20, 20, 20);
                    wordView.setTextSize(20);
                    String weekday = enteries[i];
                    String display = String.valueOf(period) + "            " + weekday;
                    wordView.setText(display);
                    rootView.addView(wordView);
                    rootView.addView(divider);
                    period++;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

    }
}
