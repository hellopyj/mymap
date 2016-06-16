package com.example.hello.mymap.map.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hello.mymap.MyApplication;
import com.example.hello.mymap.R;
import com.example.hello.mymap.map.markers.MyBaseMarker;
import com.example.hello.mymap.map.others.onMarkerClick;
import com.example.hello.mymap.utils.Config;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Created by Lovepyj on 2016/5/25.
 */
public class MyMarkerAdapter implements MapboxMap.InfoWindowAdapter {

    onMarkerClick partent = null;
    public final static int MARKER_TXT=1;
   // public final static int MARKER_QUESTION=2;
    public final static int MARKER_PCICTURE=2;
    public final static int MARKER_VOICE=3;
    public final static int MARKER_VIDEO=4;
    public final static int MARKER_AR=5;
    public final static int MARKER_ADD=0;

    public void setOnMarkerClick(onMarkerClick parent)
    {
        this.partent=parent;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        View view=null;
        if(marker instanceof MyBaseMarker)
        {
            MyBaseMarker myMarker= (MyBaseMarker) marker;
            int type=myMarker.getMessage().type;
            switch (type)
            {
                case MARKER_ADD:
                {
                    view= LayoutInflater.from(MyApplication.mainContext).inflate(R.layout.marker_add, null);
                    Button button= (Button) view.findViewById(R.id.bt_add);
                    button.setTag(myMarker);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            partent.markAdd((MyBaseMarker) view.getTag());
                        }
                    });
                    break;
                }
                case MARKER_TXT:
                {
                    view= LayoutInflater.from(MyApplication.mainContext).inflate(R.layout.marker_txt, null);
                    TextView textView= (TextView) view.findViewById(R.id.tv_content);
                    if(isquestion(myMarker))
                    {
                        LinearLayout ll= (LinearLayout) view.findViewById(R.id.ll_question);
                        ll.setVisibility(View.VISIBLE);
                        textView.setText(myMarker.getMessage().content.get("qtxt"));

                    }
                    else {
                        textView.setText(myMarker.getMessage().content.get("txt"));
                    }
                    break;
                }
//                case MARKER_QUESTION:
//                {
//                    view= LayoutInflater.from(MyApplication.mainContext).inflate(R.layout.marker_question, null);
//                    TextView textView= (TextView) view.findViewById(R.id.tv_content);
//                    textView.setText(((MyBaseMarker) marker).getMessage().content.get("question"));
//                    break;
//                }
                case MARKER_PCICTURE:
                {
                    view= LayoutInflater.from(MyApplication.mainContext).inflate(R.layout.marker_video, null);
                    SimpleDraweeView simpleDraweeView= (SimpleDraweeView) view.findViewById(R.id.sd_view);
                    simpleDraweeView.setImageURI(Uri.parse(myMarker.message.content.get("picurl")+Config.QINIU_SUOFAN));
                    simpleDraweeView.setTag(myMarker);
                    simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            partent.markPic((MyBaseMarker) view.getTag());
                        }
                    });
                    TextView textView= (TextView) view.findViewById(R.id.tv_content);
                    if(isquestion(myMarker))
                    {
                        LinearLayout ll= (LinearLayout) view.findViewById(R.id.ll_question);
                        ll.setVisibility(View.VISIBLE);
                        textView.setText(myMarker.getMessage().content.get("qtxt"));

                    }
                    else {
                        textView.setText(myMarker.getMessage().content.get("txt"));
                    }
                    break;
                }
                case MARKER_VOICE:
                {
                    view= LayoutInflater.from(MyApplication.mainContext).inflate(R.layout.marker_voice, null);
                    Button button= (Button) view.findViewById(R.id.bt_voice);
                    button.setTag(marker);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            partent.markVoice((MyBaseMarker)view.getTag());
                        }
                    });
                    TextView textView= (TextView) view.findViewById(R.id.tv_content);
                    if(isquestion(myMarker))
                    {
                        LinearLayout ll= (LinearLayout) view.findViewById(R.id.ll_question);
                        ll.setVisibility(View.VISIBLE);
                        textView.setText(myMarker.getMessage().content.get("qtxt"));

                    }
                    else {
                        textView.setText(myMarker.getMessage().content.get("txt"));
                    }
                    break;
                }
                case  MARKER_VIDEO:
                {
                    view= LayoutInflater.from(MyApplication.mainContext).inflate(R.layout.marker_video, null);
                    SimpleDraweeView simpleDraweeView= (SimpleDraweeView) view.findViewById(R.id.sd_view);
                    simpleDraweeView.setImageURI(Uri.parse(myMarker.message.content.get("videourl")+Config.QINIU_VIDEO_Z));
                    simpleDraweeView.setTag(myMarker);
                    simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            partent.markVideo((MyBaseMarker) view.getTag());
                        }
                    });
                    TextView textView= (TextView) view.findViewById(R.id.tv_content);
                    if(isquestion(myMarker))
                    {
                        LinearLayout ll= (LinearLayout) view.findViewById(R.id.ll_question);
                        ll.setVisibility(View.VISIBLE);
                        textView.setText(myMarker.getMessage().content.get("qtxt"));

                    }
                    else {
                        textView.setText(myMarker.getMessage().content.get("txt"));
                    }
                    break;
                }
                case MARKER_AR:
                {
                    view= LayoutInflater.from(MyApplication.mainContext).inflate(R.layout.marker_video, null);
                    SimpleDraweeView simpleDraweeView= (SimpleDraweeView) view.findViewById(R.id.sd_view);
                    simpleDraweeView.setImageURI(Uri.parse(myMarker.message.content.get("picurl")+Config.QINIU_SUOFAN));
                    simpleDraweeView.setTag(myMarker);
                    simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            partent.markAr((MyBaseMarker) view.getTag());
                        }
                    });
                    TextView textView= (TextView) view.findViewById(R.id.tv_content);
                    if(isquestion(myMarker))
                    {
                        LinearLayout ll= (LinearLayout) view.findViewById(R.id.ll_question);
                        ll.setVisibility(View.VISIBLE);
                        textView.setText(myMarker.getMessage().content.get("qtxt"));

                    }
                    else {
                        textView.setText(myMarker.getMessage().content.get("txt"));
                    }
                    break;

                }
            }

        }
        return view;
    }
    boolean isquestion(MyBaseMarker myBaseMarker)
    {
       return myBaseMarker.getMessage().content.containsKey("qtxt");
    }
}
