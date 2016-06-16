package com.example.hello.mymap.map.modle;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.HashMap;

/**
 * Created by Lovepyj on 2016/5/31.
 */
@JsonObject
public class MyMessage {
    @JsonField
    public int type;
    @JsonField
    public HashMap<String,String> content;
    @JsonField
    public double latitude;
    @JsonField
    public double longitude;
    @JsonField
    public  String username;
    public  MyMessage(int type,double latitude,double longitude,HashMap<String,String> content,String username)
    {
        this.type=type;
        this.content=content;
        this.latitude=latitude;
        this.longitude=longitude;
        this.username=username;
    }
    public  MyMessage()
    {
    }
}
