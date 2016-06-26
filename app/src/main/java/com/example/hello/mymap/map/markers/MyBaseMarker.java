package com.example.hello.mymap.map.markers;

import android.view.View;

import com.example.hello.mymap.map.modle.MyMessage;
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.Marker;

import java.util.List;

/**
 * Created by Lovepyj on 2016/5/24.
 */
public class MyBaseMarker extends Marker {


    public MyMessage getMessage() {
        return message;
    }
    //关联的类
    public List<Object> brother=null;

    public void setMessage(MyMessage message) {
        this.message = message;
    }

    public MyMessage message;
//    public View.OnClickListener getOnClickListener() {
//        return onClickListener;
//    }
//
//    public void setOnClickListener(View.OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }
//
//    public View.OnClickListener onClickListener=null;
    public MyBaseMarker(BaseMarkerOptions baseMarkerOptions, MyMessage myMessage) {
        super(baseMarkerOptions);
       // this.onClickListener=onClickListener;
        this.message=myMessage;
    }
}
