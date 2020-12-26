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
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //This link is for testing purposes only!!!
    private static String JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key=250ae9a3b22b8c7bdcc469913a866ce8&language=en-US&page=1";

    List<APIModelMovie> mMovieList;
    RecyclerView mRecyclerView;




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


    //Algemene poging om data te filteren oafhankelijk van de knop waarop is gedrukt.
    //lijst meegeven met de alle films in en ook een mood megeven

    public void filtermovies(List<APIModelMovie> mMovieList, Moods mood) {
        List<APIModelMovie> NewmMovieList;
        NewmMovieList = new ArrayList<>();
        APIModelMovie model = new APIModelMovie();

        for (APIModelMovie e: mMovieList)     // de lijst met films doorlopen
        {
            for (int i=0; i < e.genreID.length;i++) // het genre ID van de film doorlopen
            {
                for(int j=0; j < mood.MoodID.length;j++) //het genre ID van de Mood doorlopan
                {
                       if (e.genreID[i] == mood.MoodID[j]) // het genre ID en de Mood ID vergelijken
                       {
                           model.setGenreID(e.getGenreID());
                           model.setId(e.getId());
                           model.setImgURL(e.getImgURL());
                           model.setTitle(e.getTitle());//toevoegen aan een nieuwe MovieList  HULP VRAGEN BIJ MILAN

                           NewmMovieList.add(model);
                       }
                               // verwijderen van deze film uit de lijst?
                }
            }
        }

        PutDataIntoRecyclerView(NewmMovieList);
        /*
        mMovieList = NewmMovieList;
        PutDataIntoRecyclerView(mMovieList);
        De nieuwe lijst printen.
        */
    }

    // filteren op Mood/genre
    public void FilterHappy(List<APIModelMovie> mMovielist) {
        int[] x = {28};
        Moods Action = new Moods("Action",x);
        filtermovies(mMovieList,Action);
    }
    //btn_click
    public void FilterHappy_btnClick(View view) {
        FilterHappy(mMovieList);
        //om een of andere reden moet dees wat het zou eens allemaal mooi samen kunne
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
                    model.setGenreIDFromJSON(movie.getString("genre_ids"));

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
}