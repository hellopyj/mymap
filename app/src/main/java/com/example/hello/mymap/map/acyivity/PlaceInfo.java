package com.example.hello.mymap.map.acyivity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.example.hello.mymap.Constant;
import com.example.hello.mymap.MyApplication;
import com.example.hello.mymap.R;
import com.example.hello.mymap.activity.ChatActivity;
import com.example.hello.mymap.model.ReturnJs;
import com.example.hello.mymap.utils.Config;

import java.io.Serializable;
import java.util.HashMap;

import okhttp3.Request;
import okhttp3.Response;

public class PlaceInfo extends AppCompatActivity {
    TextView tv_title;
    TextView tv_admin;
    TextView tv_group;
    TextView tv_name;
    TextView tv_address;
    TextView tv_tip;
    TextView tv_point;
    LinearLayout ll_bt;
    Button enter;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_admin= (TextView) findViewById(R.id.tv_admin);
        tv_address= (TextView) findViewById(R.id.tv_address);
        tv_tip= (TextView) findViewById(R.id.tv_tip);
        tv_point= (TextView) findViewById(R.id.tv_point);
        tv_group= (TextView) findViewById(R.id.tv_group);
        ll_bt= (LinearLayout) findViewById(R.id.ll_bt);
        enter= (Button) findViewById(R.id.bt_enter);
        floatingActionButton= (FloatingActionButton) findViewById(R.id.fa_send);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!tv_group.getText().toString().equals("暂无")) {
                    startActivity(new Intent(PlaceInfo.this, ChatActivity.class).putExtra("userId", tv_group.getText())
                            .putExtra("chatType", Constant.CHATTYPE_CHATROOM));
                }
                else {
                    Toast.makeText(PlaceInfo.this,"没有所属群组",Toast.LENGTH_SHORT).show();

                }
            }
        });
        //初始化
        setThings();
    }
    void setThings()
    {
        Bundle bundle=getIntent().getExtras();
        HashMap<String,String> data=(HashMap<String, String>) bundle.getSerializable("content");
        String osm=data.get("osm");
        String username=data.get("username");
        String groupid=data.get("group_id");
        String tip=data.get("tip");
        String placename=data.get("place_name");
        final String placeid=data.get("place_id");
        boolean haschild;
        haschild=("0".equals(data.get("haschild"))||data.get("haschild")==null)?false:true;
        if("".equals(placename)||placename==null) {
            placename=osm.split(",")[0];
        }
        if(TextUtils.isEmpty(groupid)){
            groupid="暂无";
        }
        if(TextUtils.isEmpty(username))
        {
            username="暂无";
        }
        if(TextUtils.isEmpty(tip))
        {
            tip="没有介绍哦";
        }
        tv_title.setText(placename);
        tv_group.setText(groupid);
        tv_admin.setText(username);
        tv_address.setText(osm);
        tv_tip.setText(tip);
        tv_name.setText(placename);
        tv_point.setText(data.get("lat")+","+data.get("lon"));
        if(haschild)
        {
            final String name=placename;
            ll_bt.setVisibility(View.VISIBLE);
            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getchild(placeid,name);
                }
            });
        }

        Log.v("yyyy",data.toString());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //跳转判定
    void getchild(final String placeid, final String placename)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url=String.format(Config.GETCHILD,placeid);
                Log.v("url",url);
                final Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response response = MyApplication.getInstance().httpClient.newCall(request).execute();
                    String json = response.body().string();
                    ReturnJs returnJs= LoganSquare.parse(json,ReturnJs.class);
                     Log.v("sss",json);
                    if(returnJs.code==1)
                    {
                        if("1".equals(returnJs.content.get("type")))
                        {
                            Intent intent=new Intent(PlaceInfo.this, Childinfo.class);
                            intent.putExtra("url",String.format(Config.GETCHILD_HTML,placeid));
                            intent.putExtra("title",placename);
                            startActivity(intent);
                        }
                    }
                    // Log.v("dd",geocoderBean.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void setvr(View view){
        startActivity(new Intent(PlaceInfo.this,VrActivity.class));
    }
}
