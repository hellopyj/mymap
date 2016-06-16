package com.example.hello.mymap.map.others;

import com.example.hello.mymap.map.markers.MyBaseMarker;
import com.mapbox.mapboxsdk.annotations.Marker;

/**
 * Created by Lovepyj on 2016/5/31.
 */
public abstract class onMarkerClick {
    public abstract void  markAdd(MyBaseMarker marker);
    public abstract void markQuestion(MyBaseMarker marker);
    public abstract void markVideo(MyBaseMarker marker);
    public abstract void markPic(MyBaseMarker  marker);
    public abstract  void markAr(MyBaseMarker marker);
    public abstract  void markVoice(MyBaseMarker marker);
}
