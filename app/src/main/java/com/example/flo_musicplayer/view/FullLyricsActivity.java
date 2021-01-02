package com.example.flo_musicplayer.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.flo_musicplayer.R;
import com.example.flo_musicplayer.interfaceCollection;
import com.example.flo_musicplayer.model.MusicPlayerService;
import com.example.flo_musicplayer.model.Post;
import com.example.flo_musicplayer.presenter.FullLyricsPresenter;
import com.example.flo_musicplayer.presenter.MusicQueuePresenter;

public class FullLyricsActivity extends Activity implements interfaceCollection.FullLyricsV{

    interfaceCollection.FullLyricsP FLPresenter;
    Post MyPost;

    TextView singerText,titleText, toggleText;
    SeekBar seekBar;
    ToggleButton PlayButton,toggleButton;
    Button backButton;

    TextView[] LyricsTextView;

    private LinearLayout textContainer;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_lyrics_layout);
        MyPost = getIntent().getBundleExtra("bundle").getParcelable("PostData");
        Log.e("getIntent Test",MyPost.getTitle());

        setupMVP();
        setupView();

    }

    private void setupMVP(){
        FLPresenter = new FullLyricsPresenter(this);

    }
    private void setupView(){
        titleText = findViewById(R.id.titleText);
        singerText = findViewById(R.id.singerText);
        titleText.setText(MyPost.getTitle());
        singerText.setText(MyPost.getSinger());
        toggleText = findViewById(R.id.toggleText);
        toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    toggleText.setText("seek on");
                    Toast.makeText(FullLyricsActivity.this, "가사를 터치한 구간부터 재생합니다", Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    toggleText.setText("seek off");
                }
            }
        });
        toggleText.setText("seek off");
        toggleButton.setChecked(false);

        seekBar = findViewById(R.id.SeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    FLPresenter.setSeekBar(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayButton = findViewById(R.id.PlayButton2);
        PlayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(MusicPlayerService.getInstance().isPlaying()){
                    FLPresenter.pauseAudio();
                }
                else{
                    FLPresenter.resumeAudio();
                }
            }
        });
        if(MusicPlayerService.getInstance().isPlaying()){
            PlayButton.setChecked(false);//pause
        }
        else{
            PlayButton.setChecked(true);
        }

        seekBar.setMax(MyPost.getDuration()*1000);

        backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        showLyrics();
        setLyricsListener();
    }

    private void showLyrics(){
        textContainer = findViewById(R.id.lyrics_textLayout);
        MyPost.splitLyrics();
        int size = MyPost.getLyricsSize();
        Log.e("Lyrics Size : ",size+"");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        LyricsTextView = new TextView[size];
        for(int i=0;i<size;i++){
            LyricsTextView[i] = new TextView(this);
            String str=MyPost.timeAndLyrics[i].getLy();
            LyricsTextView[i].setText(str);
            LyricsTextView[i].setTextSize(18);
            LyricsTextView[i].setTextColor(Color.WHITE);

            LyricsTextView[i].setLayoutParams(lp);
            textContainer.addView(LyricsTextView[i]);
        }
    }
    private void setLyricsListener(){
        int size = MyPost.getLyricsSize();
        for(int i=0;i<size;i++){
            final int cur = i;
            LyricsTextView[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(toggleButton.isChecked())
                        FLPresenter.setSeekBar(MyPost.timeAndLyrics[cur].getTime());
                }
            });
        }
    }

    public void updateSeekBar(){
        seekBar.setProgress(FLPresenter.getSeekBarPosition());
    }
    public void notifyMusicDone(){
        PlayButton.setChecked(true);
        FLPresenter.setSeekBar(0);
        runOnUiThread(new Runnable(){
            public void run(){
                FLPresenter.pauseAudio();
                updateSeekBar();
                FLPresenter.readyMusic();
            }
        });
    }

    @Override
    public void checkLyricsPosition(int pos) {
        //TODO
        //기본적으로 MusicQueuePresentation에서 생성한 스레드에서 동작
        final int size = MyPost.getLyricsSize();
        int cur = 0;
        boolean Doneflag = false;
        for(int i=0;i<size;i++){
            int temp = MyPost.timeAndLyrics[i].getTime();
            if(pos<temp){ //현재 seekbar의 위치가 탐색위치 temp보다 작을때
                cur = i-1;
                //Log.e("currentt cur : ",cur+", "+pos+", "+temp);
                Doneflag = true;
                break;
            }
        }
        //Log.e("currentt cur : ",cur+", "+pos);
        if(!Doneflag){
            cur = size-1;
        }

        boolean introflag = false;
        if(cur==-1){
            cur = 0;
            introflag = true;
        }
        final int finalCur = cur;
        final boolean finalIntroflag = introflag;

        runOnUiThread(new Runnable(){
            public void run(){
                setAllLyricsWhite();
                if(finalCur < size){//재생중인 가사 하이라이팅

                    if(finalIntroflag ==false){ //초반 intro는 지났을때
                        LyricsTextView[finalCur].setTextColor(Color.parseColor("#6666ff"));
                    }
                    else{//가사 전에 초반 연주중

                    }

                }
                else{ //가사 다 끝나고 후반 연주중

                }


            }
        });
    }

    private void setAllLyricsWhite(){
        int size = MyPost.getLyricsSize();
        for(int i=0;i<size;i++){
            LyricsTextView[i].setTextColor(Color.parseColor("#ffffff"));
        }
    }


    @Override
    protected void onDestroy() {
        MusicPlayerService.getInstance().freeFLPresenter();
        super.onDestroy();

    }

}
