package com.example.flo_musicplayer;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.flo_musicplayer.model.MusicPlayerService;

public interface interfaceCollection {

    interface MusicQueueP {

        void setLog(String str);
        void prepareAudio(String Url);
        void resumeAudio();
        void pauseAudio();
        //void playAudio();
        //void stopAudio();

        void setSeekBar(int position);
        int getSeekBarPosition();
        void setTotalDuration(int dur);
        void readyMusic();

    }

    interface MusicQueueV{
        void Toast(String str);
        void updateSeekBar();
        boolean isMusicPlaying();
        void notifyMusicDone();
        void checkLyricsPosition(int position);
        void notifySplitLyrics();

    }
    interface  FullLyricsP{
        void setSeekBar(int position);
        void resumeAudio();
        void pauseAudio();
        int getSeekBarPosition();
        void readyMusic();
    }
    interface FullLyricsV{

        void updateSeekBar();

        void notifyMusicDone();

        void checkLyricsPosition(int position);
    }

}
