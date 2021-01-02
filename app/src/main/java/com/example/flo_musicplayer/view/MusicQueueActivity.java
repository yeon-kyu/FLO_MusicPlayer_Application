package com.example.flo_musicplayer.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.flo_musicplayer.model.MusicPlayerService;
import com.example.flo_musicplayer.model.Post;
import com.example.flo_musicplayer.R;
import com.example.flo_musicplayer.RetrofitAPI;
import com.example.flo_musicplayer.interfaceCollection;
import com.example.flo_musicplayer.presenter.MusicQueuePresenter;


import java.io.IOException;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MusicQueueActivity extends Activity implements interfaceCollection.MusicQueueV{

//    MusicQueuePresenter MQPresenter;
    interfaceCollection.MusicQueueP MQPresenter;

    TextView singerText,albumText,titleText;
    TextView firstLine,secondLine,thirdLine;
    ImageButton playButton;
    ImageView CoverImage;
    SeekBar seekBar;

    RetrofitAPI retrofitAPI;
    Post MyPost;
    boolean isPlaying;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_queue_layout);

        setupMVP();
        setupView();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        retrofitAPI = retrofit.create(RetrofitAPI.class);
        retrofitAPI.getSongData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Post>() {
                    @Override
                    public void accept(Post post) throws Exception {
                        Log.e("success","getSongData");
                        displayData(post);
                    }
                });


        /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Gson은 JSON을 자바클래스로 바꿔주는데 사용됨


        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        retrofitAPI.getData("song.json").enqueue(new Callback<List<Post>>(){

            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){
                    List<Post> data = response.body();
                    Toast("성공");
                    Log.e("Test",data.get(0).getSinger());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();

            }
        });

         */
    }

    @Override
    protected void onResume() {

        super.onResume();
        isPlaying = MusicPlayerService.getInstance().isPlaying();
        if(isPlaying){
            playButton.setImageResource(R.drawable.sharp_pause_white_36);
        }
        else{
            playButton.setImageResource(R.drawable.sharp_play_arrow_white_36);
        }
    }



    private void setupView(){
        isPlaying = false;

        singerText = findViewById(R.id.singer);
        albumText = findViewById(R.id.album);
        titleText = findViewById(R.id.title);
        firstLine = findViewById(R.id.first_lyrics);
        secondLine = findViewById(R.id.second_lyrics);
        thirdLine = findViewById(R.id.third_lyrics);

        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //MQPresenter.setLog("hi");
                if(!isPlaying){
                    playButton.setImageResource(R.drawable.sharp_pause_white_36);
                    MQPresenter.resumeAudio();
                }
                else{
                    playButton.setImageResource(R.drawable.sharp_play_arrow_white_36);
                    MQPresenter.pauseAudio();
                }
                isPlaying = !isPlaying;
            }
        });
        CoverImage = findViewById(R.id.coverImage);
        seekBar = findViewById(R.id.SeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    MQPresenter.setSeekBar(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        firstLine.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                moveToFullLyricsActivity();
            }
        });
        secondLine.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                moveToFullLyricsActivity();
            }
        });

        thirdLine.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                moveToFullLyricsActivity();
            }
        });
    }
    private void setupMVP(){
        //자동 타입 변환(promotion)
        MQPresenter = new MusicQueuePresenter(this);
    }

    private void moveToFullLyricsActivity(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("PostData",MyPost);
        Intent intent = new Intent(getApplicationContext(), FullLyricsActivity.class);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }

    private void displayData(Post post){

        MyPost = post;

        singerText.setText(MyPost.getSinger());
        albumText.setText(MyPost.getAlbum());
        titleText.setText(MyPost.getTitle());
        int temp_duration = MyPost.getDuration()*1000;
        seekBar.setMax(temp_duration);
        MQPresenter.setTotalDuration(temp_duration);

        OkHttpClient client = new OkHttpClient();

        Request ImageRequest = new Request.Builder()
                .url(MyPost.getImageURL())
                .build();

        client.newCall(ImageRequest).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("request fail","image");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("request success","image");

                InputStream In = response.body().byteStream();
                final Drawable drawable = Drawable.createFromStream(In,"");
                runOnUiThread(new Runnable(){
                    public void run(){
                        CoverImage.setImageDrawable(drawable);
                    }
                });

            }
        });

        MQPresenter.prepareAudio(MyPost.getFileURL());
        MQPresenter.readyMusic();



    }

    @Override
    public void notifySplitLyrics(){
        MyPost.splitLyrics();
    }

    @Override
    public void updateSeekBar(){
        seekBar.setProgress(MQPresenter.getSeekBarPosition());

    }
    @Override
    public void notifyMusicDone(){
        playButton.setImageResource(R.drawable.sharp_play_arrow_white_36);
        isPlaying = false;
        MQPresenter.setSeekBar(0);
        runOnUiThread(new Runnable(){
            public void run(){
                MQPresenter.pauseAudio();
                updateSeekBar();
                MQPresenter.readyMusic();
            }
        });
    }
    public void checkLyricsPosition(int pos){
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

                if(finalCur < size){
                    if(finalIntroflag ==false){ //재생중인 가사 하이라이팅
                        firstLine.setTextColor(Color.parseColor("#6666ff"));
                        firstLine.setText(MyPost.timeAndLyrics[finalCur].getLy());
                    }
                    else{
                        firstLine.setTextColor(Color.parseColor("#ffffff"));
                        firstLine.setText(MyPost.timeAndLyrics[finalCur].getLy());
                    }

                }
                else{
                    firstLine.setText("");
                }

                if(finalCur +1<size) {
                    secondLine.setText(MyPost.timeAndLyrics[finalCur + 1].getLy());
                }
                else{
                    secondLine.setText("");
                }
                if(finalCur +2<size){
                    thirdLine.setText(MyPost.timeAndLyrics[finalCur +2].getLy());
                }
                else{
                    thirdLine.setText("");
                }
            }
        });

    }

    public boolean isMusicPlaying(){
        return isPlaying;
    }


    @Override
    public void Toast(String str){

        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() { //앱을 닫을때 musicPlayerService 싱글톤 해제

        super.onDestroy();
        MQPresenter = null; //memory leak 방지
        MusicPlayerService.getInstance().destroy(); //memory leak 방지_싱글톤 객체 해제
        System.exit(0);
    }


}
