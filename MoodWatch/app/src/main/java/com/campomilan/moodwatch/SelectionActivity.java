package com.campomilan.moodwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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

    public static final String MOODS_SAD = "sad.txt"; // TODO: per mood aanmaken
    public static final String MOODS_HAPPY = "happy.txt";
    public final File fileHappy = new File(Environment.getDataDirectory(), MOODS_HAPPY);
    public final File fileSad = new File(Environment.getDataDirectory(), MOODS_SAD);
    String currentMood;

    private String outputString;
    private String text="";

    String[] moods;
    String selectedMood = "*select a mood first*!";
    TextView mSelectGenreTextView;

    CheckBox mCheckboxAction;
    CheckBox mCheckboxAdventure;
    CheckBox mCheckboxComedy;
    CheckBox mCheckboxDrama;
    CheckBox mCheckboxHorror;
    CheckBox mCheckboxRomance;
    CheckBox mCheckboxThriller;


// TODO:
    // 1 status checkbox overlopen. KLAAR
    // 2 Schrijven naar file KLAAR
    // 3 verschillende files
    // 4 file selectere en uitlezen.
    // 5 data omzetten en een filter request doen. VOOR mij

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        mSelectGenreTextView = (TextView) findViewById(R.id.Text_SelectGenres);
        moods = getResources().getStringArray(R.array.mood_array);

        mCheckboxAction = (CheckBox) findViewById(R.id.chk_action);
        mCheckboxAdventure = (CheckBox) findViewById(R.id.chk_adventure);
        mCheckboxComedy = (CheckBox) findViewById(R.id.chk_comedy);
        mCheckboxDrama = (CheckBox) findViewById(R.id.chk_drama);
        mCheckboxHorror = (CheckBox) findViewById(R.id.chk_horror);
        mCheckboxRomance = (CheckBox) findViewById(R.id.chk_romance);
        mCheckboxThriller = (CheckBox) findViewById(R.id.chk_thriller);


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
        currentMood = selectedMood.toLowerCase() + ".txt";
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void SubmitGenreSelection(View view) {

        boolean checked = mCheckboxAction.isChecked();
        text += String.valueOf(checked);
        text += "\n";
        checked = mCheckboxAdventure.isChecked();
        text += String.valueOf(checked);
        text += "\n";
        checked = mCheckboxComedy.isChecked();
        text += String.valueOf(checked);
        text += "\n";
        checked = mCheckboxDrama.isChecked();
        text += String.valueOf(checked);
        text += "\n";
        checked = mCheckboxHorror.isChecked();
        text += String.valueOf(checked);
        text += "\n";
        checked = mCheckboxRomance.isChecked();
        text += String.valueOf(checked);
        text += "\n";
        checked = mCheckboxThriller.isChecked();
        text += String.valueOf(checked);
        text += "\n";

        FileOutputStream fos = null;

        //filestream openen om naar file te schrijven

        try {
            fos = openFileOutput(currentMood, MODE_PRIVATE);
            fos.write(text.getBytes());

            Log.d("json file", String.valueOf(currentMood));

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
        text="";

        //uncheck checkboxes after submitting
        if(mCheckboxAction.isChecked())
            mCheckboxAction.toggle();
        if(mCheckboxAdventure.isChecked())
            mCheckboxAdventure.toggle();
        if(mCheckboxComedy.isChecked())
            mCheckboxComedy.toggle();
        if(mCheckboxDrama.isChecked())
            mCheckboxDrama.toggle();
        if(mCheckboxHorror.isChecked())
            mCheckboxHorror.toggle();
        if(mCheckboxRomance.isChecked())
            mCheckboxRomance.toggle();
        if(mCheckboxThriller.isChecked())
            mCheckboxThriller.toggle();
    }
    //TODO: 1. Veranderen van activity naar de lijst met films en file daar openen en omzetten naar json
    //      2. json omzetten naar GET-request in TMDB-API

    public void LoadMoodList() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(currentMood);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            this.outputString = sb.toString();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    Log.v("fileRead", this.outputString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}