package com.campomilan.moodwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class SelectionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String FILE_NAME = "moods.json"; // TODO: per mood aanmaken
    final File fileMoods = new File(Environment.getDataDirectory(), FILE_NAME);
    private String jsonString;

    String[] moods;
    String selectedMood = "*select a mood first*!";
    TextView mSelectGenreTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        mSelectGenreTextView = (TextView) findViewById(R.id.Text_SelectGenres);
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
        selectedMood = (String) adapterView.getItemAtPosition(position);
        String selectGenresText = getResources().getString(R.string.select_genres) + " " + selectedMood;
        mSelectGenreTextView.setText(selectGenresText);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void SubmitGenreSelection(View view) {
        String text = "json die nog geschreven moet worden"; // TODO: JSON moet hier nog geparsed worden naar een string!
        FileOutputStream fos = null;

        //filestream openen om naar file te schrijven
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());

            Log.d("json file", String.valueOf(fileMoods));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // einde filestream
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LoadMoodList(); //TESTING PURPOSES
    }
    //TODO: 1. Veranderen van activity naar de lijst met films en file daar openen en omzetten naar json
    //      2. json omzetten naar GET-request in TMDB-API

    public void LoadMoodList() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            this.jsonString = sb.toString();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                    Log.v("fileRead", this.jsonString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}