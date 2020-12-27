package com.example.flo_musicplayer;


import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RetrofitAPI {
    String URL = "https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-flo/";

    // base_url + "extra_url" 으로 GET 통신
    @GET("song.json")
    Observable<Post> getSongData();




}
