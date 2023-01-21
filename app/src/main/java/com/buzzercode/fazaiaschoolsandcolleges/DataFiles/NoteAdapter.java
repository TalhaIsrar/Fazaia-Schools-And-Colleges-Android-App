package com.buzzercode.fazaiaschoolsandcolleges.DataFiles;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.core.content.ContextCompat;

import com.buzzercode.fazaiaschoolsandcolleges.R;

import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<Note> {

    public NoteAdapter(Activity context, ArrayList<Note> note) {
        super(context, 0, note);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Note currentNote = getItem(position);
        TextView heading = (TextView) listItemView.findViewById(R.id.heading);
        heading.setText(currentNote.getmHeading());
        TextView description = (TextView) listItemView.findViewById(R.id.description);
        description.setText(currentNote.getmDescription());
        TextView time = (TextView) listItemView.findViewById(R.id.time);
        time.setText(currentNote.getmTime());
        TextView priority = (TextView) listItemView.findViewById(R.id.priority);
        priority.setText(currentNote.getmPriority());
        GradientDrawable circle = (GradientDrawable) priority.getBackground();
        int priorityColor = getPriorityColor(currentNote.getmPriority());
        circle.setColor(priorityColor);
        return listItemView;
    }
    private int getPriorityColor(String priority) {
        int ColorResourceId;
        int priority_int= Integer.parseInt(priority);
        switch (priority_int) {
            case 0:
            case 1:
                ColorResourceId = R.color.priority1;
                break;
            case 2:
                ColorResourceId = R.color.priority2;
                break;
            case 3:
                ColorResourceId = R.color.priority3;
                break;
            case 4:
                ColorResourceId = R.color.priority4;
                break;
            case 5:
                ColorResourceId = R.color.priority5;
                break;
            default:
                ColorResourceId = R.color.priority1;
                break;
        }

        return ContextCompat.getColor(getContext(), ColorResourceId);
    }
}
