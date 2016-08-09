package com.guofeilong.fortune.wxapi;

import com.guofeilong.fortune.R;
import com.guofeilong.fortune.utils.T;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler, OnClickListener {
	private IWXAPI api;
	private String mWxAppId;
	private RelativeLayout mContainer;
	private ImageView mClose;
	private TextView mWx_oth2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_usercenter_shared);
		mWxAppId = "wxappid1234";
		api = WXAPIFactory.createWXAPI(this, mWxAppId, true);
		api.registerApp(mWxAppId);
		api.handleIntent(getIntent(), this);
		initAnimation();
	}

	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		mContainer = (RelativeLayout) findViewById(R.id.rl_usercenter_dialog);
		mContainer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_from_leftbottom));
		mWx_oth2 = (TextView) findViewById(R.id.tv_wx_oth2);
		mWx_oth2.setOnClickListener(this);
		mClose = (ImageView) findViewById(R.id.ibt_dialog_close);
		mClose.setOnClickListener(this);

	}

	@Override
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onResp(BaseResp arg0) {
		switch (arg0.errCode) {
		case BaseResp.ErrCode.ERR_OK:
//			Toast.makeText(this, "分享成功", 0).show();
//			finish();
			// 第三方帐号登录返回码
			String code = ((SendAuth.Resp)arg0).code;
			
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			Toast.makeText(this, "用户取消分享", 0).show();
			finish();
			break;
		
		default:
			Toast.makeText(this, "分享失败", 0).show();
			finish();
			break;
		}
	}

	/**
	 * 授权登录
	 */
	private void wxAuthorization() {
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "wechat_sdk_demo_test";
		api.sendReq(req);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibt_dialog_close:
			mContainer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_to_rightbottom));
			finish();
			break;
		case R.id.tv_wx_oth2:
			T.show(this, "sfsffs", 0);
			wxAuthorization();
			break;
		default:
			break;
		}
	}

}
