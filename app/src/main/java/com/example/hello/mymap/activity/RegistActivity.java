package com.example.hello.mymap.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.example.hello.mymap.DemoHelper;
import com.example.hello.mymap.MyApplication;
import com.example.hello.mymap.R;
import com.example.hello.mymap.model.ReturnJs;
import com.example.hello.mymap.utils.Config;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class RegistActivity extends BaseActivity {
    EditText mnickname;
    EditText musername;
    EditText mpassword;
    EditText mrepassword;
    Button mregister;
    ProgressDialog pd;
    String tip;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 1:
                    pd.show();
                   // progressShow=true;
                    break;
                case 2:
                    pd.dismiss();
                    //progressShow=false;
                    break;
                case 3:
                    pd.dismiss();
                    Toast.makeText(RegistActivity.this, tip, Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        mnickname= (EditText) findViewById(R.id.et_nick);
        musername= (EditText) findViewById(R.id.et_email);
        mpassword= (EditText) findViewById(R.id.et_password);
        mrepassword= (EditText) findViewById(R.id.et_repassword);
        mregister=(Button) findViewById(R.id.bt_rester);
        pd = new ProgressDialog(RegistActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // Log.d(TAG, "EMClient.getInstance().onCancel");
                //progressShow = false;
            }
        });
        pd.setMessage("注册中...");

    }
    public void register(View view)
    {
        final String nickname=mnickname.getText().toString().trim();
        final String username =  musername.getText().toString().trim();
        final String pwd = mpassword.getText().toString().trim();
        String confirm_pwd = mrepassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
            musername.requestFocus();
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            mpassword.requestFocus();
            return;
        } else if (TextUtils.isEmpty(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            mrepassword.requestFocus();
            return;
        } else if (!pwd.equals(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Two_input_password), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
            pd.show();
            //网络请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Request request=new Request.Builder().url(String.format(Config.REGISTER_URL,username,pwd,nickname)).build();
                    Call call= MyApplication.httpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Message message=new Message();
                            if(response.isSuccessful()) {
                                String html =response.body().string();
                                Log.v("html",html);
                               // LoganSquare.registerTypeConverter();
                                ReturnJs returnJs= LoganSquare.parse(html, ReturnJs.class);
                                int codei=returnJs.code;
                                if (codei== 1) {
                                    HashMap<String,String> jstmp=returnJs.content;
                                    tip="成功注册";
                                   // Message message=new Message();
                                    DemoHelper.getInstance().setCurrentUserName(username);
                                    message.what=3;
                                    handler.sendMessage(message);
                                }
                                else  if (codei==0)
                                {
                                    tip="用户已被注册";
                                    message.what=3;
                                    handler.sendMessage(message);
                                }
                                else {
                                    tip="用户名或者密码不合法";
                                   // Message message=new Message();
                                    message.what=3;
                                    handler.sendMessage(message);
                                }
                            }

                        }
                    });

                }
            }).start();

//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        // 调用sdk注册方法
//                        EMClient.getInstance().createAccount(username, pwd);
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                if (!RegistActivity.this.isFinishing())
//                                    pd.dismiss();
//                                // 保存用户名
//                                DemoHelper.getInstance().setCurrentUserName(username);
//                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), 0).show();
//                                finish();
//                            }
//                        });
//                    } catch (final HyphenateException e) {
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                if (!RegistActivity.this.isFinishing())
//                                    pd.dismiss();
//                                int errorCode=e.getErrorCode();
//                                if(errorCode== EMError.NETWORK_ERROR){
//                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
//                                }else if(errorCode == EMError.USER_ALREADY_EXIST){
//                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
//                                }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
//                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
//                                }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
//                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
//                                }else{
//                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    }
//                }
//            }).start();

        }
    }

    public void back(View view) {
        finish();
    }
}
