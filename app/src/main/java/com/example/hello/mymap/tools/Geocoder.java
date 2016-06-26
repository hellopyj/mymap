package com.example.hello.mymap.tools;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.example.hello.mymap.MyApplication;
import com.example.hello.mymap.map.acyivity.PlaceInfo;
import com.example.hello.mymap.model.GeocoderBean;
import com.example.hello.mymap.model.ReturnJs;
import com.example.hello.mymap.utils.Config;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Lovepyj on 2016/6/12.
 */
public class Geocoder {
    public void setGeocoderComplete(GeocoderComplete geocoderComplete) {
        this.geocoderComplete = geocoderComplete;
    }
    LatLng point;
    int zone;
    public Geocoder(LatLng point, final int zone)
    {
        this.point=point;
        this.zone=zone;
    }
    GeocoderComplete geocoderComplete;
    public void placeInfo()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url=String.format(Config.PLACE_INFO,point.getLatitude(),point.getLongitude());
                Log.v("url",url);
                final Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response response = MyApplication.getInstance().httpClient.newCall(request).execute();
                    String json = response.body().string();
                    ReturnJs returnJs=LoganSquare.parse(json,ReturnJs.class);
                   // Log.v("sss",json);
                    if(returnJs.code==1)
                    {
                        Intent intent=new Intent(MyApplication.mainContext, PlaceInfo.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("content",returnJs.content);
                        intent.putExtras(bundle);
                        MyApplication.mainContext.startActivity(intent);
                    }
                    if(geocoderComplete!=null)
                    geocoderComplete.complete(returnJs);
                   // Log.v("dd",geocoderBean.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //请求完成接口类
    public abstract class GeocoderComplete {
        public abstract void complete(ReturnJs returnJs);
    }
}
