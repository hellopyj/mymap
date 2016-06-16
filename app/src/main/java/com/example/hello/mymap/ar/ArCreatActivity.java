package com.example.hello.mymap.ar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hello.mymap.R;
import com.example.hello.mymap.activity.ImageGridActivity;
import com.example.hello.mymap.tools.MyTools;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.io.FileOutputStream;

public class ArCreatActivity extends AppCompatActivity {
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    protected static final int REQUEST_CODE_VIDEO=4;
    Button buttontakepic;
    Button buttonphoto;
    Button buttonchosevideo;
    SimpleDraweeView simpleDraweeViewpic;
    ImageView simpleDraweeViewvideo;
    String picurl="";
    String videourl="";
    File cameraFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_creat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttontakepic= (Button) findViewById(R.id.bt_takepic);
        buttonphoto= (Button) findViewById(R.id.bt_photo);
        buttonchosevideo= (Button) findViewById(R.id.bt_chose_video);
        buttontakepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPicFromCamera();
            }
        });
        buttonphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPicFromLocal();
            }
        });
        buttonchosevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArCreatActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_VIDEO);
            }
        });
        simpleDraweeViewpic= (SimpleDraweeView) findViewById(R.id.sd_pic);
        simpleDraweeViewvideo= (ImageView) findViewById(R.id.sd_video);

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
        if(item.getItemId()==R.id.menu_done)
        {
            if(picurl!=""&&videourl!="")
            {
                Intent intent=getIntent();
                intent.putExtra("picurl", picurl).putExtra("videourl", videourl);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            else
            {
                Toast.makeText(ArCreatActivity.this,"不符合要求",Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ar_creat, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (cameraFile != null && cameraFile.exists()) {
                    //Log.v("sas",cameraFile.getAbsolutePath());

                    ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.fromFile(cameraFile))
                            .setAutoRotateEnabled(true)
                            .setResizeOptions(new ResizeOptions(800, 400));

                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(imageRequestBuilder.build())
                            .setOldController(simpleDraweeViewpic.getController())
                            .build();

                    simpleDraweeViewpic.setController(controller);
                    picurl = cameraFile.getAbsolutePath();
                }
                 //   simpleDraweeViewpic.setImageURI(Uri.parse(cameraFile.getAbsolutePath()));
            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        //simpleDraweeViewpic.setImageURI(selectedImage);


                        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(selectedImage)
                                .setAutoRotateEnabled(true)
                                .setResizeOptions(new ResizeOptions(800,400));

                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setImageRequest(imageRequestBuilder.build())
                                .setOldController(simpleDraweeViewpic.getController())
                                .build();

                        simpleDraweeViewpic.setController(controller);
                        picurl= MyTools.getRealFilePath(this,selectedImage);
                    }
                }
            }
            else if(requestCode ==REQUEST_CODE_VIDEO)
            {
                if (data != null) {
                   // int duration = data.getIntExtra("dur", 0);
                    String videoPath = data.getStringExtra("path");
                   // Log.v("video",videoPath);
                 simpleDraweeViewvideo.setImageBitmap(MyTools.getVideoThumbnail(videoPath));
                    videourl=videoPath;
                }
            }
           // Log.v("path",picurl);
        }
    }
    /**
     * 照相获取图片
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isExitsSdcard()) {
            Toast.makeText(ArCreatActivity.this, com.hyphenate.easeui.R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
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


}
