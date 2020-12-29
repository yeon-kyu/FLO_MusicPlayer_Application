package com.example.flo_musicplayer;

public interface interfaceCollection {

    interface MusicQueueP{

        void setLog(String str);
        void prepareAudio(String Url);
        //void playAudio();
        void pauseAudio();
        void resumeAudio();
        //void stopAudio();

        void setSeekBar(int position);
        int getSeekBarPosition();
        void setDuration(int dur);
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

}
