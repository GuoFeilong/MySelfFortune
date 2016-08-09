package com.guofeilong.fortune.ui;


import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.ui.view.DragRelativeLayout;
import com.guofeilong.fortune.ui.view.DragRelativeLayout.IDragImageViewOnclickListener;
import com.guofeilong.fortune.ui.view.SupportZoomWebView;
import com.guofeilong.fortune.utils.T;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DragRelativAndZoomWebviewActivity extends Activity implements OnClickListener {
	private DragRelativeLayout mCallUsView;
	private DragRelativeLayout email;
	private DragRelativeLayout liveChat;
	private DragRelativeLayout requestCall;
	private RelativeLayout mFaqViewLayout, mViewBottomLayout, mViewFaqTopTitle;
	private ProgressBar mProgressBar;
	private String mUrl;
	private SupportZoomWebView mWebView;
	private int mColor;
	private String mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactus);
		mTitle = getMyTitleAndColor();
		initNavigation();
		initView();
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

	private void initView() {
		RelativeLayout targetImageView = (RelativeLayout) findViewById(R.id.iv_target);
		mCallUsView = (DragRelativeLayout) findViewById(R.id.drag_call_us);
		email = (DragRelativeLayout) findViewById(R.id.drag_email);
		liveChat = (DragRelativeLayout) findViewById(R.id.drag_live_chat);
		requestCall = (DragRelativeLayout) findViewById(R.id.drag_request_call);
		mFaqViewLayout = (RelativeLayout) findViewById(R.id.rl_faq_view);
		mViewBottomLayout = (RelativeLayout) findViewById(R.id.rl_bottom_view);
		mViewFaqTopTitle = (RelativeLayout) findViewById(R.id.include_commond_title_1);
		mWebView = (SupportZoomWebView) findViewById(R.id.webview);
		mUrl = "https://www.baidu.com/";
		mUrl = "http://www.csdn.net/";
		mProgressBar = (ProgressBar) findViewById(R.id.progress_loading);
		TextView tvNameFAQ = (TextView) findViewById(R.id.commond_textview_title_name0);
		tvNameFAQ.setText("  " + getString(R.string.contact_us_faq));
		TextView tvQuestionMark = (TextView) findViewById(R.id.tv_question_mark);
		tvQuestionMark.setText("?");
		mViewFaqTopTitle.setOnClickListener(this);
		mViewBottomLayout.setOnClickListener(this);
		mCallUsView.setTargetView(targetImageView);
		email.setTargetView(targetImageView);
		liveChat.setTargetView(targetImageView);
		requestCall.setTargetView(targetImageView);
		mCallUsView.setIOnclickListener(new IDragImageViewOnclickListener() {

			@Override
			public void onDragImageViewOnclickListener(View v) {
				mCallUsView.animToTarget();
			}

			@Override
			public void onDragImageViewMoveToTarget(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.contact_us_phoneNo)));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		email.setIOnclickListener(new IDragImageViewOnclickListener() {

			@Override
			public void onDragImageViewOnclickListener(View v) {
				email.animToTarget();
			}

			@Override
			public void onDragImageViewMoveToTarget(View v) {
				Intent it = new Intent(Intent.ACTION_SEND);
				it.putExtra(Intent.EXTRA_EMAIL, "me@abc.com");
				it.putExtra(Intent.EXTRA_TEXT, "The email body text");
				it.setType("text/plain");
				startActivity(Intent.createChooser(it, "Choose Email Client"));
			}
		});
		liveChat.setIOnclickListener(new IDragImageViewOnclickListener() {

			@Override
			public void onDragImageViewOnclickListener(View v) {
				liveChat.animToTarget();
			}

			@Override
			public void onDragImageViewMoveToTarget(View v) {
				T.show(DragRelativAndZoomWebviewActivity.this, "liveChat to target ", 0);
			}
		});
		requestCall.setIOnclickListener(new IDragImageViewOnclickListener() {

			@Override
			public void onDragImageViewOnclickListener(View v) {
				requestCall.animToTarget();
			}

			@Override
			public void onDragImageViewMoveToTarget(View v) {
				T.show(DragRelativAndZoomWebviewActivity.this, "requestCall to target ", 0);
			}
		});

		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setBuiltInZoomControls(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setAppCacheEnabled(false);
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mProgressBar.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mProgressBar.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				mProgressBar.setVisibility(View.GONE);
				;
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float OldX1 = 0;
		float OldY1 = 0;
		float OldX2 = 0;
		float OldY2 = 0;
		float NewX1;
		float NewY1;
		float NewX2;
		float NewY2;

		switch (event.getAction()) {
		case MotionEvent.ACTION_POINTER_2_DOWN:
			if (event.getPointerCount() == 2) {
				OldX1 = event.getX(0);
				OldY1 = event.getY(0);
				OldX2 = event.getX(1);
				OldY2 = event.getY(1);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getPointerCount() == 2) {
				if (OldX1 == -1 && OldX2 == -1)
					break;
				NewX1 = event.getX(0);
				NewY1 = event.getY(0);
				NewX2 = event.getX(1);
				NewY2 = event.getY(1);
				float disOld = (float) Math.sqrt((Math.pow(OldX2 - OldX1, 2) + Math.pow(OldY2 - OldY1, 2)));
				float disNew = (float) Math.sqrt((Math.pow(NewX2 - NewX1, 2) + Math.pow(NewY2 - NewY1, 2)));
				if (disOld - disNew >= 25) {
					// 缩小
					mWebView.zoomOut();
					mWebView.loadUrl("javascript:mapScale=1;");
				} else if (disNew - disOld >= 25) {
					// 放大
					mWebView.zoomIn();
					mWebView.loadUrl("javascript:mapScale=-1;");
				}
				OldX1 = NewX1;
				OldX2 = NewX2;
				OldY1 = NewY1;
				OldY2 = NewY2;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (event.getPointerCount() < 2) {
				OldX1 = -1;
				OldY1 = -1;
				OldX2 = -1;
				OldY2 = -1;
			}
			break;
		}
		return false;

	}

	@Override
	protected void onResume() {
		requestCall.moveToStart();
		liveChat.moveToStart();
		email.moveToStart();
		mCallUsView.moveToStart();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commond_imagebutton_title_leftbutton:
			finish();
			break;
		case R.id.include_commond_title_1:
			goneFQAView();
			break;
		case R.id.rl_bottom_view:
			loadFQAView();
			break;

		default:
			break;
		}
	}

	private void goneFQAView() {
		Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.exit_bottom);
		mFaqViewLayout.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mFaqViewLayout.setVisibility(View.GONE);

			}
		});

	}

	private void loadFQAView() {
		mWebView.loadUrl(mUrl);
		mFaqViewLayout.setVisibility(View.VISIBLE);
		mFaqViewLayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in_from_bottom));
	}
}
