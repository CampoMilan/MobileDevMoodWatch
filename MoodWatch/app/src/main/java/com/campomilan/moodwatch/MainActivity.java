package com.campomilan.moodwatch;

import android.content.Intent;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.JsonReader;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key=250ae9a3b22b8c7bdcc469913a866ce8&language=en-US&page=1";

    List<APIModelMovie> mMovieList;
    RecyclerView mRecyclerView;
    private String outputString;
    private String currentMood;
    private static int[] genreID = {28, 12, 35, 18, 27, 10749, 53};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMovieList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerView);

        GetData getData = new GetData();
        getData.execute();
    }


    //Algemene functie om de data te filteren op basis van de geselecteerde genres bij een bepaalde mood.

    public void filtermovies(List<APIModelMovie> mMovieList, Moods mood) {
        List<APIModelMovie> NewmMovieList;
        NewmMovieList = new ArrayList<>();
        APIModelMovie model;
        boolean isDouble;

        for (APIModelMovie e: mMovieList)     // de lijst met films doorlopen
        {
            for (int i=0; i < e.genreID.size();i++) // het genre ID van de film doorlopen
            {
                for(int j=0; j < mood.MoodID.length;j++) //het genre ID van de Mood doorlopan
                {
                    isDouble = false;
                    if (NewmMovieList.isEmpty() && e.getGenreID().get(i) == mood.MoodID[j])
                    {
                        model = new APIModelMovie();
                        model.setGenreID(e.getGenreID());
                        model.setId(e.getId());
                        model.setFullImgURL(e.getImgURL());
                        model.setTitle(e.getTitle());

                        NewmMovieList.add(model);

                    }
                    else if (e.getGenreID().get(i) == mood.MoodID[j]) // het genre ID en de Mood ID vergelijken
                    {
                        for (int m =0; m < NewmMovieList.size(); m++ )
                        {
                            if(NewmMovieList.get(m).getId() == e.getId()) // controleren of de film al in de lijst staat. om dubbels te vermijden
                            {
                                isDouble = true;
                            }
                        }

                        if(isDouble == false)
                        {
                            model = new APIModelMovie();
                            model.setGenreID(e.getGenreID());
                            model.setId(e.getId());
                            model.setFullImgURL(e.getImgURL());
                            model.setTitle(e.getTitle());

                            NewmMovieList.add(model);
                        }
                    }
                }
            }
        }

        //      De nieuwe lijst printen.
        PutDataIntoRecyclerView(NewmMovieList);

    }

    // filteren op Mood/genre
    public void Filter(List<APIModelMovie> mMovielist)
    {
        String[] StringHolder;
        LoadMoodCurrent();
        String holder = this.currentMood;
        StringHolder = holder.split("\\n");
        String FILE_NAME = StringHolder[0];
        int[] x = GetMoodIDs(FILE_NAME);
        Moods HAPPY = new Moods("HAPPY",x);
        filtermovies(mMovieList, HAPPY);
    }

    // file ophalen en daar uit de MoodID's halen
    public int[] GetMoodIDs(String File)
    {
        LoadMoodList(File); // laden van de correcte data in de outputString
        String[] StringHolder;
        int[] intHolder;
        String holder = this.outputString; // outputstring om zetten naar een array
        ArrayList<Integer> ArrayHolder= new ArrayList<>();
        StringHolder = holder.split("\\n");
        for (int j= 0; j <StringHolder.length; j++)
        {
            if (StringHolder[j].equals("true")) //controleren of de genre id's van de film waren geselecteerd bij de currentMood
            {
                ArrayHolder.add(this.genreID[j]); // indien bijde er een match is worden de genre id's aan een arraylist toegevoegd.
            }
        }
        intHolder = new int[ArrayHolder.size()];  // arraylist omzetten naar int[] (in de mood klassen maken we gebruik van int[] en geen ArrayList

        for (int i = 0; i<ArrayHolder.size() ;i++)
        {
            intHolder[i] = ArrayHolder.get(i);
        }
        return intHolder;
    }


    //btn_click
    public void FilterHappy_btnClick(View view) {
        Filter(mMovieList);
    }

    public void NavigateSelection(View view) {
        finish();
    }

    public class GetData extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String current = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isr.read();
                    }
                    return current;

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                // parse the JSON Object from the datastream string
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i< jsonArray.length(); i++){

                    JSONObject movie = jsonArray.getJSONObject(i);

                    APIModelMovie model = new APIModelMovie();
                    model.setId(movie.getString("id"));
                    model.setTitle(movie.getString("original_title"));
                    model.setImgURL(movie.getString("poster_path"));

                    model.setGenreIDFromJSON(movie.getJSONArray("genre_ids"));

                    mMovieList.add(model);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            PutDataIntoRecyclerView(mMovieList);

        }

    }

    private void PutDataIntoRecyclerView(List<APIModelMovie> movieList){

        MovieListAdapter adapterMovie = new MovieListAdapter(this, movieList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapterMovie);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //tijdelijke oplossing om naar andere activities te navigeren
        switch (item.getItemId()) {
            case R.id.action_moods:
                Intent intent = new Intent(MainActivity.this, SelectionActivity.class);
                startActivity(intent);
                return true;
            default:
                // Do nothing

        }

        return super.onOptionsItemSelected(item);
    }

    // opvragen van een (Mood).txt bestand om de juiste configuratie optehalen
    public void LoadMoodList(String FILE_NAME) {
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

            this.outputString = sb.toString();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ophalen van de geselecteerde Mood uit de current.txt file
    public void LoadMoodCurrent() {
        FileInputStream fis = null;

        try {
            fis = openFileInput("current.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            this.currentMood = sb.toString();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}