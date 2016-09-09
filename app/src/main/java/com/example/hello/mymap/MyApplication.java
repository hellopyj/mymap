package com.example.hello.mymap;

import android.app.ActivityManager;
//import android.app.Application;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.example.hello.mymap.map.widget.MapInptBar;
import com.example.hello.mymap.utils.Config;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.qiniu.android.storage.UploadManager;

import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;



/**
 * Created by Lovepyj on 2016/5/8.
 */
public class MyApplication extends Application {

    public final String PREF_USERNAME = "username";
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    private static MyApplication instance;
    public static Context applicationContext;
    public static Context mainContext;
   // public static LocationClient mLocationClient = null;
  //  public static UploadManager uploadManager=null;
    public static OkHttpClient httpClient=new OkHttpClient();
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance=this;
        applicationContext=this;
        MapboxAccountManager.start(this,getmapToken());
        //百度地图初始化
        //SDKInitializer.initialize(this);
       // mLocationClient = new LocationClient(this);
       // setGpsOption();
        int pid = android.os.Process.myPid();
        DemoHelper.getInstance().init(applicationContext);
        initImg(this);
    }

    public static MyApplication getInstance() {
        return instance;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /** 初始化Fresco */
    public static void initImg(Context context) {
        Fresco.initialize(context);

    }
    //获得app名字
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
//获得版本名
    public String getVersionName(){
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    public  int getVersionCode()//获取版本号(内部识别号)
    {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return   info.versionCode;
        } catch (Exception e) {
            return 0;
        }
		  /*
	    try {
	        PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
	        return pi.versionCode;
	    } catch (NameNotFoundException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return 0;
	    }  */
    }
    //获得map的token
    public String getmapToken()
    {
        return getResources().getString(R.string.accessToken);
    }
    void setGpsOption()
    {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
       // mLocationClient.setLocOption(option);
    }
    //获得硬件id
    public  String getDeviceId()
    {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

}
