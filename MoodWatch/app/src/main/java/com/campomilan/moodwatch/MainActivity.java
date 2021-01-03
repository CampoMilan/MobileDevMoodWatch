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

    //This link is for testing purposes only!!!
    private static String JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key=250ae9a3b22b8c7bdcc469913a866ce8&language=en-US&page=1";

    List<APIModelMovie> mMovieList;
    RecyclerView mRecyclerView;
    private String outputString;
    private String currentMood;
    private static int[] genreID = {28, 12, 35, 18, 27, 10749, 53};



    // TODO: button toevoegen voor next page. nu toont het enkel de eerste 20 films van de filter

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
    //lijst meegeven met de alle films in en ook een mood meegeven

    public void filtermovies(List<APIModelMovie> mMovieList, Moods mood) {
        List<APIModelMovie> NewmMovieList;
        NewmMovieList = new ArrayList<>();
        APIModelMovie model;
        boolean isDouble = false;

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
                        //Log.d("model",String.valueOf(model.id));

                    }
                    else if (e.getGenreID().get(i) == mood.MoodID[j]) // het genre ID en de Mood ID vergelijken
                    {
                        for (int m =0; m < NewmMovieList.size(); m++ )
                        {
                            if(NewmMovieList.get(m).getId() == e.getId())
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
                        //Log.d("model",String.valueOf(model.id));
                    }
                }
            }
        }
        for (int i =0; i <NewmMovieList.size(); i++)
            {
            Log.d("newlist", String.valueOf(NewmMovieList.get(i).title));
            }
        PutDataIntoRecyclerView(NewmMovieList);
        /*
        De nieuwe lijst printen.
        */
    }

    // filteren op Mood/genre
    public void FilterHappy(List<APIModelMovie> mMovielist)
    {
        String[] StringHolder;
        LoadMoodCurrent();
        String holder = this.currentMood;
        StringHolder = holder.split("\\n");
        Log.d("currentMood",String.valueOf(StringHolder[0]));
 //       Log.d("currentMood",String.valueOf(StringHolder[1]));
        String FILE_NAME = StringHolder[0];
    Log.d("FILE: ",String.valueOf(FILE_NAME));
        int[] x = GetMoodIDs(FILE_NAME);
        Moods HAPPY = new Moods("HAPPY",x);
        filtermovies(mMovieList, HAPPY);
    }

    public void FilterSad(List<APIModelMovie> mMovielist)
    {
        String FILE_NAME = "sad.txt";
        int[] x = GetMoodIDs(FILE_NAME);
        Moods SAD = new Moods("SAD",x);
        filtermovies(mMovieList,SAD);
    }

    public void FilterAngry(List<APIModelMovie> mMovielist)
    {
        String FILE_NAME = "angry.txt";
        int[] x = GetMoodIDs(FILE_NAME);
        Moods ANGRY = new Moods("ANGRY",x);
        filtermovies(mMovieList,ANGRY);
    }

    public void FilterScared(List<APIModelMovie> mMovielist)
    {
        String FILE_NAME = "scared.txt";
        int[] x = GetMoodIDs(FILE_NAME);
        Moods SCARED = new Moods("SCARED",x);
        filtermovies(mMovieList,SCARED);
    }

    public void FilterSleepy(List<APIModelMovie> mMovielist)
    {
        String FILE_NAME = "sleepy.txt";
        int[] x = GetMoodIDs((FILE_NAME));
        Moods SLEEPY = new Moods("SLEEPY",x);
        filtermovies(mMovieList,SLEEPY);
    }

    // file ophalen en daar uit de MoodID's halen

    public int[] GetMoodIDs(String File)
    {
        LoadMoodList(File);
        String[] StringHolder;
        int[] intHolder;
        String holder = this.outputString;
        ArrayList<Integer> ArrayHolder= new ArrayList<>();
        StringHolder = holder.split("\\n");
        Log.d("testholder", holder);
        for (int j= 0; j <StringHolder.length; j++)
        {
            if (StringHolder[j].equals("true"))
            {
                ArrayHolder.add(this.genreID[j]);
            }
        }
        intHolder = new int[ArrayHolder.size()];

        for (int i = 0; i<ArrayHolder.size() ;i++)
        {
            intHolder[i] = ArrayHolder.get(i);
        }
        return intHolder;
    }


    //btn_click
    public void FilterHappy_btnClick(View view) {
        FilterHappy(mMovieList);
        //om een of andere reden moet dees wat het zou eens allemaal mooi samen kunne
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
                    Log.v("fileRead", this.outputString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
                    Log.v("fileRead", this.currentMood);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}