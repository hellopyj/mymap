package com.example.hello.mymap.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lovepyj on 2016/6/13.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class GeocoderBean {

    public String place_id;
    public String osm_type;
    public String osm_id;
    public double lat;
    public double lon;
    public String display_name;
    public HashMap<String,String> address;
    public List<String> boundingbox;

    @Override
    public String toString() {
       // double i= boundingbox.get(1).doubleValue();
        return place_id+","+osm_type+","+osm_id+","+lat+","+lon+","+display_name+address.toString()+
                ","+boundingbox.toString();
    }
}
