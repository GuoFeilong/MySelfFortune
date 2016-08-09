package com.guofeilong.fortune.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.pulltorefresh.PullToRefreshBase;
import com.guofeilong.fortune.pulltorefresh.PullToRefreshScrollView;
import com.guofeilong.fortune.ui.view.RefreshableView;

public class RefreshActivity extends BaseActivity implements OnClickListener ,PullToRefreshBase.OnRefreshListener,PullToRefreshBase.OnPullEventListener{
    private int mColor;
    private String mTitle;

    private RefreshableView refreshableView;
    private View headerView;

    private PullToRefreshScrollView mPullRefreshScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_refresh);
        mTitle = getMyTitleAndColor();
//        setContentView(R.layout.test);
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
//        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
//            @Override
//            public void onRefresh() {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                refreshableView.finishRefreshing();
//            }
//        }, 0);

    }

    /**
     * 初始化控件
     */
    private void initView() {
        // TODO Auto-generated method stub
//        refreshableView = (RefreshableView) findViewById(R.id.rfv_view);
//        headerView = findViewById(R.id.include_content);

        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_scrollview);

    }


    /*
	 * 初始化下拉刷新控件
	 */
    private void initPullRefreshScrollView() {
         mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
         mPullRefreshScrollView.setOnRefreshListener(this);
         mPullRefreshScrollView.setOnPullEventListener(this);

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
    public void onPullEvent(PullToRefreshBase refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
        if (PullToRefreshBase.Mode.PULL_FROM_START.equals(direction)) {
            switch (state) {
                case REFRESHING:

                    // 获取数据
                    // if (mDemoAllListData.size() == 0) {
                    // T.show(this, "No more demo...", 0);
                    // } else {
                    // processData();
                    // mAdapter.notifyDataSetChanged();
                    // T.show(this, mDemoListDataCurrent.size() + "---", 0);
                    // }
                    // mPullRefreshScrollView.onRefreshComplete();

                    break;
                default:
                    break;
            }
        } else if (PullToRefreshBase.Mode.PULL_FROM_END.equals(direction)) {
            switch (state) {
                case REFRESHING:
                    // if (mDemoAllListData.size() == 0) {
                    // T.show(this, "No more demo...", 0);
                    // } else {
                    // processData();
                    // mAdapter.notifyDataSetChanged();
                    // T.show(this, mDemoListDataCurrent.size() + "---", 0);
                    // }
                    // mPullRefreshScrollView.onRefreshComplete();
                    // mPullRefreshScrollView.getRefreshableView().fullScroll(ScrollView.FOCUS_UP);

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

    }
}
