package com.example.hello.mymap.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.typeconverters.TypeConverter;

import java.util.HashMap;


/**
 * Created by Lovepyj on 2016/5/12.
 */
@JsonObject
public class ReturnJs {
    @JsonField
    public int code;
    @JsonField
    public HashMap<String,String> content;
}
