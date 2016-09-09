package com.example.hello.mymap.map.modle;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Lovepyj on 2016/7/15.
 */
@JsonObject
public class Earthquake {
    @JsonField
    private String type;
    @JsonField
    private Properties properties;
    @JsonField
    private Geometry geometry;
    @JsonField
    private String id;

    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setProperties(Properties properties){
        this.properties = properties;
    }
    public Properties getProperties(){
        return this.properties;
    }
    public void setGeometry(Geometry geometry){
        this.geometry = geometry;
    }
    public Geometry getGeometry(){
        return this.geometry;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
}
