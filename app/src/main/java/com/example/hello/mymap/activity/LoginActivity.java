package com.example.hello.mymap.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.example.hello.mymap.DemoHelper;
import com.example.hello.mymap.MyApplication;
import com.example.hello.mymap.R;
import com.example.hello.mymap.dbtool.DemoDBManager;
import com.example.hello.mymap.model.ReturnJs;
import com.example.hello.mymap.utils.Config;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;


import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    public static final int REQUEST_CODE_SETNICK = 1;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private boolean progressShow=false;
    private boolean autoLogin = false;
    private String currentUsername;
    private String currentPassword;
    private  ProgressDialog pd;
    private Handler handler= new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 1:
                    pd.show();
                    progressShow=true;
                    break;
                case 2:
                    pd.dismiss();
                    progressShow=false;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 如果登录成功过，直接进入主页面
        if (DemoHelper.getInstance().isLoggedIn()) {
            autoLogin = true;
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            return;
        }
        setContentView(R.layout.activity_login);
        usernameEditText= (EditText) findViewById(R.id.et_username);
        passwordEditText= (EditText) findViewById(R.id.et_password);
        // 如果用户名改变，清空密码
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEditText.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (DemoHelper.getInstance().getCurrentUsernName() != null) {
            usernameEditText.setText(DemoHelper.getInstance().getCurrentUsernName());
        }
        pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // Log.d(TAG, "EMClient.getInstance().onCancel");
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));

    }
    /**
     * 登录
     *
     * @param view
     */
    public void login(View view) {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        currentUsername = usernameEditText.getText().toString().trim();
        currentPassword = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap

    }

    public void gotoregit(View view)
    {
        startActivity(new Intent(LoginActivity.this,RegistActivity.class));
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
    }
    //网络请求
    public void execute() throws Exception {
        final Request request = new Request.Builder()
                .url(String.format(Config.LOGIN_URL,currentUsername,currentPassword,MyApplication.getInstance().getDeviceId()))
                .build();
        Call call =MyApplication.httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful()) {
                    String html = response.body().string();
                    Log.v("sdasa",html);
                    ReturnJs returnJs= LoganSquare.parse(html, ReturnJs.class);
                   // JSONObject code = JSON.parseObject(html);
                    if (returnJs.code==1) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        DemoDBManager.getInstance().closeDB();
                        HashMap<String,String> aa = returnJs.content;
                        //Log.v("ss", aa.get("hxid").toString() + aa.get("hxpwd").toString());
                           // currentUsername= aa.get("hxid");
                            currentPassword= aa.get("hxpwd");
                        // reset current user name before login
                        DemoHelper.getInstance().setCurrentUserName(currentUsername);

                        final long start = System.currentTimeMillis();
                        // 调用sdk登陆方法登陆聊天服务器
                        // Log.d(TAG, "EMClient.getInstance().login");
                        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                //  Log.d(TAG, "login: onSuccess");

                                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                                    Message message = new Message();
                                    message.what = 2;
                                    handler.sendMessage(message);
                                }

                                // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                                // ** manually load all local groups and
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();

                                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                                        MyApplication.currentUserNick.trim());
                                if (!updatenick) {
                                    //    Log.e("LoginActivity", "update current user nick fail");
                                }
                                //异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
                                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

                                // 进入主页面
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onProgress(int progress, String status) {
                                Log.d(TAG, "login: onProgress");
                            }

                            @Override
                            public void onError(final int code, final String message) {
                                //  Log.d(TAG, "login: onError: " + code);
                                if (!progressShow) {
                                    return;
                                }
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Message message1=new Message();
                                message1.what=2;
                                handler.sendMessage(message1);
                                Toast.makeText(LoginActivity.this, "用户名或密码不对", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //Toast.makeText(LoginActivity.this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();

                    }
                }

                //Log.v("dada",htmlStr+Config.FormatUrl(Config.LOGIN_URL,currentUsername,currentPassword));

            }
        });
    }
}
