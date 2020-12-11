package com.campomilan.moodwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class SelectionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] moods;
    String selectedMood= "*select a mood first*!";
    TextView mSelectGenreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        mSelectGenreTextView = (TextView)findViewById(R.id.Text_SelectGenres);
        moods = getResources().getStringArray(R.array.mood_array);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spinner = findViewById(R.id.spinner_mood);
        spinner.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the moods list
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.mood_array, android.R.layout.simple_spinner_item);
        //Setting the ArrayAdapter data on the Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        selectedMood = adapterView.getItemAtPosition(position).toString();
        String selectGenresText = findViewById(R.id.Text_SelectGenres).toString() + " " + selectedMood;
        mSelectGenreTextView.setText(selectGenresText);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void SubmitGenreSelection(View view) {
        //TODO: genres linken aan geselecteerde mood
    }
}