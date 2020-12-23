package com.campomilan.moodwatch;

public class APIModelMovie {
    String id;
    String title;
    String imgURL;

    public APIModelMovie(String id, String title, String imgURL){
        this.id = id;
        this.title = title;
        this.imgURL = imgURL;
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
