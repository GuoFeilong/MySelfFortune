package com.guofeilong.fortune.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.CustomEditTextView;

import java.util.ArrayList;


public class QQaiteActivity extends BaseActivity implements OnClickListener, AdapterView.OnItemClickListener {
    private static final String MY_AITE = "@";
    private int mColor;
    private String mTitle;
    private CustomEditTextView mCustomEditTextView;
    private CustomEditTextView.OnTextChangeListener onTextChangeListener;
    private Dialog mDialog;
    private ListView mQQMembers;
    private ArrayList<String> mQQNames;
    private MyAdapter adapter;
    private String mCurrentNickName;
    private Bitmap mCurrentBitmap;
    private ImageView mTest;
    private EditText test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qq_aite);
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
        mCustomEditTextView = (CustomEditTextView) findViewById(R.id.cet_qqaite);
        mCustomEditTextView.setTextChangeListener(onTextChangeListener);

        mTest = (ImageView) findViewById(R.id.iv_bitmap);
        test = (EditText) findViewById(R.id.et_test);
        test.setText("No 代码 say 个 jb");
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // TODO Auto-generated method stub
        onTextChangeListener = new CustomEditTextView.OnTextChangeListener() {
            @Override
            public void myTextChanged(String content) {
                if (MY_AITE.equals(content)) {
                    // TODO: 15/9/28 弹出选择框
                    showCustomDialog();
                }

//                if (content.contains(MY_AITE)) {
//                    // TODO: 15/9/28 弹出选择框
//                    showCustomDialog();
//                }
            }
        };

        mQQNames = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            mQQNames.add("小三" + i + "号");
        }

        adapter = new MyAdapter();
        adapter.setListData(mQQNames);
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
            case R.id.ibt_dialog_close:
                mDialog.dismiss();
                mCustomEditTextView.setText("");
                break;
            default:
                break;
        }

    }


    private void showCustomDialog() {

        if (mDialog == null) {
            mDialog = new Dialog(this, R.style.myDialogTheme);
            mDialog.setContentView(R.layout.dialog_qq_aite);
        }

        mDialog.setCancelable(false);
        RelativeLayout rl = (RelativeLayout) mDialog.findViewById(R.id.rl_container);
        ImageView ibt_dialog_close = (ImageView) mDialog.findViewById(R.id.ibt_dialog_close);
        ibt_dialog_close.setOnClickListener(this);

        mQQMembers = (ListView) rl.findViewById(R.id.lv_qq_members);
        initQQMembers(mQQMembers);

        Window window = mDialog.getWindow();
        window.setWindowAnimations(R.style.dialogAnimationStyle);

        mDialog.show();

        LayoutAnimationController controller = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.custom_item_anim));
        controller.setDelay(0.8f);
        rl.setLayoutAnimation(controller);
    }


    private void initQQMembers(ListView mQQMembers) {
        mQQMembers.setAdapter(adapter);
        mQQMembers.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurrentNickName = mQQNames.get(position);
        mCurrentBitmap = drawTextToBitmap(QQaiteActivity.this, R.drawable.bg_qq_at, MY_AITE + mCurrentNickName);
        mTest.setBackgroundDrawable(new BitmapDrawable(mCurrentBitmap));

        mDialog.dismiss();
        setBitmap2EditText(mCurrentBitmap);
    }

    /**
     * 设置图片到编辑框
     *
     * @param mCurrentBitmap
     */
    private void setBitmap2EditText(Bitmap mCurrentBitmap) {
        SpannableStringBuilder sBuilder = new SpannableStringBuilder(MY_AITE + mCurrentNickName);
        Drawable drawable = new BitmapDrawable(mCurrentBitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        sBuilder.setSpan(imageSpan, 0, (MY_AITE + mCurrentNickName).length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mCustomEditTextView.setText(sBuilder);
        mCustomEditTextView.setSelection(mCustomEditTextView.length());// 设置Edittext中光标在最后面显示


    }





    class MyAdapter extends BaseAdapter {
        private ArrayList<String> listData;

        public void setListData(ArrayList<String> listData) {
            this.listData = listData;
        }

        @Override
        public int getCount() {
            return listData.size();
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
            if (convertView == null) {
                holder = new Holder();
                convertView = View.inflate(QQaiteActivity.this, R.layout.item_qq_aite, null);
                holder.nickName = (TextView) convertView.findViewById(R.id.tv_qq_aite);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.nickName.setText(listData.get(position));

            return convertView;
        }
    }

    class Holder {
        TextView nickName;
    }


    /**
     * 创建带文字的bitmap
     *
     * @param mContext
     * @param resourceId
     * @param mText
     * @return
     */
    public Bitmap drawTextToBitmap(Context mContext, int resourceId, String mText) {
        try {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);


            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
            if (bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.rgb(110, 110, 110));
            paint.setTextSize((int) (15 * scale));
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            int x = (bitmap.getWidth() - bounds.width()) / 6;
            int y = (bitmap.getHeight() + bounds.height()) / 5;
            canvas.drawText(mText, x * scale, y * scale, paint);
            return bitmap;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }

    }
}
