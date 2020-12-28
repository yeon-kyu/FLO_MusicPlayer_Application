package com.example.flo_musicplayer;


import com.example.flo_musicplayer.model.Post;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface RetrofitAPI {
    String URL = "https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-flo/";

    // base_url + "extra_url" 으로 GET 통신
    @GET("song.json")
    Observable<Post> getSongData();

    @Streaming
    @GET
    Observable<Response<ResponseBody>> getCoverImage(@Url String fileUrl);

    @GET("/2020-flo/{imageName}")
    void getImage(@Path("imageName") String imageName, Callback<Response> callback);

    @Streaming
    @GET
    Observable<Response<ResponseBody>> getMusic(@Url String fileUrl);



}
