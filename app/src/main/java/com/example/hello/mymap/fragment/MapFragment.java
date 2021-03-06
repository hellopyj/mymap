package com.example.hello.mymap.fragment;

import android.animation.TypeEvaluator;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.example.hello.mymap.MyApplication;
import com.example.hello.mymap.R;
import com.example.hello.mymap.activity.ImageGridActivity;
import com.example.hello.mymap.activity.MainActivity;
import com.example.hello.mymap.activity.TestActivity;
import com.example.hello.mymap.ar.ArActivity;
import com.example.hello.mymap.ar.ArCreatActivity;
import com.example.hello.mymap.map.acyivity.BigPicActivity;
import com.example.hello.mymap.map.acyivity.MapEditActivity;
import com.example.hello.mymap.map.acyivity.PLVideoViewActivity;
import com.example.hello.mymap.map.adapter.MyMarkerAdapter;
import com.example.hello.mymap.map.markers.MyBaseMarker;
import com.example.hello.mymap.map.markers.MyMarkerOption;
import com.example.hello.mymap.map.modle.MyMessage;
import com.example.hello.mymap.map.modle.MyPolyLine;
import com.example.hello.mymap.map.others.MarkerVoiceClick;
import com.example.hello.mymap.map.others.onMarkerClick;
import com.example.hello.mymap.map.widget.MapInptBar;
import com.example.hello.mymap.map.widget.MapPopuLineWindow;
import com.example.hello.mymap.map.widget.MapPopuWindow;
import com.example.hello.mymap.tools.FileDownloadCallback;
import com.example.hello.mymap.tools.FileDownloadTask;
import com.example.hello.mymap.tools.FileUploadThread;
import com.example.hello.mymap.tools.Geocoder;
import com.example.hello.mymap.tools.MyPolyline;
import com.example.hello.mymap.tools.MyTools;
import com.example.hello.mymap.utils.Config;
import com.google.vr.sdk.widgets.common.VrWidgetRenderer;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.hyphenate.util.PathUtil;
import com.lzp.floatingactionbuttonplus.FabTagLayout;
import com.lzp.floatingactionbuttonplus.FloatingActionButtonPlus;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.utils.AsyncRun;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Lovepyj on 2016/5/8.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    protected static final int REQUEST_CODE_VIDEO=4;
    protected static final int REQUEST_CODE_AR=5;
    boolean isMenuShow=false;
    private ProgressDialog pd;
    MapView mMapView;
    boolean canTouch=true;
    //FloatingActionButtonPlus fbPlus;
    int currEditMod=-1;
    int pointTmpMarktype;
    Marker pointTmpMark=null;

    //int lineTmpMarkIndex=0;
    //Marker lineTmpMark[]=new Marker[2];
    LinkedList<Marker> lineTmpMark=new LinkedList<>();

    MapPopuWindow mapPopuWindow;
    MyMessage myMessage=new MyMessage();
    MapboxMap mMapBoxMap;
    File cameraFile;
    //MapboxMap.OnMapLongClickListener mapLongClickListener;
    //MapboxMap.OnInfoWindowCloseListener onInfoWindowCloseListener;
    public FloatingActionButtonPlus.OnItemClickListener fbonItemClickListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // setok();
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_map,container,false);
        //fbPlus=(FloatingActionButtonPlus)view.findViewById(R.id.fb_plus);
        //fbPlus.hideFab();
       // fbPlus.setVisibility(View.INVISIBLE);
        setFbClick();
        ((MainActivity)getActivity()).fbPlus.setOnItemClickListener(fbonItemClickListener);

        pd=new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("正在上传请稍候...");
        mMapView= (MapView) view.findViewById(R.id.mapview);
        //mMapView.setAccessToken(MyApplication.getInstance().getmapToken());
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        //setGps();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //fbPlus.showFab();
       // fbPlus.setVisibility(View.VISIBLE);
        mMapView.onResume();
       // fbPlus.hideFab();
       // fbPlus.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        // Set map style
        mMapBoxMap=mapboxMap;
        setMkEvent();
//        mMapBoxMap.setMyLocationEnabled(true);
        mapboxMap.setStyleUrl(Style.MAPBOX_STREETS);
        // Set the camera's starting position
        CameraPosition cameraPosition = new CameraPosition.Builder()
        //26.037969,119.311926
                .target(new LatLng(26.03659,119.30299)) // set the camera's center position
                .zoom(17)  // set the camera's zoom level
                .tilt(0)  // set the camera's tilt
                .build();
