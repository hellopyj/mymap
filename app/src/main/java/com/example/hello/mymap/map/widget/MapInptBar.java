package com.example.hello.mymap.map.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.hello.mymap.MyApplication;
import com.example.hello.mymap.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.model.EaseDefaultEmojiconDatas;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.widget.EaseChatPrimaryMenu;
import com.hyphenate.easeui.widget.EaseChatPrimaryMenuBase;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenu;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenuBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lovepyj on 2016/5/26.
 */
public class MapInptBar extends LinearLayout {
    View parent;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    public Button cancle;
    ImageView ivQuestion;
    public EaseVoiceRecorderView voiceRecorderView;
    public boolean isQuestion=false;
    RelativeLayout bottomBtns;
    FrameLayout primaryMenuContainer, emojiconMenuContainer;
    protected EaseChatPrimaryMenuBase chatPrimaryMenu;
    protected EaseEmojiconMenuBase emojiconMenu;
    //protected EaseChatExtendMenu chatExtendMenu;
    protected FrameLayout chatExtendMenuContainer;
    protected LayoutInflater layoutInflater;

    private Handler handler = new Handler();
    private ChatInputMenuListener listener;
    private BottomClickLister btlistener;
    private Context context;
    private boolean inited;

    public MapInptBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public MapInptBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MapInptBar(Context context) {
        super(context);
        init(context, null);
    }
    //底部按钮
    private void setBottomClickLister()
    {
        OnClickListener onClickListener=new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btlistener!=null) {
                    btlistener.extentButton((int) view.getTag());
                }
            }
        };
        button1.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);
        button4.setOnClickListener(onClickListener);
        OnClickListener  onClickListener1=new OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(btlistener!=null) {
                    btlistener.cancleButton();
                    cancle.setVisibility(INVISIBLE);
                }
            }
        };
        cancle.setOnClickListener(onClickListener1);
    }
    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.map_input_bar, this);

        button1= (Button) findViewById(R.id.bt_ar);
        button2= (Button) findViewById(R.id.bt_voice);
        button3= (Button) findViewById(R.id.bt_pic);
        button4= (Button) findViewById(R.id.bt_video);
        cancle= (Button) findViewById(R.id.bt_cancle);
        button1.setTag(1);
        button2.setTag(2);
        button3.setTag(3);
        button4.setTag(4);
        ivQuestion= (ImageView) findViewById(R.id.iv_tip_question);
        bottomBtns= (RelativeLayout) findViewById(R.id.ll_btbt);
        voiceRecorderView = (EaseVoiceRecorderView)findViewById(R.id.voice_recorder);
        primaryMenuContainer = (FrameLayout) findViewById(R.id.primary_menu_container);
        emojiconMenuContainer = (FrameLayout) findViewById(R.id.emojicon_menu_container);
        chatExtendMenuContainer = (FrameLayout) findViewById(R.id.extend_menu_container);
        setBottomClickLister();

        // 扩展按钮栏
       // chatExtendMenu = (EaseChatExtendMenu) findViewById(com.hyphenate.easeui.R.id.extend_menu);

    }
    public void setnottxt()
    {

    }

    /**
     * init view 此方法需放在registerExtendMenuItem后面及setCustomEmojiconMenu，
     * setCustomPrimaryMenu(如果需要自定义这两个menu)后面
     * @param emojiconGroupList 表情组类别，传null使用easeui默认的表情
     */
    public void init(List<EaseEmojiconGroupEntity> emojiconGroupList) {
        if(inited){
            return;
        }
        // 主按钮菜单栏,没有自定义的用默认的
        if(chatPrimaryMenu == null){
            chatPrimaryMenu = (EaseChatPrimaryMenu) layoutInflater.inflate(com.hyphenate.easeui.R.layout.ease_layout_chat_primary_menu, null);
        }
        primaryMenuContainer.addView(chatPrimaryMenu);

        // 表情栏，没有自定义的用默认的
        if(emojiconMenu == null){
            emojiconMenu = (EaseEmojiconMenu) layoutInflater.inflate(com.hyphenate.easeui.R.layout.ease_layout_emojicon_menu, null);
            if(emojiconGroupList == null){
                emojiconGroupList = new ArrayList<EaseEmojiconGroupEntity>();
                emojiconGroupList.add(new EaseEmojiconGroupEntity(com.hyphenate.easeui.R.drawable.ee_1,  Arrays.asList(EaseDefaultEmojiconDatas.getData())));
            }
            ((EaseEmojiconMenu)emojiconMenu).init(emojiconGroupList);
        }
        emojiconMenuContainer.addView(emojiconMenu);

        processChatMenu();
        // 初始化extendmenu
       // chatExtendMenu.init();

        inited = true;
    }

    public void init(){
        init(null);
    }

    /**
     * 设置自定义的表情栏，该控件需要继承自EaseEmojiconMenuBase，
     * 以及回调你想要回调出去的事件给设置的EaseEmojiconMenuListener
     * @param customEmojiconMenu
     */
    public void setCustomEmojiconMenu(EaseEmojiconMenuBase customEmojiconMenu){
        this.emojiconMenu = customEmojiconMenu;
    }

    /**
     * 设置自定义的主菜单栏，该控件需要继承自EaseChatPrimaryMenuBase，
     * 以及回调你想要回调出去的事件给设置的EaseEmojiconMenuListener
     * @param customEmojiconMenu
     */
    public void setCustomPrimaryMenu(EaseChatPrimaryMenuBase customPrimaryMenu){
        this.chatPrimaryMenu = customPrimaryMenu;
    }

    public EaseChatPrimaryMenuBase getPrimaryMenu(){
        return chatPrimaryMenu;
    }

