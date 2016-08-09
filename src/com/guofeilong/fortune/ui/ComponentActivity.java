package com.guofeilong.fortune.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.infolist.MUiInfoListView;
import com.guofeilong.fortune.ui.view.infolist.MyInfoListStyle;
import com.guofeilong.fortune.ui.view.inventory.InventoryView;

import java.util.ArrayList;
import java.util.List;

public class ComponentActivity extends BaseActivity implements OnClickListener {
    private int mColor;
    private String mTitle;
    private LinearLayout mInfoListContainer;

    private MUiInfoListView mUiInfoListView;
    private MyInfoListStyle style;

    private LinearLayout mInventoryContainer;
    private InventoryView inventoryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_component);
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
        style = new MyInfoListStyle();
        style.setmCanEdit(true);
        style.setmFieldCount(2);
        style.setmWidth(100);

        mUiInfoListView = new MUiInfoListView(this, style);
        View uiInfoView = mUiInfoListView.getmUiInfoListView();

        mUiInfoListView.setmTitle("Test SetName Method");
        mInfoListContainer.addView(uiInfoView);
        mInventoryContainer.addView(inventoryView.getmInventoryView());
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // TODO Auto-generated method stub
        mInfoListContainer = (LinearLayout) findViewById(R.id.scrollview_container);
        mInventoryContainer = (LinearLayout) findViewById(R.id.ll_inventory_container);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // TODO Auto-generated method stub
        List<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < 3; i++) {
            ArrayList<String> temp = new ArrayList<String>();
            for (int j = 0; j < 3; j++) {
                temp.add(i + "组" + j + "号");
            }
            data.add(temp);
        }
        inventoryView = new InventoryView(this, data, true, true);
        inventoryView.setmTextSize(12);
        inventoryView.setmTextColorNo3(R.color.demo_title5);
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
