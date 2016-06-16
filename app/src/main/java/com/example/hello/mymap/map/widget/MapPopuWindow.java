package com.example.hello.mymap.map.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import com.example.hello.mymap.R;
import com.example.hello.mymap.map.widget.MapInptBar;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;

/**
 * Created by Lovepyj on 2016/5/23.
 */
public class MapPopuWindow extends PopupWindow{

    public MapInptBar inptBar;
    public ScrollView scrollView;
    public MapPopuWindow(Context context)
    {
        super(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.popuwindow_map_add, null);
        //PopupWindow popWnd = new PopupWindow (contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        inptBar = (MapInptBar) contentView.findViewById(R.id.input_menu);
        // init input menu
        scrollView= (ScrollView) contentView.findViewById(R.id.sv_parent);
        inptBar.init(null);
        inptBar.parent=scrollView;
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.MenuAnimationFade);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
       // listView= (ListView) contentView.findViewById(R.id.lv_popu_content);
    }

}