//      HashMap<String,String> a=new HashMap<>();
//        HashMap<String,String> b=new HashMap<>();
//        a.put("picurl","http://o7zehvmb8.bkt.clouddn.com/18750790760_1465313588720seyz.jpg");
//        a.put("txt","nihao");
//        b.put("txt","wohao");
//        b.put("picurl","http://o7zehvmb8.bkt.clouddn.com/18750790760_1465313606418knnl.png");
//        mMapBoxMap.addMarker(new MyMarkerOption(new MyMessage(2,26.036636593303,119.30290416933542,a,"123")));
//        mMapBoxMap.addMarker(new MyMarkerOption(new MyMessage(2,26.036161137766328,119.30270555887307,b,"123")));
//        mapboxMap.addMarker(new MarkerOptions()
//                .position(new LatLng(26.03659,119.30299))
//                .title("Sydney Opera House")
//                .snippet("Bennelong Point, Sydney NSW 2000, Australia"));
//        try {
//            mapboxMap.setMyLocationEnabled(true);
//            mapboxMap.setOnMyLocationChangeListener(new MapboxMap.OnMyLocationChangeListener() {
//                @Override
//                public void onMyLocationChange(@Nullable Location location) {
//                    Toast.makeText(getActivity(),
//                            ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } catch (SecurityException e) {
//            //should not occur, permission was checked in FeatureOverviewActivity
//            Toast.makeText(getActivity(),
//                    "Location permission is not available", Toast.LENGTH_SHORT).show();
//        }
       // mapboxMap.addMarker(new MyMarkerOption("nihao",new LatLng(26.03659,119.30299),null,0,"nihaosijie"));
        //Log.v("sasa",mMapBoxMap.getMyLocation().getLatitude()+"");
        // Move the camera to that position
        MyMarkerAdapter myMarkerAdapter=new MyMarkerAdapter();
        myMarkerAdapter.setOnMarkerClick(new myOnMapClick());
        mapboxMap.setInfoWindowAdapter(myMarkerAdapter);
        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    //路线长按事件
    void setMkLongPloyLine()
    {
        lineTmpMark=new LinkedList<>();
        myMessage.type= MyMarkerAdapter.LINE_START;
        mMapBoxMap.setOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                if (pointTmpMark == null) {
                    // Remove previous added marker
//                    mMapBoxMap.removeAnnotation(pointTmpMark );
//                    pointTmpMark  = null;
                    pointTmpMark = mMapBoxMap.addMarker(new MyMarkerOption(new MyMessage(MyMarkerAdapter.MARKER_ADD, latLng.getLatitude(), latLng.getLongitude(), null, null)));
                    mMapBoxMap.selectMarker(pointTmpMark);
                } else {
                    pointTmpMark.setPosition(latLng);
                }
            }
        });

    }
    //点操作长按事件
    void setMkLongPoint()
    {
        //myMessage.type= marktype;
        //地图长按事件
        if(pointTmpMark!=null){
            mMapBoxMap.removeMarker(pointTmpMark);
            pointTmpMark=null;
        }
        mMapBoxMap.setOnMapLongClickListener(
        new MapboxMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                if (pointTmpMark == null) {
                    // Remove previous added marker
//                    mMapBoxMap.removeAnnotation(pointTmpMark );
//                    pointTmpMark  = null;
                    pointTmpMark = mMapBoxMap.addMarker(new MyMarkerOption(new MyMessage(pointTmpMarktype, latLng.getLatitude(), latLng.getLongitude(), null, null)));
                    mMapBoxMap.selectMarker(pointTmpMark);
                } else {
                    pointTmpMark.setPosition(latLng);
                }
            }
        });
                //infowindow关闭事件
          mMapBoxMap.setOnInfoWindowCloseListener(new MapboxMap.OnInfoWindowCloseListener() {
                    @Override
                    public void onInfoWindowClose(Marker marker) {
                        if (marker instanceof MyBaseMarker)
                        {
                            if(((MyBaseMarker) marker).getMessage()!=null) {
                                //添加状态
                                int type = ((MyBaseMarker) marker).getMessage().type;
                                if (type == MyMarkerAdapter.MARKER_ADD || type == MyMarkerAdapter.LINE_START || type == MyMarkerAdapter.GON_STRAT) {
                                    mMapBoxMap.removeMarker(marker);
                                    pointTmpMark = null;
                                }
                            }
                        }
                        //Log.v("ss","close");
                    }
                });
    }
    void setMkEvent()
    {
        mMapBoxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
               // Log.v("nihaosijie","shakskamdhjkgdsjhsggj");
                canTouch=false;
                return false;
            }
        });
        //地图点击事件
        mMapBoxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                if(canTouch) {
                    Geocoder geocoder = new Geocoder(point, 18);
                    geocoder.placeInfo();
                }
                else
                {
                    canTouch=true;
                }

            }
        });

