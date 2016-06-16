package com.example.hello.mymap.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;

import com.example.hello.mymap.MyApplication;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.PathUtil;

import java.util.List;

/**
 * Created by Lovepyj on 2016/6/5.
 */
public class MyTools {
    //获得后缀名
    public static String getFileSuffix(String url) {
        return url.substring(url.lastIndexOf('.') + 1);
    }

    //获得图片绝对路径
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    //获得缩略
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    //获得随机文件名
    public static String getRandomFileName(String url) {
        return EMClient.getInstance().getCurrentUser() + "_" + System.currentTimeMillis() + getRandomChars(4) + "." + getFileSuffix(url);
    }

    //获得文件名
    public static String urlGetfileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    //
    //随机字符
    public static String getRandomChars(int count) {
        String a = "";
        String all = "abcdefghizklmnopqrstuvwxyz";
        for (int i = 0; i < count; i++) {
            int k = (int) (Math.random() * all.length());
            a += all.charAt(k);
        }
        return a;
    }
}
