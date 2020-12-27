package com.example.flo_musicplayer.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.flo_musicplayer.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        //splash 화면에서는 setContentView로 inflate하지 않는 것이 일반적이나 화면비율을 우선 맞추기 위해 일단은 이렇게 함

        final Intent intent = new Intent(this, MusicQueueActivity.class);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                startActivity(intent);
                finish();
            }
        },2000);


    }
}