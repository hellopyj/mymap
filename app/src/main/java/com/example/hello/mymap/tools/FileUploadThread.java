package com.example.hello.mymap.tools;

import android.net.Uri;
import android.util.Log;

import com.example.hello.mymap.MyApplication;
import com.example.hello.mymap.utils.Config;
import com.facebook.common.file.FileUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.persistent.FileRecorder;
import com.qiniu.android.utils.AsyncRun;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Lovepyj on 2016/6/2.
 */
public class FileUploadThread extends Thread {

    String url;
    String newname;
    UploadOptions uploadOptions;
    UpCompletionHandler upCompletionHandler;
    File uploadFile;
    public FileUploadThread(String url, String newname,UploadOptions uploadOptions, UpCompletionHandler upCompletionHandler)
    {
        this.url=url;
        this.newname=newname;
        this.uploadOptions=uploadOptions;
        this.upCompletionHandler=upCompletionHandler;
    }

    @Override
    public void run() {
        super.run();
        uploadFile = new File(this.url);
        if(!uploadFile.exists())
        {
            Log.v("fileerror","文件不存在");
            return;
        }
        final OkHttpClient httpClient = MyApplication.httpClient;
        Request req = new Request.Builder().url(Config.UPLOAD_URL).build();
        Response resp = null;
        try {
            resp = httpClient.newCall(req).execute();
            String key=resp.body().string();
            Log.v("key",key);
            upload(key);
        } catch (IOException e) {
            //e.printStackTrace();
            Log.v("neterror","网络错误");
        }
    }

    private void upload(String uploadToken) {

        UploadManager uploadManager = null;
        try {
            uploadManager = new UploadManager(new FileRecorder(
                                MyApplication.applicationContext.getFilesDir() + "/QiniuAndroid"),
                                new KeyGenerator() {
                                    // 指定一个进度文件名，用文件路径和最后修改时间做hash
                                    // generator
                                    @Override
                                    public String gen(String key, File file) {
                                        String recorderName = System.currentTimeMillis() + ".progress";
                                        //recorderName = "nihao";
                                        return "niahao1";
                                    }
                                });
        } catch (IOException e) {
           // e.printStackTrace();
            Log.v("um","出现错误");
            return;
        }
        if(uploadOptions==null)
        {
            uploadOptions = new UploadOptions(null, null, false,
                    new UpProgressHandler() {
                        @Override
                        public void progress(String key, double percent) {
                            Log.v("jindu",percent+"");
                        }
                    }, new UpCancellationSignal() {

                @Override
                public boolean isCancelled() {
                    return false;
                }
            });
        }
        if (upCompletionHandler==null)
        {
            upCompletionHandler=new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    Log.v("ok","ok");
                }
            };
        }
//        UploadOptions uploadOptions = new UploadOptions(null, null, false,
//                new UpProgressHandler() {
//                    @Override
//                    public void progress(String key, double percent) {
//                        Log.v("jindu",percent+"");
//                    }
//                }, new UpCancellationSignal() {
//
//            @Override
//            public boolean isCancelled() {
//                return cancelUpload;
//            }
//        });

        uploadManager.put(uploadFile, newname, uploadToken,upCompletionHandler, uploadOptions);

    }

}
