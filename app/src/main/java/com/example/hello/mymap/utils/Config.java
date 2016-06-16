package com.example.hello.mymap.utils;

import com.example.hello.mymap.MyApplication;

/**
 * Created by Lovepyj on 2016/5/3.
 */
public class Config {
    //七牛地址
    public static final String QINIU_BASE="http://o7zehvmb8.bkt.clouddn.com/";
    //照片缩略图
    public static final String QINIU_SUOFAN="?imageMogr2/thumbnail/1920x1080";
    //视频帧
    public static final String QINIU_VIDEO_Z="?vframe/jpg/offset/1/w/480/h/360";
    //mapbox样式地址
    public static String MAP_STYLE="mapbox://styles/hellopyj/cinemlqr4004cfokvpecakeq7";
   //路线规划
    public static String MAP_ROTE="http://www.yournavigation.org/api/1.0/gosmore.php?format=geojson&flat=%s&flon=%s&tlat=%s&tlon=%s&v=foot&fast=1&layer=mapnik&instructions=1";

    public static final String VERSION = "&v="+ MyApplication.getInstance().getVersionName();
    //http连接
    public static final String BASE_URL = "http://192.168.199.230/pmymap/";
    //普通登录url
    public static final String LOGIN_URL = BASE_URL+"login.php?"+"uname=%s&pwd=%s&tmpid=%s"+VERSION;
    //普通注册url
    public static final String REGISTER_URL = BASE_URL+"register.php?"+"uname=%s&pwd=%s&nickname=%s"+VERSION;
    public  static final String UPLOAD_URL=BASE_URL+"upload.php";
    //添加marker
    public  static final String ADD_MARKER_URL=BASE_URL+"upmarker.php";//+"?type=%s&latitude=%s&longitude=%s&content=%s";
    //地点解析
    public static String PLACE_INFO=BASE_URL+"placeinfo.php"+"?latitude=%s"+"&longitude=%s"+VERSION;


    ///////////////////////////////////////////////////////////////////

}
