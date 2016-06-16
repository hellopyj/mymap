package com.example.hello.mymap.map.others;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Toast;

import com.example.hello.mymap.MyApplication;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;

import java.io.File;
import java.io.IOException;

/**
 * Created by Lovepyj on 2016/6/10.
 */
public class MarkerVoiceClick {

    static MarkerVoiceClick mmarkerc;
    boolean ispalying = false;
    MediaPlayer mediaPlayer = new MediaPlayer();

    public static MarkerVoiceClick getInstance() {
        if (mmarkerc == null)
            mmarkerc=new MarkerVoiceClick();
            return mmarkerc;
    }

    public void playVoice(String filepath) {
        if (!(new File(filepath).exists())) {
            return;
        }
       // mediaPlayer = new MediaPlayer();
        //AudioManager audioManager = (AudioManager) MyApplication.mainContext.getSystemService(Context.AUDIO_SERVICE);
       if(!ispalying) {
           mediaPlayer = new MediaPlayer();
           AudioManager audioManager = (AudioManager) MyApplication.mainContext.getSystemService(Context.AUDIO_SERVICE);
           if (EaseUI.getInstance().getSettingsProvider().isSpeakerOpened()) {
               audioManager.setMode(AudioManager.MODE_NORMAL);
               audioManager.setSpeakerphoneOn(true);
               mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
           } else {
               audioManager.setSpeakerphoneOn(false);// 关闭扬声器
               // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
               audioManager.setMode(AudioManager.MODE_IN_CALL);
               mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
           }
           try {
               mediaPlayer.setDataSource(filepath);
               mediaPlayer.prepare();
               mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                   @Override
                   public void onCompletion(MediaPlayer mp) {
                       // TODO Auto-generated method stub
                       mediaPlayer.reset();
                       mediaPlayer.release();
                       mediaPlayer = null;
                       stopPlay(); // stop animation
                       Toast.makeText(MyApplication.mainContext,"播放完成",Toast.LENGTH_SHORT).show();
                   }

               });
               ispalying = true;
               //currentPlayListener = this;
               mediaPlayer.start();

           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }
    public void stopPlay() {
        // stop play voice
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        ispalying = false;
    }
}