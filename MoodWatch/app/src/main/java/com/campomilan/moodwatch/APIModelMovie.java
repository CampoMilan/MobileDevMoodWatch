package com.campomilan.moodwatch;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class APIModelMovie {
    String id;
    String title;
    String imgURL;
    ArrayList<Integer> genreID;

    public APIModelMovie(String id, String title, String imgURL, ArrayList<Integer> genreID){
        this.id = id;
        this.title = title;
        this.imgURL = imgURL;
        this.genreID = genreID;
    }

    public APIModelMovie() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = "https://image.tmdb.org/t/p/w300";

        this.imgURL = this.imgURL + imgURL;
    }

    public void setFullImgURL(String imgURL)
    {
        this.imgURL = imgURL;
    }

    public  ArrayList<Integer> getGenreID(){return  genreID;}

    public void setGenreID(ArrayList<Integer> ID)
    {
        this.genreID = ID;
    }

    // functie die een zet een  JsonArray (voor de genre ids) om in een ArrayList. (nodig voor het ophalen van de API)
    public void setGenreIDFromJSON(JSONArray jsonArray) {


        ArrayList<Integer> Holder = new ArrayList<>();
        try
        {
            for (int i = 0; i < jsonArray.length(); i++)
                {
                Holder.add(Integer.parseInt(jsonArray.getString(i)));
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.genreID = Holder;
    }
}
