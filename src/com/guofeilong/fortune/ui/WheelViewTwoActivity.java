package com.guofeilong.fortune.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.wheelview2.OptionsPopupWindow;
import com.guofeilong.fortune.ui.view.wheelview2.TimePopupWindow;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WheelViewTwoActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;




	private ArrayList<String> options1Items = new ArrayList<String>();
	private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();
	private TextView tvTime, tvOptions;
	TimePopupWindow pwTime;
	OptionsPopupWindow pwOptions;


	private TextView mText;
	private TextView mTextAmi;
	private Runnable r;
	private Thread thread;
	private Handler mHander = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);


			switch (msg.what){
				case 1111:
					mTextAmi.setVisibility(View.GONE);

					break;
				default:
					mTextAmi.setTextSize(mySize);

			}

		}
	};
	private int mySize=12;
	private boolean flag;
	private long temp;


	private TextView mTest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wheelview2);
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
		TextView tvLong = (TextView) findViewById(R.id.tv_long);
		TextView tvShort = (TextView) findViewById(R.id.tv_short);

		tvLong.setText("我是长文本");
		tvShort.setText("短文");

		tvLong.setBackgroundColor(Color.RED);
		tvShort.setBackgroundColor(Color.BLUE);


		//获取长textview的字段长度
	    float longTextSize =	getTextWidth(this,tvLong.getText().toString(),tvLong.getTextSize());
		float shortTextSize=	getTextWidth(this,tvShort.getText().toString(),tvShort.getTextSize());

		// 空格的单位长度
		float unitSize=	getTextWidth(this, " ", tvLong.getTextSize());

		String shortDesc = tvShort.getText().toString();
		String newShortDesc ;

		char[]temp=	shortDesc.toCharArray();
		String befor =String.valueOf(temp[0]);
		StringBuffer buffer= new StringBuffer() ;
		for (int i = 0; i <temp.length ; i++) {
			if(i!=0){
				String tempLast = String.valueOf(temp[i]);
				buffer.append(tempLast);
			}
		}

		String last = buffer.toString();

		float spaec=	(longTextSize-shortTextSize)/unitSize;
		StringBuffer bufferSpace = new StringBuffer();
		for (int j = 0; j < spaec; j++) {
			bufferSpace.append(" ");
		}




		newShortDesc = new StringBuffer().append(befor).append(bufferSpace.toString()).append(last).toString();


		// 改变ui
		tvShort.setText(newShortDesc);

	}

	public float getTextWidth(Context context, String text, float textSize){
		TextPaint paint = new TextPaint();
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		paint.setTextSize(scaledDensity * textSize);
		return paint.measureText(text);
	}


	/**
	 * 初始化数据
	 */
	private void initData() {
		// TODO Auto-generated method stub


		mText = (TextView) findViewById(R.id.tv_text);
		mTextAmi = (TextView) findViewById(R.id.tv_animaiton);
		mTest = (TextView) findViewById(R.id.tv_test);
		mTest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 测试缩放动画,字体是否能放大
				ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,2.0f,1.0f,2.0f);
				scaleAnimation.setDuration(1000);
				scaleAnimation.setRepeatCount(1);

				mTest.startAnimation(scaleAnimation);
			}
		});



		mText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mText.setBackgroundColor(Color.GRAY);
				mText.setText("已领取");
				mText.setClickable(false);

				// 位移 放大 alpha动画
				AnimationSet aSet = new AnimationSet(true);
				TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF, -180);

				AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0.5f);
				aSet.addAnimation(ta);
				aSet.addAnimation(alphaAnimation);


//                ScaleAnimation sa = new ScaleAnimation(1.0f,2.0f,1.0f,2.0f);
//                aSet.addAnimation(sa);

//                aSet.setFillAfter(true);
				aSet.setRepeatCount(1);
				aSet.setDuration(500);
				mTextAmi.startAnimation(aSet);


				flag = true;
				r = new Runnable() {
					@Override
					public void run() {
						while (flag){
							mySize+=2;
							Message msg=    mHander.obtainMessage();
							mHander.sendMessage(msg);

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							temp+=100;

							if(temp>=500){
								flag =false;
								Message gMsg=     mHander.obtainMessage();
								gMsg.what = 1111;
								mHander.sendMessage(gMsg);
							}
						}
					}
				};




				thread = new Thread(r);
				aSet.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
						thread.start();
					}

					@Override
					public void onAnimationEnd(Animation animation) {

						thread.interrupt();
						mTextAmi.setVisibility(View.GONE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});

			}
		});


		tvTime = (TextView) findViewById(R.id.tvTime);
		tvOptions = (TextView) findViewById(R.id.tvOptions);
		//时间选择器
		pwTime = new TimePopupWindow(this, TimePopupWindow.Type.ALL);
		//时间选择后回调
		pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				tvTime.setText(getTime(date));
			}
		});
		//弹出时间选择器
		tvTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwTime.showAtLocation(tvTime, Gravity.BOTTOM, 0, 0, new Date());
			}
		});

		//选项选择器
		pwOptions = new OptionsPopupWindow(this);

		//选项1
		options1Items.add("广东");
		options1Items.add("湖南");

		//选项2
		ArrayList<String> options2Items_01 = new ArrayList<String>();
		options2Items_01.add("广州");
		options2Items_01.add("佛山");
		options2Items_01.add("东莞");
		ArrayList<String> options2Items_02 = new ArrayList<String>();
		options2Items_02.add("长沙");
		options2Items_02.add("岳阳");
		options2Items.add(options2Items_01);
		options2Items.add(options2Items_02);

		//选项3
		ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> options3Items_02 = new ArrayList<ArrayList<String>>();
		ArrayList<String> options3Items_01_01 = new ArrayList<String>();
		options3Items_01_01.add("白云");
		options3Items_01_01.add("天河");
		options3Items_01_01.add("海珠");
		options3Items_01_01.add("越秀");
		options3Items_01.add(options3Items_01_01);
		ArrayList<String> options3Items_01_02 = new ArrayList<String>();
		options3Items_01_02.add("南海");
		options3Items_01.add(options3Items_01_02);
		ArrayList<String> options3Items_01_03 = new ArrayList<String>();
		options3Items_01_03.add("常平");
		options3Items_01_03.add("虎门");
		options3Items_01.add(options3Items_01_03);

		ArrayList<String> options3Items_02_01 = new ArrayList<String>();
		options3Items_02_01.add("长沙1");
		options3Items_02.add(options3Items_02_01);
		ArrayList<String> options3Items_02_02 = new ArrayList<String>();
		options3Items_02_02.add("岳1");
		options3Items_02.add(options3Items_02_02);

		options3Items.add(options3Items_01);
		options3Items.add(options3Items_02);

		//三级联动效果
		pwOptions.setPicker(options1Items, options2Items, options3Items, true);
		//设置选择的三级单位
		pwOptions.setLabels("省", "市", "区");
		//设置默认选中的三级项目
		pwOptions.setSelectOptions(0, 0, 0);
		//监听确定选择按钮
		pwOptions.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				//返回的分别是三个级别的选中位置
				String tx = options1Items.get(options1)
						+ options2Items.get(options1).get(option2)
						+ options3Items.get(options1).get(option2).get(options3);
				tvOptions.setText(tx);
			}
		});
		//点击弹出选项选择器
		tvOptions.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pwOptions.showAtLocation(tvTime, Gravity.BOTTOM, 0, 0);
			}
		});

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

	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}
}
