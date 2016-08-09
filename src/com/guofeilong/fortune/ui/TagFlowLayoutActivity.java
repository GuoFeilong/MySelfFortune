package com.guofeilong.fortune.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.tagflowlayout.FlowLayout;
import com.guofeilong.fortune.ui.view.tagflowlayout.TagAdapter;
import com.guofeilong.fortune.ui.view.tagflowlayout.TagFlowLayout;
import com.guofeilong.fortune.utils.T;

import java.util.Set;

public class TagFlowLayoutActivity extends BaseActivity implements OnClickListener {
    private int mColor;
    private String mTitle;

    private LayoutInflater mInflater;
    private TagFlowLayout mTagLayoutSize;
    private TagFlowLayout mTagLayoutColor;
    private String[] mVals = new String[]
            {"  S  ", "  M  ", " L  ", "  XL  ", "  XXL  ", "  3XL  ", " 定制 "};
    private String[] mColors = new String[]{"  土豪金  ","  流光银  ","  放荡灰  ","  透明  ",};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tag_flow_layout);
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
        setAdapter(mTagLayoutSize,mVals);
        setAdapter(mTagLayoutColor,mColors);

    }

    private void setAdapter(TagFlowLayout tagFlowLayout,String[]data) {
        tagFlowLayout.setAdapter(new TagAdapter<String>(data) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                // 指定textview的rootview
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,mTagLayoutSize,false);
                tv.setText(s);
                return tv;
            }
        });

        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                T.show(TagFlowLayoutActivity.this, mVals[position] + "====", 0);
                return true;
            }
        });


        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                T.show(TagFlowLayoutActivity.this, selectPosSet.toString() + "choose", 0);

            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mInflater = LayoutInflater.from(this);
        mTagLayoutSize = (TagFlowLayout) findViewById(R.id.tag_flow_layout_size);
        mTagLayoutColor = (TagFlowLayout) findViewById(R.id.tag_flow_layout_color);

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
}
