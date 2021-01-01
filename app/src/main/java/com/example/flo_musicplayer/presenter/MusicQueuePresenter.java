package com.example.flo_musicplayer.presenter;

import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.flo_musicplayer.interfaceCollection;
import com.example.flo_musicplayer.model.MusicPlayerService;
import com.example.flo_musicplayer.model.testModel;

import java.io.IOException;

public class MusicQueuePresenter implements interfaceCollection.MusicQueueP {

    private interfaceCollection.MusicQueueV MQView;


    public MusicQueuePresenter(interfaceCollection.MusicQueueV v){
        this.MQView = v;
        MusicPlayerService.getInstance().setMQPresenter(this);

    }


    public void notifySplitLyrics(){
        MQView.notifySplitLyrics();
    }
    public void updateSeekBar(){
        MQView.updateSeekBar();
    }
    public void notifyMusicDone(){
        MQView.notifyMusicDone();
    }

    public void checkLyricsPosition(int position){
        MQView.checkLyricsPosition(position);
  }


    public void setLog(String str){
        Log.e("MyMessage",str);
        MQView.Toast(str);
    }

    @Override
    public void prepareAudio(String Url) {
        MusicPlayerService.getInstance().prepareAudio(Url);
    }

    @Override
    public void pauseAudio() {
        MusicPlayerService.getInstance().pauseAudio();
    }

    @Override
    public void resumeAudio() {
        MusicPlayerService.getInstance().resumeAudio();
    }

    @Override
    public void setSeekBar(int position) {
        MusicPlayerService.getInstance().setSeekBar(position);
    }

    @Override
    public void setTotalDuration(int dur) {
        MusicPlayerService.getInstance().setTotalDuration(dur);
    }

    @Override
    public void readyMusic() {
        MusicPlayerService.getInstance().readyMusic();
    }

    public int getSeekBarPosition(){
        return MusicPlayerService.getInstance().getSeekBarPosition();
    }


}