//                else {
//                    ValueAnimator markerAnimator = ValueAnimator.ofObject(new LatLngEvaluator(), (Object[]) new LatLng[]{pointTmpMark.getPosition(), latLng});
//                    markerAnimator.setDuration(1000);
//                    //markerAnimator.setRepeatCount(ValueAnimator.RESTART);
//                    //markerAnimator.setRepeatMode(ValueAnimator.INFINITE);
//                    markerAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//                    markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator animation) {
//                            if (pointTmpMark!= null) {
//                                pointTmpMark.setPosition((LatLng) animation.getAnimatedValue());
//                            }
//                        }
//                    });
//                    markerAnimator.start();
//                }

                // Add marker on long click location with default marker image

    }
    void setFbClick()
    {
        fbonItemClickListener= new FloatingActionButtonPlus.OnItemClickListener() {
            @Override
            public void onItemClick(FabTagLayout tagView, int position) {
                if(position==4&&currEditMod!=4) {
                    removeListMark(lineTmpMark);
                    pointTmpMarktype=MyMarkerAdapter.MARKER_ADD;
                    setMkLongPoint();
                    currEditMod=4;
                }
                if(position==3&&currEditMod!=3)
                {
                    //Log.v("ddd","dsds");
                    MapPopuLineWindow mapPopuLineWindow=new MapPopuLineWindow(getActivity());
                    mapPopuLineWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            currEditMod=-1;
                        }
                    });
                    mapPopuLineWindow.showAtLocation(getActivity().findViewById(R.id.rl_main), Gravity.BOTTOM, 0, 0);
                    mapPopuLineWindow.setPolyLineClick(new MapPopuLineWindow.PolyLineClick() {
                        @Override
                        public void onPolyLineClick(int tag) {
                            switch (tag)
                            {
                                case 1:{
                                    removeListMark(lineTmpMark);
                                    pointTmpMarktype=MyMarkerAdapter.LINE_START;
                                    setMkLongPoint();
                                    Log.v("ployline","ployline");
                                    break;
                                }
                            }
                        }
                    });
                    currEditMod=3;
                }
                if(position==2&&currEditMod!=2) {
                    removeListMark(lineTmpMark);
                    setMkLongPoint();
                    setok();
                    pointTmpMarktype=MyMarkerAdapter.GON_STRAT;
                    currEditMod=2;
                }
                if(position==1&&currEditMod!=1) {
                    startActivity(new Intent(getActivity(), MapEditActivity.class));
                    currEditMod=2;
                }
                if(position==0&&currEditMod!=0) {
                    startActivity(new Intent(getActivity(), TestActivity.class));
                    currEditMod=0;
                }
            }
        };
    }
    void setGps()
    {
//        MyApplication.mLocationClient.registerLocationListener(new BDLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation bdLocation) {
//                Log.v("pyj",bdLocation.getLatitude()+","+bdLocation.getLongitude());
//                if(null!=mMapBoxMap)
//           {
//               mMapBoxMap.moveCamera(CameraUpdateFactory.newCameraPosition((new CameraPosition.Builder()
//                       .target(new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude())) // set the camera's center position
//                       .zoom(17)  // set the camera's zoom level
//                       .tilt(0)  // set the camera's tilt
//                       .build())));
//           }
//            }
//        });
//        MyApplication.mLocationClient.start();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
               // myMessage=new MyMessage(MyMarkerAdapter.MARKER_PCICTURE,pointTmpMark.getPosition())
                myMessage.type=MyMarkerAdapter.MARKER_PCICTURE;
                myMessage.content=new HashMap<>();
                myMessage.content.put("picurl",cameraFile.getAbsolutePath());

            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                myMessage.type=MyMarkerAdapter.MARKER_PCICTURE;
                myMessage.content=new HashMap<>();
                myMessage.content.put("picurl", MyTools.getRealFilePath(getActivity(),data.getData()));
                //sendMyMessage.senPicMessage();

            } else if (requestCode == REQUEST_CODE_AR) { // AR
                myMessage.type=MyMarkerAdapter.MARKER_AR;
                myMessage.content=new HashMap<>();
                myMessage.content.put("picurl",data.getStringExtra("picurl"));
                myMessage.content.put("videourl",data.getStringExtra("videourl"));
                //Log.v("video",data.getStringExtra("picurl"));
            }
            else if(requestCode ==REQUEST_CODE_VIDEO)
            {
                myMessage.type=MyMarkerAdapter.MARKER_VIDEO;
                myMessage.content=new HashMap<>();
                myMessage.content.put("videourl", data.getStringExtra("path"));
            }

            if(mapPopuWindow!=null)
                mapPopuWindow.inptBar.cancle.setVisibility(View.VISIBLE);
        }
    }
    private class LatLngEvaluator implements TypeEvaluator<LatLng> {
        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            return new LatLng(startValue.getLatitude() + (endValue.getLatitude() - startValue.getLatitude()) * fraction,
                    startValue.getLongitude() + (endValue.getLongitude() - startValue.getLongitude()) * fraction);
        }
    }
    //底部输入栏设置(回收出项问题，所以每次都new一个，待解决)
    void setPopuWindow() {
        myMessage.type=MyMarkerAdapter.MARKER_TXT;
        myMessage.content=new HashMap<>();
            mapPopuWindow = new MapPopuWindow(getActivity());
            mapPopuWindow.showAtLocation(getActivity().findViewById(R.id.rl_main), Gravity.BOTTOM, 0, 0);
            mapPopuWindow.inptBar.setBottomLister(new MapInptBar.BottomClickLister() {
            @Override
            public void extentButton(int tag) {
                switch (tag)
                {
                    case 1:
                        Intent intentar = new Intent(getActivity(), ArCreatActivity.class);
                        startActivityForResult(intentar, REQUEST_CODE_AR);
                        break;
                    case 2:
                        selectPicFromCamera();
                        break;
                    case 3:
                        selectPicFromLocal();
                        break;
                    case 4:
                        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_VIDEO);
                        break;
                }

            }

            @Override
            public void cancleButton() {

                myMessage.type=MyMarkerAdapter.MARKER_TXT;
                myMessage.content.clear();

            }
        });
            mapPopuWindow.inptBar.setChatInputMenuListener(new MapInptBar.ChatInputMenuListener() {
                @Override
                public void onSendMessage(String content) {
                    //mMapBoxMap.addMarker(new MyMarkerOption().type());
                    LatLng latLng=pointTmpMark.getPosition();
                    //myMessage=new MyMessage(chattype,latLng.getLatitude(),latLng.getLongitude(),null);
                    myMessage.latitude=latLng.getLatitude();
                    myMessage.longitude=latLng.getLongitude();
                   // Log.v("nihao",content);
                    //Log.v("question",mapPopuWindow.inptBar.isQuestion?"true":"false");
                    if(mapPopuWindow.inptBar.isQuestion) {
                        myMessage.content.put("qtxt", content);
                    }
                    else {
                        myMessage.content.put("txt", content);
                    }
                    sendMyMessage();
                   // sendMyMessage.senPicMessage("/storage/emulated/0/Pictures/Screenshots/Screenshot_2016-05-08-01-07-04.png");
                }

                @Override
                public void onBigExpressionClicked(EaseEmojicon emojicon) {

                }

                @Override
                public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {

                    return mapPopuWindow.inptBar.voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {

                        @Override
                        public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                           // Log.v("test",voiceFilePath);
                            myMessage.type=MyMarkerAdapter.MARKER_VOICE;
                            myMessage.content.clear();
                            myMessage.content.put("voiceurl",voiceFilePath);
                            mapPopuWindow.inptBar.cancle.setVisibility(View.VISIBLE);
                            // 发送语音消息
                            // sendVoiceMessage(voiceFilePath, voiceTimeLength);
                        }
                    });
                }
            });

