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

    private LinearLayout textContainer;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_lyrics_layout);
        MyPost = getIntent().getBundleExtra("bundle").getParcelable("PostData");
        Log.e("getIntent Test",MyPost.getTitle());
        Toast.makeText(this, MyPost.getTitle(), Toast.LENGTH_SHORT).show();

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
                } else {
                    // The toggle is disabled
                    toggleText.setText("seek off");
                }
            }
        });
        if(toggleButton.isEnabled()){
            toggleText.setText("seek off");
        }
        else{
            toggleText.setText("seek on");
        }

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

        displayLyrics();
    }

    private void displayLyrics(){
        textContainer = findViewById(R.id.lyrics_textLayout);
        MyPost.splitLyrics();
        int size = MyPost.getLyricsSize();
        Log.e("Lyrics Size : ",size+"");
        for(int i=0;i<size;i++){
            String temp=MyPost.timeAndLyrics[i].getLy();
            makeTextView(temp);
        }
    }
    private void makeTextView(String str){
        TextView tempView = new TextView(this);
        tempView.setText(str);
        tempView.setTextSize(18);
        tempView.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        tempView.setLayoutParams(lp);
        textContainer.addView(tempView);
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
    public void checkLyricsPosition(int position) {
        //TODO
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        MusicPlayerService.getInstance().freeFLPresenter();
    }

}
