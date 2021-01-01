package com.example.flo_musicplayer.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flo_musicplayer.R;
import com.example.flo_musicplayer.model.Post;

public class FullLyricsActivity extends Activity {

    TextView singerText,titleText;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_lyrics_layout);
        Post MyPost = getIntent().getBundleExtra("bundle").getParcelable("PostData");
        Log.e("getIntent Test",MyPost.getTitle());
        Toast.makeText(this, MyPost.getTitle(), Toast.LENGTH_SHORT).show();
        titleText = findViewById(R.id.titleText);
        singerText = findViewById(R.id.singerText);
        titleText.setText(MyPost.getTitle());
        singerText.setText(MyPost.getSinger());

    }
}
