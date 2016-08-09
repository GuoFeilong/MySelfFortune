package com.guofeilong.fortune.ui;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

import com.guofeilong.fortune.R;
import com.guofeilong.fortune.UserConfig;
import com.guofeilong.fortune.business.OnLoginListener;
import com.guofeilong.fortune.business.UserInfo;
import com.guofeilong.fortune.utils.ImageDownloader;
import com.guofeilong.fortune.utils.T;
import com.guofeilong.fortune.utils.ViewUtils;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MyDialogActivity extends BaseActivity implements OnClickListener, PlatformActionListener {
	/** 图片名字 */
	private static final String PICTURE_NAME = "userIcon.jpg";
	private static final int MSG_SMSSDK_CALLBACK = 1;
	private static final int MSG_AUTH_CANCEL = 2;
	private static final int MSG_AUTH_ERROR = 3;
	private static final int MSG_AUTH_COMPLETE = 4;
	private OnLoginListener signupListener;
	private Platform platform;
	private String picturePath;
	private UserInfo userInfo = new UserInfo();
	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = "73bb928c1542";
	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "24a4b64a6f780dfe5854985dd6cb2d94";
	
	private ImageView mClose;
	private TextView mWXOH2;
	private TextView mSinaOH2;
	private TextView mQQOH2;
	private ImageDownloader downloader;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case MSG_AUTH_CANCEL: {
				// 取消授权
				T.show(MyDialogActivity.this, "取消授权", 0);
			}
				break;
			case MSG_AUTH_ERROR: {
				// 授权失败
				T.show(MyDialogActivity.this, "授权失败", 0);
			}
				break;
			case MSG_AUTH_COMPLETE: {
				// 授权成功
				T.show(MyDialogActivity.this, "授权成功", 0);
				Object[] objs = (Object[]) msg.obj;
				String platformString = (String) objs[0];
				platform = ShareSDK.getPlatform(platformString);

				HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
				if (signupListener != null && signupListener.onSignin(platformString, res)) {
					// TODO 授权成功,获取用户资料
					getTplUserData();
				}
			}
				break;

			default:
				break;

			}
		}

	};
	/**
	 * 获取第三方用户平台资料
	 * 
	 * @param platform2
	 */
	protected void getTplUserData() {

		String gender = "";
		if (platform != null) {
			gender = platform.getDb().getUserGender();
			if (gender.equals("m")) {
				userInfo.setUserGender(UserInfo.Gender.BOY);
				gender = "男";
			} else {
				userInfo.setUserGender(UserInfo.Gender.GIRL);
				gender = "女";
			}

			userInfo.setUserIcon(platform.getDb().getUserIcon());
			userInfo.setUserName(platform.getDb().getUserName());
			userInfo.setUserNote(platform.getDb().getUserId());
		}
//		mDemoTitle.setText(userInfo.getUserName());
		// 加载头像
		if (!TextUtils.isEmpty(userInfo.getUserIcon())) {
			loadIcon();
		}
		// 初始化照片保存地址
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String thumPicture = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + this.getPackageName() + "/download";
			File pictureParent = new File(thumPicture);
			File pictureFile = new File(pictureParent, PICTURE_NAME);

			if (!pictureParent.exists()) {
				pictureParent.mkdirs();
			}
			try {
				if (!pictureFile.exists()) {
					pictureFile.createNewFile();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			picturePath = pictureFile.getAbsolutePath();
			Log.e("picturePath ==>>", picturePath);
		} else {
			Log.e("change user icon ==>>", "there is not sdcard!");
		}

	}
	/**
	 * 加载头像
	 */
	public void loadIcon() {
		final String imageUrl = platform.getDb().getUserIcon();
//		downloader.download(imageUrl, mUserPortrait);

	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_usercenter_shared);
		initData();
		initView();
		initEvent();
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		mClose.setOnClickListener(this);
		mWXOH2.setOnClickListener(this);
		mSinaOH2.setOnClickListener(this);
		mQQOH2.setOnClickListener(this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mClose = (ImageView) findViewById(R.id.ibt_dialog_close);
		mWXOH2 = (TextView) findViewById(R.id.tv_wx_oth2);
		mSinaOH2 = (TextView) findViewById(R.id.tv_sina_oth2);
		mQQOH2 = (TextView) findViewById(R.id.tv_qq_oth2);

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		ShareSDK.initSDK(this);
		downloader = new ImageDownloader(null);
	}

	// 执行授权,获取用户信息
	private void authorize(Platform plat) {
		plat.setPlatformActionListener(this);
		// 关闭SSO授权
		plat.SSOSetting(true);
		plat.showUser(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibt_dialog_close:
			finish();
			break;
		case R.id.tv_wx_oth2:
			// wxAuthorization();
			// sendReqWX(this, null, false);

			// 微信登录
			// 测试时，需要打包签名；sample测试时，用项目里面的demokey.keystore
			// 打包签名apk,然后才能产生微信的登录
			Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
			authorize(wechat);
			break;
		case R.id.tv_sina_oth2:
			// 新浪微博
			Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
			authorize(sina);
			break;
		case R.id.tv_qq_oth2:
			Platform qzone = ShareSDK.getPlatform(QZone.NAME);
			authorize(qzone);
			break;
		default:
			break;
		}
	}

	@Override
	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			mHandler.sendEmptyMessage(MSG_AUTH_CANCEL);
		}
	}

	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			msg.obj = new Object[] { platform.getName(), res };
			mHandler.sendMessage(msg);
		}
	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			mHandler.sendEmptyMessage(MSG_AUTH_ERROR);
		}
	}
}
