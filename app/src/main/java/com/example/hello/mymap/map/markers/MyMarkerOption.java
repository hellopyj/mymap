package com.example.hello.mymap.map.markers;

import android.os.Parcel;
import android.view.View;

import com.example.hello.mymap.MyApplication;
import com.example.hello.mymap.R;
import com.example.hello.mymap.map.adapter.MyMarkerAdapter;
import com.example.hello.mymap.map.modle.MyMessage;
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * Created by Lovepyj on 2016/5/24.
 */
public class MyMarkerOption extends BaseMarkerOptions<MyBaseMarker,MyMarkerOption> {
    public MyMessage getMessage() {
        return message;
    }

    public void setMessage(MyMessage message) {
        this.message = message;
    }

    public MyMessage message;

    public MyMarkerOption(String title, LatLng myposition, Icon icon)
    {
        this.title=title;
        this.position=myposition;
        if(icon!=null)
        this.icon=icon;
    }
    public MyMarkerOption(MyMessage myMessage)
    {
        this.message=new MyMessage(myMessage.type,myMessage.latitude,myMessage.longitude,myMessage.content,myMessage.username);
        this.position=new LatLng(myMessage.latitude,myMessage.longitude);
        this.title="nihao";
        setIcon(myMessage.type);
    }
    public MyMarkerOption() {}
    @Override
    public MyMarkerOption getThis() {
        return this;
    }

    @Override
    public MyBaseMarker getMarker() {
        return new MyBaseMarker(this,message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
    void setIcon(int type)
    {
        IconFactory mIconFactory = IconFactory.getInstance(MyApplication.applicationContext);
        int tmp=0;
        switch (type)
        {

            case MyMarkerAdapter.MARKER_TXT:
                tmp=R.drawable.ic_sms_black_24dp;
                break;
            case MyMarkerAdapter.MARKER_PCICTURE:
                tmp=R.drawable.ic_panorama_black_24dp;
                break;
            case MyMarkerAdapter.MARKER_AR:
                tmp=R.drawable.ic_camera_black_24dp;
                break;
            case MyMarkerAdapter.MARKER_VIDEO:
                tmp=R.drawable.ic_movie_filter_black_24dp;
                break;
            case MyMarkerAdapter.MARKER_VOICE:
                tmp=R.drawable.ic_settings_voice_black_24dp;
                break;

        }
        if(tmp!=0)
        icon= mIconFactory.fromResource(tmp);
    }
}
