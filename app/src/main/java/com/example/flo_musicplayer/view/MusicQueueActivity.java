package com.example.flo_musicplayer.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flo_musicplayer.Post;
import com.example.flo_musicplayer.R;
import com.example.flo_musicplayer.RetrofitAPI;
import com.example.flo_musicplayer.interfaceCollection;
import com.example.flo_musicplayer.presenter.MusicQueuePresenter;


import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MusicQueueActivity extends Activity implements interfaceCollection.MusicQueueV{

//    MusicQueuePresenter MQPresenter;
    interfaceCollection.MusicQueueP MQPresenter;

    TextView singerText,albumText,titleText;
    ImageButton playButton;

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

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        retrofitAPI.getSongData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Post>() {
                    @Override
                    public void accept(Post post) throws Exception {
                        Log.e("get SongData","success");
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

    private void setupView(){
        singerText = findViewById(R.id.singer);
        albumText = findViewById(R.id.album);
        titleText = findViewById(R.id.title);
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                MQPresenter.setLog("hi");
            }
        });
    }
    private void setupMVP(){
        //자동 타입 변환(promotion)
        MQPresenter = new MusicQueuePresenter(this);
    }

    private void displayData(Post post){
        singerText.setText(post.getSinger());
        albumText.setText(post.getAlbum());
        titleText.setText(post.getTitle());
    }

    @Override
    public void Toast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


}
