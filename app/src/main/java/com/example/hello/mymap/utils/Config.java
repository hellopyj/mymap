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
    public static String MAP_ROTE="http://valhalla.mapzen.com/route?json={\"locations\":[{\"lat\":%s,\"lon\":%s},{\"lat\":%s,\"lon\":%s}],\"costing\":\"auto\",\"costing_options\":{\"auto\":{\"country_crossing_penalty\":2000.0}},\"directions_options\":{\"units\":\"miles\"}}&id=my_work_route&api_key=valhalla-7hHJLgR";

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
    public static final String PLACE_INFO=BASE_URL+"placeinfo.php"+"?latitude=%s"+"&longitude=%s"+VERSION;
    //获得路线
    public static final String POLYLINE_INFO=BASE_URL+"getpolyline.php"+"?frlatitude=%s&frlongitude=%s&tolatitude=%s&tolongitude=%s";
    //获得child信息
    public static final String GETCHILD=BASE_URL+"getchild.php"+"?placeid=%s";
    //获得网站
    public static final String GETCHILD_HTML=BASE_URL+"placeshtml/%s";
    //获得vr图
    public static final String GETVR=BASE_URL+"getvr.php"+"?placeid=%s";
    //获得地震
    public static final String GETEARTHQUAKE=BASE_URL+"getnatureinfo.php?type=earthquake&types=%s";

    ///////////////////////////////////////////////////////////////////

}
