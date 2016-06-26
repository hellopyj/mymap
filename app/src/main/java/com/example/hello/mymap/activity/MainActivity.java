package com.example.hello.mymap.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.example.hello.mymap.Constant;
import com.example.hello.mymap.DemoHelper;
import com.example.hello.mymap.MyApplication;
import com.example.hello.mymap.R;
import com.example.hello.mymap.fragment.ChatFragment;
import com.example.hello.mymap.fragment.CommountionFragment;
import com.example.hello.mymap.fragment.ContactListFragment;
import com.example.hello.mymap.fragment.MapFragment;
import com.example.hello.mymap.map.markers.MyMarkerOption;
import com.example.hello.mymap.map.modle.MyMessage;
import com.example.hello.mymap.tools.FileDownloadCallback;
import com.example.hello.mymap.tools.FileDownloadTask;
import com.example.hello.mymap.tools.FileUploadThread;
import com.example.hello.mymap.utils.Config;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseBaseFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.lzp.floatingactionbuttonplus.FabTagLayout;
import com.lzp.floatingactionbuttonplus.FloatingActionButtonPlus;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseActivity {

    android.app.FragmentManager fragmentManager;
    ContactListFragment contactListFragment;
    MapFragment mapFragment;
//    public Button buttonok;
   // LinkedList<MenuItem> menuItems;
    public MenuItem miserach;
    public SubMenu mysumenu;
    CommountionFragment commountionFragment;
    Fragment[] contentFragment;
    public SearchView searchView;
    public FloatingActionButtonPlus fbPlus;
    int currentFragmentIndex;
    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;

    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;
   //Boolean [] fbOrnot={false,false,true};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            DemoHelper.getInstance().logout(false,null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }


        setContentView(R.layout.activity_main);
        MyApplication.mainContext=this;

        if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }

        fragmentManager=getFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fbPlus=(FloatingActionButtonPlus)findViewById(R.id.fb_plus);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                int index=0;
                if (id == R.id.nav_chat) {
                    // Handle the camera action
                    index=0;
                }
                else if (id == R.id.nav_communication) {
                    index=1;

                }else if (id == R.id.nav_map) {
                   index=2;

                }  else if (id == R.id.nav_manage) {
//                    new FileUploadThread("/storage/emulated/0/1.png",
//                            null,    new UpCompletionHandler() {
//                        @Override
//                        public void complete(final String key, final ResponseInfo respInfo,
//                                             final JSONObject jsonData) {
//                            Log.v("ok","ok");
//                        }}).start();

                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_send) {

                }
                changeFragment(index);
                changeFb();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawers();
                return true;
            }
        });
        navigationView.setCheckedItem(R.id.nav_chat);
        inItFragment();
        registerBroadcastReceiver();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
       // searchView= (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        //buttonok= (Button) MenuItemCompat.getActionView(menu.findItem(R.id.menu_ok));
       // buttonok.setVisibility(View.GONE);
        //searchView.setVisibility(View.INVISIBLE);
        miserach=menu.findItem(R.id.menu_search);
       // mymenu=menu;
        //menuItems.add( menu.findItem(R.id.menu_ok));
        //MenuItem buttondone = menu.findItem(R.id.menu_ok);
        //buttondone.seto
        //buttondone.setVisible(false);
       // SubMenu fontMenu=menu.addSubMenu("字体大小");
        //menu.add("sss");
        //fontMenu.setHeaderIcon(R.drawable.camera_switch_normal);
        mysumenu= menu.addSubMenu("更多");
        mysumenu.add(1,1,100,"关于").setIcon(R.drawable.ic_help_black_24dp);
       // mysumenu.add(1,1,100,"消息").setIcon(R.drawable.compass);
        MenuItem item =  mysumenu.getItem();
        item.setIcon(R.drawable.ic_more_vert_black_24dp);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
       // return true;
    }
    //设置默认Fragment
    void inItFragment()
    {
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        //menuItems=new LinkedList<>();
        contactListFragment=new ContactListFragment();
        mapFragment=new MapFragment();
        commountionFragment=new CommountionFragment();
        contentFragment=new Fragment[]{contactListFragment,commountionFragment,mapFragment};
        currentFragmentIndex=0;
        transaction.add(R.id.fr_content,contactListFragment).add(R.id.fr_content,mapFragment).add(R.id.fr_content,commountionFragment)
                .hide(mapFragment).hide(commountionFragment).show(contactListFragment).commit();
        changeFb();

    }
    //改变framgent
    void changeFragment(int i) {
        if (i != currentFragmentIndex) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
           transaction.hide(contentFragment[currentFragmentIndex]);
            if(!contentFragment[i].isAdded())
            {
                transaction.add(R.id.fr_content, contentFragment[i]);
            }
            transaction.show(contentFragment[i]).commit();
            //Log.v("ssas",i+"");
           // changeFb(i);
            currentFragmentIndex=i;
        }

    }
    //改变fb状态
    void changeFb()
    {

            if (currentFragmentIndex==2) {
              //  fbPlus.setVisibility(View.VISIBLE);
                fbPlus.showFab();
               // Log.v("sas",i+"");
            }
            else {
               // fbPlus.setVisibility(View.GONE);
                fbPlus.hideFab();
                //mysumenu.getItem();
                //mysumenu.removeGroup(2);
               // Log.v("sss",i+"");
            }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //待修改
    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if ( currentFragmentIndex== 0) {
                   // contactListFragment.refresh();
                } else if (currentFragmentIndex== 1) {
                  //  commountionFragment.refresh();
                }
                String action = intent.getAction();
                if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }
    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHelper.getInstance().logout(false,null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                Log.v("bug",e.toString());
            }

        }

    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHelper.getInstance().logout(false,null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                Log.v("bug",e.toString());
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isConflict && !isCurrentAccountRemoved) {
           // updateUnreadLabel();
            //updateUnreadAddressLable();
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.mainContext=null;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }
    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
            //Log.v("sasasa","sdsdadsaq");
            // 提示新消息
          //  for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMesg(messages);
           // }

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            for(EMMessage emMessage:messages){
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) emMessage.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
                if("marker".equals(action))
                {
                   // Log.v("jkkk","sasa");
                   // Toast.makeText(MainActivity.this,"nijaq",Toast.LENGTH_SHORT).show();
                    try {
                        String tmp=emMessage.getStringAttribute("newmark");
                        Log.v("sas",tmp);
                        //JSONObject jsonObject=emMessage.getJSONObjectAttribute("newmark");

                        //Toast.makeText(MainActivity.this,"nija",Toast.LENGTH_SHORT).show();
                       // MyMarkerOption myMarkerOption=new MyMarkerOption().type().position(new LatLng())
                        //int type=jsonObject.getInt("type");
                       // double lati = Double.valueOf(jsonObject.getString("latitude"));
                       // double longi = Double.valueOf(jsonObject.getString("longitude"));
                      //  String content=jsonObject.getString("content");
                      //  mapFragment.addNewMark(new MyMarkerOption().type(type).content(content).position(new LatLng(lati,longi)));

                        MyMessage myMessage= LoganSquare.parse(tmp,MyMessage.class);
                       // Log.v("stest",emMessage.getFrom());
                        mapFragment.addNewMark(new MyMarkerOption(myMessage));
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };
    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
               // updateUnreadLabel();
                if ( currentFragmentIndex== 0) {
                    contactListFragment.refresh();
                } else if (currentFragmentIndex== 1) {
                    commountionFragment.refresh();
                }
            }
        });
    }
    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {}
        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_SHORT)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
        }
        @Override
        public void onContactInvited(String username, String reason) {}
        @Override
        public void onContactAgreed(String username) {}
        @Override
        public void onContactRefused(String username) {}
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//       // Log.v("sas","sas");
//    }
}
