package com.example.flo_musicplayer.model;

import android.util.Log;

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

    public TimeAndLyrics[] timeAndLyrics;
    private int size;

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

    public void splitLyrics(){
        String[] lyricsParts = lyrics.split("\\n");
        size = lyricsParts.length;
        timeAndLyrics = new TimeAndLyrics[size];
        for(int i=0;i<size;i++){
            timeAndLyrics[i] = new TimeAndLyrics();
            String temp = lyricsParts[i];

            timeAndLyrics[i].time = Integer.parseInt(temp.substring(1,3))*60000
                    +Integer.parseInt(temp.substring(4,6))*1000
                    +Integer.parseInt(temp.substring(7,10));
            timeAndLyrics[i].ly = temp.substring(11);
            //Log.e("time & ly : ",timeAndLyrics[i].time+", "+timeAndLyrics[i].ly);
        }
    }
    public int getLyricsSize(){
        return size;
    }

    public class TimeAndLyrics{
        int time;
        String ly;

        public int getTime(){
            return time;
        }
        public String getLy(){
            return ly;
        }
    }
}
