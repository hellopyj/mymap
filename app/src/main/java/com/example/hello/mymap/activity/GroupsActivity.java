/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.hello.mymap.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.mymap.Constant;
import com.example.hello.mymap.R;
import com.example.hello.mymap.adapter.ChatRoomAdapter;
import com.example.hello.mymap.adapter.GroupAdapter;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class GroupsActivity extends BaseActivity {
	public static final String TAG = "GroupsActivity";
	TabLayout tablayout;
	TextView title;
	SearchView searchView;
	private ListView cgroupListView;
	protected List<EMGroup> grouplist;
	private GroupAdapter groupAdapter;

	protected List<EMChatRoom> roomList;
	private ChatRoomAdapter chatRoomAdapter;

	private ArrayAdapter< ?> currentAdapter;
	private  int currentTab;

	private InputMethodManager inputMethodManager;
	public static GroupsActivity instance;
	private View progressBar;
	private SwipeRefreshLayout swipeRefreshLayout;
	
	
	Handler handler = new Handler(){
	    public void handleMessage(android.os.Message msg) {
	        swipeRefreshLayout.setRefreshing(false);
	        switch (msg.what) {
            case 0:
                refresh();
                break;
            case 1:
                Toast.makeText(GroupsActivity.this, R.string.Failed_to_get_group_chat_information, Toast.LENGTH_LONG).show();
                break;

            default:
                break;
            }
	    };
	};

		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_fragment_groups);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		title= (TextView) findViewById(R.id.tv_title);
		instance = this;
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		grouplist = EMClient.getInstance().groupManager().getAllGroups();
		cgroupListView = (ListView) findViewById(R.id.list);
		//show group list
        groupAdapter = new GroupAdapter(this, 1, grouplist);

		roomList=EMClient.getInstance().chatroomManager().getAllChatRooms();
		Log.v("saa",roomList.size()+"");
		chatRoomAdapter=new ChatRoomAdapter(this,1,roomList);

		currentAdapter=groupAdapter;
        cgroupListView.setAdapter(currentAdapter);
	//	searchView= (SearchView) findViewById(R.id.menu_search);
//		searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View view, boolean b) {
//				Log.v("sasa","jhhh");
//			}
//		});
		tablayout= (TabLayout) findViewById(R.id.tb_tabs);
		tablayout.addTab(tablayout.newTab().setText("群组"));
		tablayout.addTab(tablayout.newTab().setText("讨论组"));
		tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				if(tab.getPosition()!=currentTab) {
					if (tab.getPosition() == 0) {
						//currentTab=0;
						currentAdapter=groupAdapter;
						//cgroupListView.setAdapter(groupAdapter);
					}
					else if (tab.getPosition() == 1) {
						//currentTab=1;
						currentAdapter=chatRoomAdapter;
					}
					cgroupListView.setAdapter(currentAdapter);
					currentTab=tab.getPosition();
				}

			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
		currentTab=tablayout.getSelectedTabPosition();
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
		swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
		                R.color.holo_orange_light, R.color.holo_red_light);
		//下拉刷新
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new Thread(){
					@Override
					public void run(){
						if(currentTab==0) {
							try {
								EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
							} catch (HyphenateException e) {
								e.printStackTrace();
								handler.sendEmptyMessage(1);
							}
						}
						handler.sendEmptyMessage(0);
					}
				}.start();
			}
		});
		cgroupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				if (position == 1) {
//					// 新建群聊
//					startActivityForResult(new Intent(GroupsActivity.this, TestActivity.class), 0);
//				} else if (position == 2) {
//					// 添加公开群
//					startActivityForResult(new Intent(GroupsActivity.this, TestActivity.class), 0);
//				} else {
					// 进入群聊
					Intent intent = new Intent(GroupsActivity.this, ChatActivity.class);
					// it is group chat
				if(currentTab==0) {
					intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
					intent.putExtra("userId", groupAdapter.getItem(position).getGroupId());
				}
				else if(currentTab==1)
				{
					intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
					intent.putExtra("userId", chatRoomAdapter.getItem(position).getId());
				}

					startActivityForResult(intent, 0);
				}
//			}
//
		});
		cgroupListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
		
	}

	/**
	 * 进入公开群聊列表
	 */
	public void onPublicGroups(View view) {
		startActivity(new Intent(this, TestActivity.class));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
        refresh();
		super.onResume();
	}
	
	private void refresh(){
		if(currentTab==0) {
			grouplist = EMClient.getInstance().groupManager().getAllGroups();
			groupAdapter = new GroupAdapter(this, 1, grouplist);
			cgroupListView.setAdapter(groupAdapter);
			groupAdapter.notifyDataSetChanged();
		}
		else if(currentTab==1)
		{
			roomList=EMClient.getInstance().chatroomManager().getAllChatRooms();
			chatRoomAdapter=new ChatRoomAdapter(this,1,roomList);
			cgroupListView.setAdapter(chatRoomAdapter);
			chatRoomAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}
	

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_group, menu);
		searchView= (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				currentAdapter.getFilter().filter(searchView.getQuery());
				return false;
			}
		});
		return true;
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
