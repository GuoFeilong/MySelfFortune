package com.guofeilong.fortune.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TimeZoneActivity extends BaseActivity implements OnClickListener, AdapterView.OnItemClickListener {
    private int mColor;
    private String mTitle;

    private ListView mTimeZone;
    private TimeAdapter timeAdapter;

    private List<TimeModle> mData;
    private LinearLayout mAddFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_time_zone);
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
        mAddFooter = (LinearLayout) getLayoutInflater().inflate(R.layout.item_time_zone_footer, null);
        mTimeZone.addFooterView(mAddFooter);
        mTimeZone.setAdapter(timeAdapter);
        mTimeZone.setOnItemClickListener(this);

    }

    /**
     * 初始化控件
     */
    private void initView() {
        // TODO: 15/9/9 add  footer
        mTimeZone = (ListView) findViewById(R.id.lv_time_zone);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        // TODO Auto-generated method stub
        mData = new ArrayList<>();

        TimeModle temp = new TimeModle("网易", "高级经理", "2015年-至今");
        mData.add(temp);
        temp = new TimeModle("百度", "Android开发", "2014年-15");
        mData.add(temp);
        temp = new TimeModle("腾讯", "女优设计", "2013年-14");
        mData.add(temp);


        timeAdapter = new TimeAdapter();
        timeAdapter.setData(mData);


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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position==mData.size()){
            TimeModle temp = new TimeModle("阿里粑粑", "清洁工", "2011年-至今");
            mData.add(temp);
            timeAdapter.notifyDataSetChanged();
        }
    }

    class TimeAdapter extends BaseAdapter {
        private List<TimeModle> data;

        public void setData(List<TimeModle> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (holder == null) {
                holder = new Holder();
                convertView = View.inflate(TimeZoneActivity.this, R.layout.item_time_zone, null);
                holder.title1 = (TextView) convertView.findViewById(R.id.tv_title1);
                holder.title2 = (TextView) convertView.findViewById(R.id.tv_title2);
                holder.title3 = (TextView) convertView.findViewById(R.id.tv_title3);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            setData2Postion(holder, data.get(position));

            return convertView;
        }
    }

    private void setData2Postion(Holder holder, TimeModle timeModle) {
        holder.title1.setText(timeModle.getTitle1());
        holder.title2.setText(timeModle.getTitle2());
        holder.title3.setText(timeModle.getTitle3());
    }

    class Holder {
        TextView title1;
        TextView title2;
        TextView title3;
    }

    class TimeModle {
        String title1;
        String title2;
        String title3;

        public TimeModle(String title1, String title2, String title3) {
            this.title1 = title1;
            this.title2 = title2;
            this.title3 = title3;
        }

        public String getTitle1() {
            return title1;
        }

        public void setTitle1(String title1) {
            this.title1 = title1;
        }

        public String getTitle2() {
            return title2;
        }

        public void setTitle2(String title2) {
            this.title2 = title2;
        }

        public String getTitle3() {
            return title3;
        }

        public void setTitle3(String title3) {
            this.title3 = title3;
        }
    }
}
