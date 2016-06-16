package com.example.hello.mymap.map.acyivity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.mymap.Constant;
import com.example.hello.mymap.R;
import com.example.hello.mymap.activity.ChatActivity;

import java.io.Serializable;
import java.util.HashMap;

public class PlaceInfo extends AppCompatActivity {
    TextView tv_title;
    TextView tv_admin;
    TextView tv_group;
    TextView tv_name;
    TextView tv_address;
    TextView tv_tip;
    TextView tv_point;
    LinearLayout ll_bt;
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
        boolean haschild;
        haschild="0".equals(data.get("haschild"))?false:true;
        if("".equals(placename)) {
            placename=osm.split(",")[0];
        }
        if("".equals(groupid)){
            groupid="暂无";
        }
        if("".equals(username))
        {
            username="暂无";
        }
        if("".equals(tip))
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
            ll_bt.setVisibility(View.VISIBLE);
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
}
