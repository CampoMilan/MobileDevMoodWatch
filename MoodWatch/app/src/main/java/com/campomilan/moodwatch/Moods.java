    package com.campomilan.moodwatch;

import org.json.JSONArray;
import org.json.JSONObject;

public class Moods {
    String Name;
    int[] MoodID;


    public Moods(String Name,int[] MoodID)
    {
        this.Name = Name;
        this.MoodID = MoodID;
    }

    public String getName(){return Name;}
    public int[] getMoodID(){return MoodID;}

    //Hiervoor gaan we gebruik moeten maken van de secondActivity
    public int[] setMoodID(int NewMoodID){

        return MoodID; // gaat waarschijnlijk moeten uitbreiden
    }
}
/*
             Action 28
             Adventure 12
             Animation 16
             Comedy 35
             Crime 80
             Documentary 99
             Drama 18
             Family 10751
             Fantasy 14
             History 36
             Horror 27
             Music 10402
             Mystery 9648
            Romance 10749
            Science Fiction 878
            TV Movie 10770
            Thriller 53
            War 10752
            Western 37
* */