//        else {
//            mapPopuWindow.showAtLocation(getActivity().findViewById(R.id.rl_main), Gravity.BOTTOM, 0, 0);
//        }
    }
    //marker添加点击事件
 public class myOnMapClick extends onMarkerClick
    {

        @Override
        public void markAdd(MyBaseMarker marker) {
            AsyncRun.run(new Runnable() {
                @Override
                public void run() {
                    setPopuWindow();
                }
            });

        }

        @Override
        public void markQuestion(MyBaseMarker marker) {

        }

        @Override
        public void markVideo(MyBaseMarker marker) {
           // startActivity(new Intent(getActivity(), ArActivity.class));
            Intent intent=new Intent(getActivity(), PLVideoViewActivity.class);
            intent.putExtra("videoPath", marker.getMessage().content.get("videourl"));
            intent.putExtra("mediaCodec", 0);
            startActivity(intent);
        }

        @Override
        public void markPic(MyBaseMarker marker) {
            Intent intent = new Intent(getActivity(), BigPicActivity.class);
           // ImagePipeline imagePipeline = Fresco.getImagePipeline();
           // boolean inMemoryCache = imagePipeline.isInBitmapMemoryCache(Uri.parse(marker.message.content.get("url")));

//            File file = new File(imgBody.getLocalUrl());
//            if (file.exists()) {
//                Uri uri = Uri.fromFile(file);
//                intent.putExtra("uri", uri);
//            } else {
//                // The local full size pic does not exist yet.
//                // ShowBigImage needs to download it from the server
//                // first
//                intent.putExtra("secret", imgBody.getSecret());
//                intent.putExtra("remotepath", imgBody.getRemoteUrl());
//                intent.putExtra("localUrl", imgBody.getLocalUrl());
//            }
            intent.putExtra("url", marker.message.content.get("picurl")+Config.QINIU_SUOFAN);
          //  intent.putExtra("uri", uri);
            startActivity(intent);

        }

        @Override
        public void markAr(final MyBaseMarker marker) {
            //Log.v("string",MyTools.getArImgFile(marker.message.content.get("picurl")));
            final String picurl=marker.message.content.get("picurl");
            final String arpicurl=PathUtil.getInstance().getImagePath()+"/"+MyTools.urlGetfileName(picurl);
            Log.v("ss",arpicurl);
           // final String suffix=MyTools.getFileSuffix(picurl);
            FileDownloadCallback fileDownloadCallback=new FileDownloadCallback(){
                @Override
                public void onDone() {
                    super.onDone();
                    Intent intent=new Intent(getActivity(),ArActivity.class);
                    intent.putExtra("picurl",arpicurl);
                    intent.putExtra("videourl",marker.message.content.get("videourl"));
                    startActivity(intent);

                }
            };
            new FileDownloadTask(picurl,arpicurl,fileDownloadCallback,false).execute();
        }

        @Override
        public void markVoice(final MyBaseMarker marker) {
            String voiceurl=marker.message.content.get("voiceurl");
            final String localurl=PathUtil.getInstance().getVoicePath()+"/"+MyTools.urlGetfileName(voiceurl);
            if(new File(localurl).exists()) {
                MarkerVoiceClick.getInstance().playVoice(localurl);
            }
            else
            {
                FileDownloadCallback fileDownloadCallback=new FileDownloadCallback(){
                    @Override
                    public void onDone() {
                        super.onDone();
                        MarkerVoiceClick.getInstance().playVoice(localurl);

                    }
                };
                new FileDownloadTask(voiceurl,localurl,fileDownloadCallback,false).execute();
            }
        }

        @Override
        public void markLineStart(MyBaseMarker marker) {

            mMapBoxMap.removeMarker(pointTmpMark);
            lineTmpMark.clear();
            LatLng latLng=marker.getPosition();
            lineTmpMark.add(mMapBoxMap.addMarker(new MarkerOptions().position(latLng).title("起点")));
            //lineTmpMark.add(marker);
           // Log.v("sas","hh");
            //lineTmpMark.add(pointTmpMark);
            pointTmpMark=null;
            pointTmpMarktype=MyMarkerAdapter.LINE_END;
            canTouch=true;
        }

        @Override
        public void markLineEnd(MyBaseMarker marker) {
            //Log.v("hh","end");
            lineTmpMark.add(marker);
           // removeListMark(lineTmpMark);
            MyPolyline myPolyline=new MyPolyline(lineTmpMark.get(0).getPosition(),marker.getPosition());
            myPolyline.setLinecomplete(new MyPolyline.LineComplete() {
                @Override
                public void complete(final MyMessage myMessage) {
                    //removeListMark(lineTmpMark);
                   // Log.v("ssa","sssss");
                    AsyncRun.run(new Runnable() {
                        @Override
                        public void run() {
                            removeListMark(lineTmpMark);
                            pointTmpMarktype=MyMarkerAdapter.LINE_START;
                            addNewPolyLine(new MyMarkerOption(myMessage));
                            canTouch=true;
                        }
                    });
                    //addNewPolyLine(new MyMarkerOption(myMessage));

                }

            });
            myPolyline.lineInfo();
        }

        @Override
        public void markGonStrat(MyBaseMarker marker, int i) {
            if(i==0)
            {
                mMapBoxMap.removeMarker(marker);
               // lineTmpMark.remove(marker);
            }
            else
            {
                mMapBoxMap.removeMarker(pointTmpMark);
               // lineTmpMark.clear();
                LatLng latLng=marker.getPosition();
                lineTmpMark.add(mMapBoxMap.addMarker(new MarkerOptions().position(latLng).title("起点")));
                //lineTmpMark.add(marker);
                // Log.v("sas","hh");
                //lineTmpMark.add(pointTmpMark);
                pointTmpMark=null;
                //setok();
               // Log.v("size",lineTmpMark.size()+"");
                if(lineTmpMark.size()==3)
                {
                    isMenuShow=true;
                    shownok();

                }
                else {
                    if (lineTmpMark.size()<3)
                    {
                        unshownok();
                    }
                }
            }
        }
    }
    /**
     * 照相获取图片
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isExitsSdcard()) {
            Toast.makeText(getActivity(), com.hyphenate.easeui.R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

         cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * 从图库获取图片
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }
    //照片选项
    void tipItem()
    {
        String [] tip=new String[]{"拍照","图库"};
        AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).setItems(tip, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0)
                {
                    selectPicFromCamera();
                }
               else if(i==1)
                {
                    selectPicFromLocal();
                }

            }
        }).create();
        alertDialog.show();
    }
//发送消息到服务器
    void sendMyMessage()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AsyncRun.run(new Runnable() {
                    @Override
                    public void run() {
                        pd.show();
                    }
                });
               upfiles();
                //new FileUploadThread()
                try {
                    RequestBody requestBody= new FormBody.Builder().add("type",myMessage.type+"")
                            .add("latitude",myMessage.latitude+"")
                            .add("longitude",myMessage.longitude+"")
                            .add("username",EMClient.getInstance().getCurrentUser())
                            .add("content",LoganSquare.serialize(myMessage.content)).build();
                    Request request=new Request.Builder().url(Config.ADD_MARKER_URL).post(requestBody).build();
                    MyApplication.httpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                           // Log.v("json",content);
                            Log.v("return",response.body().string());

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();

    }
    //上传文件
    void upfiles()
    {
        UpCompletionHandler upCompletionHandler=new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                AsyncRun.run(new Runnable() {
                    @Override
                    public void run() {
                        addmark();
                        //pd.dismiss();
                    }
                });
            }
        };
        switch (myMessage.type)
        {
            case MyMarkerAdapter.MARKER_VOICE:
                upmethord("voiceurl",upCompletionHandler);
                break;
            case MyMarkerAdapter.MARKER_VIDEO:
                upmethord("videourl",upCompletionHandler);
                break;
            case MyMarkerAdapter.MARKER_PCICTURE:
                upmethord("picurl",upCompletionHandler);
                break;
            case MyMarkerAdapter.MARKER_AR:
                upmethord("picurl",null);
                upmethord("videourl",upCompletionHandler);
                break;
            case  MyMarkerAdapter.MARKER_TXT:
                AsyncRun.run(new Runnable() {
                    @Override
                    public void run() {
                         addmark();
                        pd.dismiss();
                    }
                });
               // addNewMark(new MyMarkerOption(myMessage));
               // mMapBoxMap.removeMarker(pointTmpMark);
               // mapPopuWindow.dismiss();
                break;
        }
    }

    //通过键值对上传
    void upmethord(String key, UpCompletionHandler upCompletionHandler)
    {
//        UploadOptions uploadOptions=new UploadOptions(null, null, false,
//                new UpProgressHandler() {
//                    @Override
//                    public void progress(String key, final double percent) {
//                        AsyncRun.run(new Runnable() {
//                            @Override
//                            public void run() {
//                                pd.setMessage("已上传:"+(int)(percent*100)+"%");
//                            }
//                        });
//                    }
//                }, new UpCancellationSignal() {
//
//            @Override
//            public boolean isCancelled() {
//                return false;
//            }
//        });
        String localname=myMessage.content.get(key);
        String newname=MyTools.getRandomFileName(localname);
        new FileUploadThread(localname,newname,null,upCompletionHandler).start();
        myMessage.content.put(key,Config.QINIU_BASE+newname);
    }
    //添加mark;
    void addmark()
    {
        pd.dismiss();
        addNewMark(new MyMarkerOption(myMessage));
        mMapBoxMap.removeMarker(pointTmpMark);
        mapPopuWindow.dismiss();
    }
    //添加marker
    public void addNewMark(MyMarkerOption  myMarkerOption)
    {
        mMapBoxMap.addMarker(myMarkerOption);
    }
    //添加polyline
    public  void addNewPolyLine(MyMarkerOption  myMarkerOption)
    {
       MyBaseMarker tmp= (MyBaseMarker) mMapBoxMap.addMarker(myMarkerOption);
        String shape=tmp.message.content.get("shape");
        tmp.brother=new ArrayList<>();
        LatLng tolat=new LatLng(Double.parseDouble(tmp.message.content.get("tolatitude")),Double.parseDouble(tmp.message.content.get("tolongitude")));
        //double tolan=Double.parseDouble(tmp.message.content.get("tolatitude"));
        //double tolon=Double.parseDouble(tmp.message.content.get("tolongitude"));
       // Log.v("ss",tolan+";"+tolon);
        tmp.brother.add(mMapBoxMap.addPolyline(new PolylineOptions().addAll(MyTools.decode(shape,tmp.getPosition(),tolat))
                .color(Color.parseColor("#3887be"))
                .width(5)
        ));
        //待修改
        tmp.brother.add(mMapBoxMap.addMarker(new MyMarkerOption().position(tolat)));
    }
    //移除polyline
    public void removePolyLine(MyBaseMarker myBaseMarker){
        mMapBoxMap.removePolyline((Polyline)myBaseMarker.brother.get(0));
        mMapBoxMap.removeMarker(myBaseMarker);
    }
    //添加polygon
    public void addNewPolyGon()
    {
        if(lineTmpMark.size()>0)
        {
            List<LatLng> latLngs=new ArrayList<LatLng>();
            for(Marker marker:lineTmpMark){
                latLngs.add(marker.getPosition());
            }
            MyBaseMarker tmp= (MyBaseMarker) mMapBoxMap.addMarker(new MyMarkerOption().position(latLngs.get(0)));
            tmp.brother=new ArrayList<>();
            int color=Color.parseColor("#ffffff");
            tmp.brother.add(mMapBoxMap.addPolygon(new PolygonOptions()
                    .addAll(latLngs)
                    .fillColor(color).strokeColor(color)));
        }
    }
    //移除listmark
    public  void removeListMark(LinkedList<Marker> myBaseMarkers)
    {
        for(Marker key:myBaseMarkers){
            mMapBoxMap.removeMarker(key);
        }
        myBaseMarkers.clear();
        if(pointTmpMark!=null) {
            mMapBoxMap.removeMarker(pointTmpMark);
            pointTmpMark = null;
        }
    }
    //显示ok按钮
    public void setok()
    {
        if(!isMenuShow){
            ((MainActivity)getActivity()).mysumenu.add(2,1,1,"完成").setIcon(R.drawable.ic_done_black_24dp).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    addNewPolyGon();
                    removeListMark(lineTmpMark);
                    unshownok();
                    canTouch=true;
                    isMenuShow=false;
                    return false;
                }
            });
            ((MainActivity)getActivity()).mysumenu.add(2,1,1,"取消").setIcon(R.drawable.ic_close_black_24dp).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    removeListMark(lineTmpMark);
                    unshownok();
                    isMenuShow=false;
                    return false;
                }
            });
            isMenuShow=true;
        }
       // ((MainActivity)getActivity()).mymenu.removeGroup(200);
    }
    public void shownok()
    {
         if(isMenuShow)
             ((MainActivity)getActivity()).mysumenu.setGroupVisible(2,true);
    }
    public void unshownok()
    {
        if(isMenuShow) {
            ((MainActivity) getActivity()).mysumenu.setGroupVisible(2, false);
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        //uLog.v("ddd",hidden?"true":"false");
        if(hidden) {
           unshownok();
          //  isMenuShow=false;

        }
        else {
            shownok();
        }
       // Log.v("sasa","hksj");
        super.onHiddenChanged(hidden);
    }

}
