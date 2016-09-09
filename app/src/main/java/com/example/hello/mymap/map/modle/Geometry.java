package com.example.hello.mymap.map.modle;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by Lovepyj on 2016/7/15.
 */
@JsonObject
public class Geometry {
    @JsonField
    private String type;
    @JsonField
    private List<Double> coordinates ;

    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setCoordinates(List<Double> coordinates){
        this.coordinates = coordinates;
    }
    public List<Double> getCoordinates(){
        return this.coordinates;
    }

}
