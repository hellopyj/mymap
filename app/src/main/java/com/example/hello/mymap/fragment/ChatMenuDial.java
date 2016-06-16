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
package com.example.hello.mymap.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hello.mymap.Constant;
import com.example.hello.mymap.R;
import com.hyphenate.chat.EMMessage;

public class ChatMenuDial extends DialogFragment implements View.OnClickListener{
	TextView textView1;
	TextView textView2;
	TextView textView3;
	EMMessage.Type type;

	@Override
	public void onClick(View view) {
		TextView textView= (TextView) view;
		int item=0;
		if(textView==textView1)
			item=0;
		else if(textView==textView2)
		{
			item=1;
		}
		else
			item=2;
		dismiss();
		((DiaClick)getParentFragment()).itemDo(item);


	}

	public interface DiaClick{
		void itemDo(int item);
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		EMMessage message=getArguments().getParcelable("message");
//		int viewid = 0;
//		int type = message.getType().ordinal();
//		if (type == EMMessage.Type.TXT.ordinal()) {
//			if(message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false) ||
//					message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
//				 viewid=R.layout.em_context_menu_for_location;
//			}else if(message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
//				viewid=R.layout.em_context_menu_for_image;
//			}else{
//				viewid=R.layout.em_context_menu_for_text;
//			}
//		} else if (type == EMMessage.Type.LOCATION.ordinal()) {
//			viewid=R.layout.em_context_menu_for_location;
//		} else if (type == EMMessage.Type.IMAGE.ordinal()) {
//			viewid=R.layout.em_context_menu_for_image;
//		} else if (type == EMMessage.Type.VOICE.ordinal()) {
//			viewid=R.layout.em_context_menu_for_voice;
//		} else if (type == EMMessage.Type.VIDEO.ordinal()) {
//			viewid=R.layout.em_context_menu_for_video;
//		} else if (type == EMMessage.Type.FILE.ordinal()) {
//			viewid=R.layout.em_context_menu_for_location;
//		}
//		View view=inflater.inflate(R.layout.fragmnet_dialog,container,false);
//		LinearLayout linearLayout= (LinearLayout) view.findViewById(R.id.ll_fr_dialog);
//		for(int i=0;i<3;i++) {
//			TextView textView = new TextView(linearLayout.getContext());
//			LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//			textView.setLayoutParams(layoutParams);
//			textView.setText("复制");
//			textView.setBackgroundResource(R.drawable.em_context_menu_item_bg);
//			textView.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//			textView.setTextSize(18);
//			textView.setPadding(60, 20, 20, 20);
//			textView.setClickable(true);
//			textView.setGravity(Gravity.CENTER_VERTICAL);
//			linearLayout.addView(textView);
//		}
//		return view;
		View view=inflater.inflate(R.layout.em_context_menu,container,false);
		textView1= (TextView) view.findViewById(R.id.copy_message);
		textView2= (TextView) view.findViewById(R.id.forward_message);
		textView3= (TextView) view.findViewById(R.id.delete_message);
		textView1.setOnClickListener(this);
		textView2.setOnClickListener(this);
		textView3.setOnClickListener(this);
		if(type!=null)
			doView();
		return view;
	}

	void setMessageType(EMMessage.Type a)
	{
		type=a;
	}
void doView()
{
	if(type.ordinal()==EMMessage.Type.IMAGE.ordinal())
	{
		textView1.setVisibility(View.GONE);
	}
	else  if(type.ordinal()==EMMessage.Type.VOICE.ordinal())
	{
		textView1.setVisibility(View.GONE);
		textView2.setVisibility(View.GONE);
	}
}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		EMMessage message = getIntent().getParcelableExtra("message");
//
//		int type = message.getType().ordinal();
//		if (type == EMMessage.Type.TXT.ordinal()) {
//		    if(message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false) ||
//		            message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
//		        setContentView(R.layout.em_context_menu_for_location);
//		    }else if(message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
//		        setContentView(R.layout.em_context_menu_for_image);
//		    }else{
//		        setContentView(R.layout.em_context_menu_for_text);
//		    }
//		} else if (type == EMMessage.Type.LOCATION.ordinal()) {
//		    setContentView(R.layout.em_context_menu_for_location);
//		} else if (type == EMMessage.Type.IMAGE.ordinal()) {
//		    setContentView(R.layout.em_context_menu_for_image);
//		} else if (type == EMMessage.Type.VOICE.ordinal()) {
//		    setContentView(R.layout.em_context_menu_for_voice);
//		} else if (type == EMMessage.Type.VIDEO.ordinal()) {
//			setContentView(R.layout.em_context_menu_for_video);
//		} else if (type == EMMessage.Type.FILE.ordinal()) {
//		    setContentView(R.layout.em_context_menu_for_location);
//		}
//
//	}
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		finish();
//		return true;
//	}

//	public void copy(View view){
//		setResult(RESULT_CODE_COPY);
//		finish();
//	}
//	public void delete(View view){
//		setResult(RESULT_CODE_DELETE);
//		finish();
//	}
//	public void forward(View view){
//		setResult(RESULT_CODE_FORWARD);
//		finish();
//	}

}
