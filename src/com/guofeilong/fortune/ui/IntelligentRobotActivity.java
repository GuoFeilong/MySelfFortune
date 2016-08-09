package com.guofeilong.fortune.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.business.ChartMessage;
import com.guofeilong.fortune.business.ChartMessage.MyType;
import com.guofeilong.fortune.business.Result;
import com.guofeilong.fortune.utils.T;
import com.guofeilong.fortune.utils.Tools;
import com.guofeilong.fortune.utils.TuLingNeT;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IntelligentRobotActivity extends BaseActivity implements OnClickListener {
	private int mColor;
	private String mTitle;
	private ListView mChartList;
	private EditText mChartDesc;
	private Button mChartSend;
	private MyChartMessageAdapter myChartMessageAdapter;
	private List<ChartMessage> mChartMesgs;
	private RequestQueue volleyRequestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_intelligent_robot);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initData();
		initVolley();
		initView();
		initEvent();
	}

	/**
	 * 初始化volley
	 */
	private void initVolley() {
		volleyRequestQueue = Volley.newRequestQueue(this);

	}

	/**
	 * Close the soft keyboard click event
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Tools.closeSoftKeyboard(this);
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		if (mChartMesgs != null) {
			if (myChartMessageAdapter != null) {
				myChartMessageAdapter.setChartMsgs(mChartMesgs);
				mChartList.setAdapter(myChartMessageAdapter);
			}
		}
		mChartSend.setOnClickListener(this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mChartList = (ListView) findViewById(R.id.lv_robot_chart);
		mChartDesc = (EditText) findViewById(R.id.et_input_msg);
		mChartSend = (Button) findViewById(R.id.btn_send);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		mChartMesgs = new ArrayList<ChartMessage>();
		ChartMessage chartMessage = new ChartMessage();
		chartMessage.setDate(new Date());
		chartMessage.setMsg("你好,我是晴子有什么可以帮您!");
		chartMessage.setType(MyType.INCOMING);
		mChartMesgs.add(chartMessage);

		// chartMessage = new ChartMessage();
		// chartMessage.setDate(new Date());
		// chartMessage.setMsg("讲个笑话");
		// chartMessage.setType(MyType.OUTCOMING);
		// mChartMesgs.add(chartMessage);

		myChartMessageAdapter = new MyChartMessageAdapter();
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
		case R.id.btn_send:
			// TODO 发送消息把自己的消息封装成 chartmessage,等服务器回应后把服务器的消息加入chartmessage
			String mySent = mChartDesc.getText().toString();
			if (TextUtils.isEmpty(mySent)) {
				T.show(IntelligentRobotActivity.this, "发送的内容不能为空", 0);
			} else {
				ChartMessage message = new ChartMessage();
				message.setDate(new Date());
				message.setMsg(mySent);
				message.setType(MyType.OUTCOMING);
				mChartMesgs.add(message);
				myChartMessageAdapter.notifyDataSetChanged();

				String url = TuLingNeT.doGet(mySent);
				StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						ChartMessage chartMessage = new ChartMessage();
						try {
							Result result = new Result();
							Gson gson = new Gson();

							result = gson.fromJson(response, Result.class);
							chartMessage.setDate(new Date());
							chartMessage.setMsg(result.getText());
							chartMessage.setType(MyType.INCOMING);

							mChartMesgs.add(chartMessage);
							myChartMessageAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							chartMessage.setMsg("服务器忙,请重试....");
							mChartMesgs.add(chartMessage);
							myChartMessageAdapter.notifyDataSetChanged();
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});

				volleyRequestQueue.add(stringRequest);
				mChartDesc.setText("");
			}

			break;
		default:
			break;
		}

	}

	/**
	 * 聊天界面的适配器
	 * 
	 * @author guofl
	 * 
	 */
	class MyChartMessageAdapter extends BaseAdapter {
		private List<ChartMessage> chartMsgs;

		public void setChartMsgs(List<ChartMessage> chartMsgs) {
			this.chartMsgs = chartMsgs;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return chartMsgs.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChartMessage currentMes = chartMsgs.get(position);
			MyType currentType = currentMes.getType();
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				switch (currentType) {
				case INCOMING:
					convertView = getLayoutInflater().inflate(R.layout.item_robort_inmsg, null);
					holder.chartTime = (TextView) convertView.findViewById(R.id.tv_chart_time);
					holder.chartDesc = (TextView) convertView.findViewById(R.id.tv_chart_in_msg);
					break;
				case OUTCOMING:
					convertView = getLayoutInflater().inflate(R.layout.item_robort_outmsg, null);
					holder.chartTime = (TextView) convertView.findViewById(R.id.tv_chart_time_out);
					holder.chartDesc = (TextView) convertView.findViewById(R.id.tv_chart_out_msg);

					break;

				}

				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			setData2Postion(holder, position, currentMes);
			return convertView;
		}

	}

	class Holder {
		TextView chartDesc;
		TextView chartTime;
	}

	/**
	 * 设置数据到当前item
	 * 
	 * @param holder
	 * @param position
	 * @param currentMes
	 */
	public void setData2Postion(Holder holder, int position, ChartMessage currentMes) {
		holder.chartDesc.setText(currentMes.getMsg());
		Date date = currentMes.getDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = sdf.format(date);
		holder.chartTime.setText(format);
	}
}
