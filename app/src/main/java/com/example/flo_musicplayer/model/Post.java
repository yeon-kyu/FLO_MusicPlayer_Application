package com.example.flo_musicplayer.model;

import com.google.gson.annotations.SerializedName;

public class Post {
    //singer album title duration image file lyrics
    @SerializedName("singer")
    private String singer;

    @SerializedName("album")
    private String album;

    @SerializedName("title")
    private String title;

    @SerializedName("duration")
    private int duration;

    @SerializedName("image")
    private String imageURL;

    @SerializedName("file")
    private String fileURL;

    @SerializedName("lyrics")
    private String lyrics;

    public String getSinger(){
        return singer;
    }
    public String getAlbum(){
        return album;
    }
    public String getTitle(){
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getFileURL() {
        return fileURL;
    }

    public String getLyrics() {
        return lyrics;
    }
}
