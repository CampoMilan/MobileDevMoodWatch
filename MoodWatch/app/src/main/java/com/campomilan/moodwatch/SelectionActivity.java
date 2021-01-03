package com.campomilan.moodwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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

    public static final String MOODS_SAD = "sad.txt";
    public static final String MOODS_HAPPY = "happy.txt";
    public static final String MOODS_ANGRY = "angry.txt";
    public static final String MOODS_SCARED = "scared.txt";
    public static final String MOODS_SLEEPY = "sleepy.txt";
    public static final String MOODS_CURRENT = "current.txt"; //added

    public final File fileHappy = new File(Environment.getDataDirectory(), MOODS_HAPPY);
    public final File fileSad = new File(Environment.getDataDirectory(), MOODS_SAD);
    public final File fileAngry = new File(Environment.getDataDirectory(), MOODS_ANGRY);
    public final File fileScared = new File(Environment.getDataDirectory(), MOODS_SCARED);
    public final File fileSleepy = new File(Environment.getDataDirectory(), MOODS_SLEEPY);
    public final File fileCurrent = new File(Environment.getDataDirectory(), MOODS_CURRENT); //added
    String currentMood;

    private String outputString;
    private String text = "";

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

    List<CheckBox> mCheckboxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        mSelectGenreTextView = (TextView) findViewById(R.id.Text_SelectGenres);
        moods = getResources().getStringArray(R.array.mood_array);

        mCheckboxes.add((CheckBox) findViewById(R.id.chk_action));
        mCheckboxes.add((CheckBox) findViewById(R.id.chk_adventure));
        mCheckboxes.add((CheckBox) findViewById(R.id.chk_comedy));
        mCheckboxes.add((CheckBox) findViewById(R.id.chk_drama));
        mCheckboxes.add((CheckBox) findViewById(R.id.chk_horror));
        mCheckboxes.add((CheckBox) findViewById(R.id.chk_romance));
        mCheckboxes.add((CheckBox) findViewById(R.id.chk_thriller));


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
        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
        String selectGenresText = getResources().getString(R.string.select_genres) + " " + selectedMood;
        mSelectGenreTextView.setText(selectGenresText);
        currentMood = selectedMood.toLowerCase() + ".txt";
//        Log.d("current_mood: ", currentMood)

        checkboxreset();
        checkboxset();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void SubmitGenreSelection(View view) {

        for (CheckBox checkbox : mCheckboxes) {
            boolean checked = checkbox.isChecked();
            text += String.valueOf(checked);
            text += "\n";
        }

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
        text = "";

        //filestream voor de current.txt file en daarin de currentmood in toe te voegen

        text = currentMood;
        text += "\n";

        fos = null;

        try {
            fos = openFileOutput("current.txt", MODE_PRIVATE);
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
        text = "";

        //uncheck checkboxes after submitting
        // checkboxreset();

        startActivity(new Intent(SelectionActivity.this, MainActivity.class));
    }

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
            this.outputString = null;
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    Log.v("fileRead", this.outputString);
                } catch (IOException e) {
                    this.outputString = null;
                    e.printStackTrace();
                }
            }
        }
    }

    public void checkboxreset() {
        for (CheckBox checkbox : mCheckboxes) {
            if (checkbox.isChecked())
                checkbox.toggle();
        }
    }

    public void checkboxset() {

        LoadMoodList();
        String[] StringHolder;
        String holder = this.outputString;
        if (holder != null) {
            StringHolder = holder.split("\n");
            //       for (int i = 0; i< StringHolder.length;i++){Log.d("Stringholder_content",StringHolder[i]);}
            checkboxreset();

            for (int i = 0; i < mCheckboxes.size(); i++) {
                if (StringHolder[i].equals("true")) {
                    mCheckboxes.get(i).toggle();
                }

            }
        }

    }


}