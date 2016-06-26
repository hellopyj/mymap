package com.example.hello.mymap.map.adapter;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
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
import com.hyphenate.easeui.utils.EaseSmileUtils;
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
    public final static int LINE_START=6;
    public final static int LINE_END=7;
    public final static int LINE=8;
    public final static int GON_STRAT=9;
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
            if(myMarker.getMessage()!=null){
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
                   // TextView textView= (TextView) view.findViewById(R.id.tv_content);
                    setTextView(view,myMarker);
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
                   // TextView textView= (TextView) view.findViewById(R.id.tv_content);
                    setTextView(view,myMarker);
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
                    setTextView(view,myMarker);
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
                    setTextView(view,myMarker);
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
                    setTextView(view,myMarker);
                    break;

                }
                //线段开始
                case  LINE_START:
                {
                    view= LayoutInflater.from(MyApplication.mainContext).inflate(R.layout.marker_line_start, null);
                    Button button= (Button) view.findViewById(R.id.bt_add);
                    button.setTag(myMarker);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            partent.markLineStart((MyBaseMarker) view.getTag());
                        }
                    });
                    break;

                }
                //线段结束
                case  LINE_END:
                {
                    view= LayoutInflater.from(MyApplication.mainContext).inflate(R.layout.marker_line_start, null);
                    Button button= (Button) view.findViewById(R.id.bt_add);
                    button.setTag(myMarker);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            partent.markLineEnd((MyBaseMarker) view.getTag());
                        }
                    });
                    break;

                }
                //线段
                case  LINE:
                {
                    view= LayoutInflater.from(MyApplication.mainContext).inflate(R.layout.marker_picture, null);
                    break;

                }
                //面
                case GON_STRAT:
                {
                    view= LayoutInflater.from(MyApplication.mainContext).inflate(R.layout.marker_gon_start, null);
                    View.OnClickListener onClickListener=new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(view.getId()==R.id.bt_gon_cancle) {
                                partent.markGonStrat((MyBaseMarker) view.getTag(), 0);
                            }
                            else if(view.getId()==R.id.bt_gon_ok) {
                                partent.markGonStrat((MyBaseMarker) view.getTag(), 1);
                            }

                        }
                    };
                    Button cancle=(Button) view.findViewById(R.id.bt_gon_cancle);
                    cancle.setTag(myMarker);
                    Button ok=(Button) view.findViewById(R.id.bt_gon_ok);
                    ok.setTag(myMarker);
                    cancle.setOnClickListener(onClickListener);
                    ok.setOnClickListener(onClickListener);
                    break;
                }
            }

        }
            else{
                view= LayoutInflater.from(MyApplication.mainContext).inflate(R.layout.marker_etc, null);
            }
        }
        return view;
    }
    boolean isquestion(MyBaseMarker myBaseMarker)
    {
       return myBaseMarker.getMessage().content.containsKey("qtxt");
    }
    //设置文字说明
    void setTextView(View view,MyBaseMarker myMarker)
    {
        TextView textView= (TextView) view.findViewById(R.id.tv_content);
        if(isquestion(myMarker))
        {
            LinearLayout ll= (LinearLayout) view.findViewById(R.id.ll_question);
            ll.setVisibility(View.VISIBLE);
            //textView.setText(myMarker.getMessage().content.get("qtxt"));
            Spannable span = EaseSmileUtils.getSmiledText(MyApplication.applicationContext,myMarker.getMessage().content.get("qtxt"));
            // 设置内容
            textView.setText(span, TextView.BufferType.SPANNABLE);

        }
        else {
            Spannable span = EaseSmileUtils.getSmiledText(MyApplication.applicationContext,myMarker.getMessage().content.get("txt"));
            // 设置内容
            textView.setText(span, TextView.BufferType.SPANNABLE);
        }
    }
}