//    public EaseChatExtendMenu getExtendMenu(){
//        return chatExtendMenu;
//    }

    public EaseEmojiconMenuBase getEmojiconMenu(){
        return emojiconMenu;
    }


    /**
     * 注册扩展菜单的item
     *
     * @param name
     *            item名字
     * @param drawableRes
     *            item背景
     * @param itemId
     *            id
     * @param listener
     *            item点击事件
     */
//    public void registerExtendMenuItem(String name, int drawableRes, int itemId,
//                                       EaseChatExtendMenu.EaseChatExtendMenuItemClickListener listener) {
//        chatExtendMenu.registerMenuItem(name, drawableRes, itemId, listener);
//    }

    /**
     * 注册扩展菜单的item
     *
     */
//    public void registerExtendMenuItem(int nameRes, int drawableRes, int itemId,
//                                       EaseChatExtendMenu.EaseChatExtendMenuItemClickListener listener) {
//        chatExtendMenu.registerMenuItem(nameRes, drawableRes, itemId, listener);
//    }


    protected void processChatMenu() {
        // 发送消息栏
        chatPrimaryMenu.setChatPrimaryMenuListener(new EaseChatPrimaryMenuBase.EaseChatPrimaryMenuListener() {

            @Override
            public void onSendBtnClicked(String content) {
                if (listener != null)
                    listener.onSendMessage(content);
            }

            @Override
            public void onToggleVoiceBtnClicked() {
                hideKeyboard();
                hideExtendMenuContainer();
            }

            @Override
            public void onToggleExtendClicked() {
                toggleMore();
                if(isQuestion) {
                    isQuestion=false;
                    ivQuestion.setVisibility(INVISIBLE);
                }
                else {
                    isQuestion=true;
                    ivQuestion.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onToggleEmojiconClicked() {
                toggleEmojicon();
            }

            @Override
            public void onEditTextClicked() {
                hideExtendMenuContainer();
            }


            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                if(listener != null){
                    return listener.onPressToSpeakBtnTouch(v, event);
                }
                return false;
//                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {
//
//                    @Override
//                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
//                        // 发送语音消息
//                       // sendVoiceMessage(voiceFilePath, voiceTimeLength);
//                    }
//                });
            }
        });

        // emojicon menu
        emojiconMenu.setEmojiconMenuListener(new EaseEmojiconMenuBase.EaseEmojiconMenuListener() {

            @Override
            public void onExpressionClicked(EaseEmojicon emojicon) {
                if(emojicon.getType() != EaseEmojicon.Type.BIG_EXPRESSION){
                    if(emojicon.getEmojiText() != null){
                        chatPrimaryMenu.onEmojiconInputEvent(EaseSmileUtils.getSmiledText(context,emojicon.getEmojiText()));
                    }
                }else{
                    if(listener != null){
                        listener.onBigExpressionClicked(emojicon);
                    }
                }
            }

            @Override
            public void onDeleteImageClicked() {
                chatPrimaryMenu.onEmojiconDeleteEvent();
            }
        });

    }

    /**
     * 显示或隐藏图标按钮页
     *
     */
    protected void toggleMore() {
        if (chatExtendMenuContainer.getVisibility() == View.GONE) {
            hideKeyboard();
            handler.postDelayed(new Runnable() {
                public void run() {
                    chatExtendMenuContainer.setVisibility(View.VISIBLE);
                    //chatExtendMenu.setVisibility(View.VISIBLE);
                    emojiconMenu.setVisibility(View.GONE);
                   // bottomBtns.setVisibility(View.VISIBLE);
                }
            }, 50);
        } else {
            if (emojiconMenu.getVisibility() == View.VISIBLE) {
                emojiconMenu.setVisibility(View.GONE);
                bottomBtns.setVisibility(VISIBLE);
                //chatExtendMenu.setVisibility(View.VISIBLE);
            } else {
                chatExtendMenuContainer.setVisibility(View.GONE);
            }
          //  bottomBtns.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 显示或隐藏表情页
     */
    protected void toggleEmojicon() {
        if (chatExtendMenuContainer.getVisibility() == View.GONE) {
            handler.postDelayed(new Runnable() {
                public void run() {
                    hideKeyboard();
                   chatExtendMenuContainer.setVisibility(View.VISIBLE);
                   // chatExtendMenu.setVisibility(View.GONE);
                    emojiconMenu.setVisibility(View.VISIBLE);
                    bottomBtns.setVisibility(View.GONE);
                }
            }, 50);
        } else {
            if (emojiconMenu.getVisibility() == View.VISIBLE) {
                chatExtendMenuContainer.setVisibility(View.GONE);
                emojiconMenu.setVisibility(View.GONE);
                bottomBtns.setVisibility(View.VISIBLE);
            } else {
               // chatExtendMenu.setVisibility(View.GONE);
                emojiconMenu.setVisibility(View.VISIBLE);
                bottomBtns.setVisibility(View.GONE);
            }

        }
    }

    /**
     * 隐藏软键盘
     */

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) MyApplication.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()) {
           // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            imm.hideSoftInputFromWindow(parent .getWindowToken(), 0);
        }

    }

    /**
     * 隐藏整个扩展按钮栏(包括表情栏)
     */
    public void hideExtendMenuContainer() {
        //chatExtendMenu.setVisibility(View.GONE);
        emojiconMenu.setVisibility(View.GONE);
        bottomBtns.setVisibility(VISIBLE);
        chatExtendMenuContainer.setVisibility(View.GONE);
        chatPrimaryMenu.onExtendMenuContainerHide();
    }

    /**
     * 系统返回键被按时调用此方法
     *
     * @return 返回false表示返回键时扩展菜单栏时打开状态，true则表示按返回键时扩展栏是关闭状态<br/>
     *         如果返回时打开状态状态，会先关闭扩展栏再返回值
     */
    public boolean onBackPressed() {
        if (chatExtendMenuContainer.getVisibility() == View.VISIBLE) {
            hideExtendMenuContainer();
            return false;
        } else {
            return true;
        }

    }


    public void setChatInputMenuListener(ChatInputMenuListener listener) {
        this.listener = listener;
    }
    public void setBottomLister(BottomClickLister listener) {
        this.btlistener= listener;
    }

public interface BottomClickLister{
    void extentButton(int tag);
    void cancleButton();
}
    public interface ChatInputMenuListener {
        /**
         * 发送消息按钮点击
         *
         * @param content
         *            文本内容
         */
        void onSendMessage(String content);

        /**
         * 大表情被点击
         * @param emojicon
         */
        void onBigExpressionClicked(EaseEmojicon emojicon);

        /**
         * 长按说话按钮touch事件
         * @param v
         * @param event
         * @return
         */
        boolean onPressToSpeakBtnTouch(View v, MotionEvent event);
    }
}
