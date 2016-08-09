package com.guofeilong.fortune.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.ScrollTextView;

public class ViewScrollActivity extends BaseActivity implements OnClickListener {
    private int mColor;
    private String mTitle;
    private ScrollTextView textView;
    private LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scroll);
        mTitle = getMyTitleAndColor();
        initNavigation();
        initData();
        initView();
        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        // TODO Auto-generated method stub

    }

    /**
     * 初始化控件
     */
    private void initView() {
        // TODO Auto-generated method stub
        textView = (ScrollTextView) findViewById(R.id.stv_scroll);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // TODO Auto-generated method stub

    }

    /**
     * 获取标题
     *
     * @return
     */
    private String getMyTitleAndColor() {
        String title = "";

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                title = extras.getString(AppConstants.INTENT_AMOUNT);
                mColor = extras.getInt(AppConstants.INTENT_NAME_FROM);
            }
        }
        return title;
    }

    /**
     * 初始化通用标题栏
     */
    private void initNavigation() {
        TextView nameTextView = (TextView) findViewById(R.id.commond_textview_title_name);
        nameTextView.setText(mTitle);
        nameTextView.setTextColor(mColor);
        RelativeLayout leftButton = (RelativeLayout) findViewById(R.id.commond_imagebutton_title_leftbutton);
        leftButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.commond_imagebutton_title_leftbutton:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        params = (LinearLayout.LayoutParams) textView.getLayoutParams();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        float xView = event.getX();
        float yView = event.getY();

        Log.i("SCROLL_TAG", "onTouchEvent: " + x + "===" + y + "====" + xView + "====" + yView);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                params.leftMargin = x;
                params.topMargin = y;
                textView.setLayoutParams(params);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
