package com.example.flo_musicplayer.presenter;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.example.flo_musicplayer.interfaceCollection;
import com.example.flo_musicplayer.model.testModel;

import java.io.IOException;

public class MusicQueuePresenter implements interfaceCollection.MusicQueueP {
    private interfaceCollection.MusicQueueV MQView;
    private testModel tModel;


    MediaPlayer player;
    Thread timerThread;
    int position;
    int duration;

    public MusicQueuePresenter(interfaceCollection.MusicQueueV v){
        this.MQView = v;
    }

    @Override
    public void readyMusic(){
        synchronized (this){
            position = 0;
        }

        timerThread = new Thread(new Runnable(){
            public void run(){
                while(true){
                    if(MQView.isMusicPlaying()){
                        MQView.updateSeekBar();
                    }
                    synchronized (this){
                        position = getSeekBarPosition();
                    }
                    if(position >= duration){
                        synchronized (this){
                            position = 0;
                        }
                        MQView.updateSeekBar();
                        MQView.notifyMusicDone();
                        break;
                    }
                    MQView.checkLyricsPosition(position);


                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        MQView.notifySplitLyrics();
        timerThread.start();
    }

    @Override
    public void setSeekBar(int position){
        player.seekTo(position);
        synchronized (this){
            this.position = position;
        }

    }

    @Override
    public int getSeekBarPosition(){
        return player.getCurrentPosition();
    }

    @Override
    public void setDuration(int dur){
        duration = dur;
    }


    public void setLog(String str){
        Log.e("MyMessage",str);
        MQView.Toast(str);
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

    @Override
    public void pauseAudio() {
        if (player != null) {
            player.pause();
            position = player.getCurrentPosition();

            Log.e("pause current position",player.getCurrentPosition()+"");
            MQView.Toast("중지");
        }
    }
    public void resumeAudio() {
        if (player != null && !player.isPlaying()) {
            player.seekTo(position);
            player.start();

            Log.e("resume current position",position+"");
            MQView.Toast("재생");
        }
    }

//    @Override
//    public void playAudio() {
//
//        player.start();
//
//        MQView.Toast("재생 시작");
//
//    }

//    public void stopAudio() {
//        if(player != null && player.isPlaying()){
//            player.stop();
//
//            MQView.Toast("중지");
//        }
//    }


    private void closePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

}
