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
