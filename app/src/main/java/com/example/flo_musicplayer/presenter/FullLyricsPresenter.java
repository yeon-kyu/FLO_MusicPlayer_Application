package com.example.flo_musicplayer.presenter;

import com.example.flo_musicplayer.interfaceCollection;
import com.example.flo_musicplayer.model.MusicPlayerService;

public class FullLyricsPresenter implements interfaceCollection.FullLyricsP {

    private interfaceCollection.FullLyricsV FLView;

    public FullLyricsPresenter(interfaceCollection.FullLyricsV v){
        this.FLView = v;
        MusicPlayerService.getInstance().setFLPresenter(this);
    }

    @Override
    public void setSeekBar(int position) {
        MusicPlayerService.getInstance().setSeekBar(position);
    }

    @Override
    public void resumeAudio() {
        MusicPlayerService.getInstance().resumeAudio();
    }

    @Override
    public void pauseAudio() {
        MusicPlayerService.getInstance().pauseAudio();
    }
    @Override
    public void readyMusic() {
        MusicPlayerService.getInstance().readyMusic();
    }

    public int getSeekBarPosition(){
        return MusicPlayerService.getInstance().getSeekBarPosition();
    }


    public void updateSeekBar(){
        FLView.updateSeekBar();
    }
    public void notifyMusicDone(){
        FLView.notifyMusicDone();
    }

    public void checkLyricsPosition(int position) {
        FLView.checkLyricsPosition(position);
    }
}
