package com.example.hello.mymap.map.acyivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.hello.mymap.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class BigPicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_pic);
        SimpleDraweeView simpleDraweeView= (SimpleDraweeView) findViewById(R.id.sd_big);
        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        });
        Intent intent= getIntent();
        String url=intent.getExtras().getString("url");
        simpleDraweeView.setImageURI(Uri.parse(url));
    }
}
