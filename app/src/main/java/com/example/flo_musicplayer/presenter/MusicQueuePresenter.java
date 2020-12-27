package com.example.flo_musicplayer.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.flo_musicplayer.interfaceCollection;
import com.example.flo_musicplayer.model.testModel;

public class MusicQueuePresenter implements interfaceCollection.MusicQueueP {
    private interfaceCollection.MusicQueueV MQView;
    private testModel tModel;

    public MusicQueuePresenter(interfaceCollection.MusicQueueV v){
        this.MQView = v;
    }

    public void setLog(String str){
        Log.e("MyMessage",str);
        MQView.Toast(str);
    }
}
