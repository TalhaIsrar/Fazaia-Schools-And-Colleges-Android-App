package com.buzzercode.fazaiaschoolsandcolleges;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class FulltimetableActivity extends AppCompatActivity {
    String[] enteries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulltimetable);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final String result = getIntent().getStringExtra("timetable");

        String[] words = result.split(",");
        enteries = words;

        final ArrayList<String> mylist = new ArrayList<String>();
        for (int i = 0; i < enteries.length; i = i + 1) {
            mylist.add(enteries[i]);

        }
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        int i=0;

        while(i<7){
            View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item, null, false);
            TextView name = tableRow.findViewById(R.id.name);
            TextView name1 = tableRow.findViewById(R.id.name1);
            TextView name2 = tableRow.findViewById(R.id.name2);
            TextView name3 = tableRow.findViewById(R.id.name3);
            TextView name4 = tableRow.findViewById(R.id.name4);
            TextView name5 = tableRow.findViewById(R.id.name5);
            TextView name6 = tableRow.findViewById(R.id.name6);
            TextView name7 = tableRow.findViewById(R.id.name7);
            TextView name8 = tableRow.findViewById(R.id.name8);
            name.setText(words[(9*i)]);
            name1.setText(words[1+(9*i)]);
            name2.setText(words[2+(9*i)]);
            name3.setText(words[3+(9*i)]);
            name4.setText(words[4+(9*i)]);
            name5.setText(words[5+(9*i)]);
            name6.setText(words[6+(9*i)]);
            name7.setText(words[7+(9*i)]);
            name8.setText(words[8+(9*i)]);
            tableLayout.addView(tableRow);
            i=i+1;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);

    }
}
