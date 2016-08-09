package com.guofeilong.fortune.ui.view.inventory;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.guofeilong.fortune.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsion on 15/8/27.
 */
public class InventoryView {
    private Context mContext;
    private LayoutInflater mLayoutInfflater;
    private ListView mInventoryView;
    /**
     * 是否显示item下划线
     */
    private boolean isShowUnderline;
    /**
     * 字体大小
     */
    private int mTextSize;
    /**
     * 一号区域字体颜色
     */
    private int mTextColorNo1 = R.color.demo_title1;
    /**
     * 二号区域字体颜色
     */
    private int mTextColorNo2 = R.color.demo_title2;
    /**
     * 三号区域字体颜色
     */
    private int mTextColorNo3 = R.color.demo_title3;
    /**
     * 一号区域位置属性
     */
    private int mTextGravityNo1 = Gravity.CENTER_VERTICAL | Gravity.LEFT;
    /**
     * 二号区域位置属性
     */
    private int mTextGravityNo2 = Gravity.CENTER;
    /**
     * 三号区域位置属性
     */
    private int mTextGravityNo3 = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
    /**
     * 是否显示二号区域文本
     */
    private boolean isShowNo2Text;


    /**
     * 数据
     */
    private List<ArrayList<String>> mInventoryData;
    private InventoryAdapter inventoryAdapter;

    public InventoryView(Context mContext, List<ArrayList<String>> mInventoryData, boolean isShowNo2Text, boolean isShowUnderline) {
        this.mContext = mContext;
        this.mInventoryData = mInventoryData;
        this.isShowUnderline = isShowUnderline;
        this.isShowNo2Text=isShowNo2Text;

        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, mContext.getResources().getDisplayMetrics());

        mLayoutInfflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInventoryView = new ListView(mContext);
        mInventoryView.setSelector(R.color.transparent);
        mInventoryView.setDivider(null);
        inventoryAdapter = new InventoryAdapter(mInventoryData, mContext, mLayoutInfflater, isShowUnderline, isShowNo2Text);
        mInventoryView.setAdapter(inventoryAdapter);
    }

    /**
     * 获取评价列表组件
     *
     * @return
     */
    private View getInventoryView() {
        return mInventoryView;
    }

    public void setIsShowUnderline(boolean isShowUnderline) {
        this.isShowUnderline = isShowUnderline;
    }

    public void setmTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, mContext.getResources().getDisplayMetrics());
    }

    public void setmTextColorNo1(int mTextColorNo1) {
        this.mTextColorNo1 = mTextColorNo1;
    }

    public void setmTextColorNo2(int mTextColorNo2) {
        this.mTextColorNo2 = mTextColorNo2;
    }

    public void setmTextColorNo3(int mTextColorNo3) {
        this.mTextColorNo3 = mTextColorNo3;
    }

    public void setmTextGravityNo1(int mTextGravityNo1) {
        this.mTextGravityNo1 = mTextGravityNo1;
    }

    public void setmTextGravityNo2(int mTextGravityNo2) {
        this.mTextGravityNo2 = mTextGravityNo2;
    }

    public void setmTextGravityNo3(int mTextGravityNo3) {
        this.mTextGravityNo3 = mTextGravityNo3;
    }

    public void setIsShowNo2Text(boolean isShowNo2Text) {
        this.isShowNo2Text = isShowNo2Text;
    }

    public void setmInventoryData(List<ArrayList<String>> mInventoryData) {
        this.mInventoryData = mInventoryData;
    }

    public ListView getmInventoryView() {
        return mInventoryView;
    }

    public void notifyList() {
        if (inventoryAdapter != null) {
            inventoryAdapter.notifyDataSetChanged();
        }
    }

    class InventoryHolder {
        LinearLayout textContainer;


        TextView textView1 = getTextView(mTextSize, mTextColorNo1, mTextGravityNo1);
        TextView textView2 = getTextView(mTextSize, mTextColorNo2, mTextGravityNo2);
        TextView textView3 = getTextView(mTextSize, mTextColorNo3, mTextGravityNo3);


        ImageView underLine;

    }

    class InventoryAdapter extends BaseAdapter {
        private List<ArrayList<String>> inventoryData;
        private Context context;
        private LayoutInflater inflater;
        private boolean showUnderLine;
        private boolean isShowNo2Text;

        public InventoryAdapter(List<ArrayList<String>> inventoryData, Context context, LayoutInflater inflater, boolean showUnderLine, boolean isShowNo2Text) {
            this.inventoryData = inventoryData;
            this.context = context;
            this.inflater = inflater;
            this.showUnderLine = showUnderLine;
            this.isShowNo2Text = isShowNo2Text;
        }

        @Override
        public int getCount() {
            return inventoryData.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            InventoryHolder holder = null;
            if (holder == null) {
                holder = new InventoryHolder();
                view = inflater.inflate(R.layout.inventory_item_inventory, null);
                holder.textContainer = (LinearLayout) view.findViewById(R.id.ll_container);
                holder.textView1 =getTextView(mTextSize, mTextColorNo1, mTextGravityNo1);
                holder.textView2 =getTextView(mTextSize, mTextColorNo2, mTextGravityNo2);
                holder.textView3 =getTextView(mTextSize, mTextColorNo3, mTextGravityNo3);

                holder.textContainer.addView(holder.textView1);
                holder.textContainer.addView(holder.textView2);
                holder.textContainer.addView(holder.textView3);

                holder.underLine = (ImageView) view.findViewById(R.id.iv_inventory_line);
                view.setTag(holder);
            } else {
                holder = (InventoryHolder) view.getTag();
            }

            if (isShowNo2Text) {
                holder.textView2.setVisibility(View.VISIBLE);
            } else {
                holder.textView2.setVisibility(View.INVISIBLE);
            }

            if (isShowUnderline) {
                holder.underLine.setVisibility(View.VISIBLE);
            } else {
                holder.underLine.setVisibility(View.INVISIBLE);
            }

            setDate2CurrentPosition(holder, inventoryData.get(i));


            return view;
        }
    }

    private void setDate2CurrentPosition(InventoryHolder holder, ArrayList<String> strings) {

        holder.textView1.setText(strings.get(0));
        holder.textView2.setText(strings.get(1));
        holder.textView3.setText(strings.get(2));
    }

    private TextView getTextView(int textSize, int textColor, int textGravity) {
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        layoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams);
        textView.setTextColor(mContext.getResources().getColor(textColor));
        textView.setSingleLine();
        textView.setGravity(textGravity);
        textView.setTextSize(textSize);
        return textView;
    }
}
