package com.example.flo_musicplayer.model;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.flo_musicplayer.presenter.FullLyricsPresenter;
import com.example.flo_musicplayer.presenter.MusicQueuePresenter;

import java.io.IOException;

public class MusicPlayerService {

    private static MusicPlayerService instance = new MusicPlayerService();

    private static final int MusicQueueStatus = 0;
    private static final int FullLyricsStatus = 1;

    MusicQueuePresenter MQPresenter;
    FullLyricsPresenter FLPresenter;

    MediaPlayer player;
    Thread timerThread;
    int position;
    int totalDuration;

    private int activityStatus;

    private MusicPlayerService(){
    }

    public static MusicPlayerService getInstance(){
        return instance;
    }

    public void setMQPresenter(MusicQueuePresenter MQP){
        this.MQPresenter = MQP;
        activityStatus = MusicQueueStatus;
    }
    public void setFLPresenter(FullLyricsPresenter FLP){
        this.FLPresenter = FLP;
        activityStatus = FullLyricsStatus;
    }
    public void freeFLPresenter(){
        this.FLPresenter = null;
        activityStatus = MusicQueueStatus;
    }

    public void readyMusic(){
        synchronized (this){
            position = 0;
        }

        timerThread = new Thread(new Runnable(){
            public void run(){
                while(true){
                    if(isMusicPlaying()){
                        seekbarUpdate();
                    }
                    synchronized (this){
                        position = getSeekBarPosition();
                    }
                    if(position >= totalDuration){
                        synchronized (this){
                            position = 0;
                        }
                        seekbarUpdate();
                        notifyMusicDone();
                        break;
                    }
                    checkLyricsPosition();


                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        MQPresenter.notifySplitLyrics();
        timerThread.start();
    }


    private boolean isMusicPlaying(){
        if(player.isPlaying())
            return true;
        else
            return false;
    }

    private void seekbarUpdate(){
        if(activityStatus==MusicQueueStatus){
            if(MQPresenter!=null)
                MQPresenter.updateSeekBar();
        }
        else{
            if(FLPresenter!=null)
                FLPresenter.updateSeekBar();
        }
    }
    public void setSeekBar(int position){
        player.seekTo(position);
        synchronized (this){
            this.position = position;
        }

    }
    public int getSeekBarPosition(){
        return player.getCurrentPosition();
    }
    public void notifyMusicDone(){
        if(activityStatus==MusicQueueStatus){
            MQPresenter.notifyMusicDone();
        }
        else{
            FLPresenter.notifyMusicDone();

        }
    }

    public void checkLyricsPosition(){
        if(activityStatus==MusicQueueStatus){
            if(MQPresenter!=null)
                MQPresenter.checkLyricsPosition(position);
        }
        else{
            if(FLPresenter!=null)
                FLPresenter.checkLyricsPosition(position);
        }

    }
    public void setTotalDuration(int dur){
        totalDuration = dur;
    }
    public void prepareAudio(String url){
        closePlayer();

        player = new MediaPlayer();
        try {
            player.setDataSource(url);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void closePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public void pauseAudio() {
        if (player != null) {
            player.pause();
            position = player.getCurrentPosition();

            Log.e("pause current position",player.getCurrentPosition()+"");

        }
    }
    public void resumeAudio() {
        if (player != null && !player.isPlaying()) {
            player.seekTo(position);
            player.start();

            Log.e("resume current position",position+"");

        }
    }

    public boolean isPlaying(){
        if(player==null){
            return false;
        }
        else
            return player.isPlaying();
    }

    public void destroy(){
        MQPresenter = null;
        timerThread.interrupt();
        try {
            instance.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
