package com.guofeilong.fortune.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.guofeilong.fortune.AppConstants;
import com.guofeilong.fortune.R;
import com.guofeilong.fortune.UserConfig;
import com.guofeilong.fortune.business.OnLoginListener;
import com.guofeilong.fortune.business.UserInfo;
import com.guofeilong.fortune.pulltorefresh.PullToRefreshBase;
import com.guofeilong.fortune.pulltorefresh.PullToRefreshBase.Mode;
import com.guofeilong.fortune.pulltorefresh.PullToRefreshBase.OnPullEventListener;
import com.guofeilong.fortune.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.guofeilong.fortune.pulltorefresh.PullToRefreshBase.State;
import com.guofeilong.fortune.ui.view.CircleImageView;
import com.guofeilong.fortune.utils.ImageDownloader;
import com.guofeilong.fortune.utils.Settings;
import com.guofeilong.fortune.utils.T;
import com.guofeilong.fortune.utils.Tools;
import com.guofeilong.fortune.utils.ViewUtils;
import com.guofeilong.fortune.utils.ViewUtils.AutoDismissOnClickListener;
import com.guofeilong.fortune.utils.ViewUtils.SelectMenuPopupWindow;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

public class MainActivity extends BaseActivity implements OnRefreshListener<ScrollView>, OnPullEventListener<ScrollView>, OnItemClickListener, OnClickListener, OnLongClickListener, PlatformActionListener {
	public static boolean isForeground = false;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
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

	private static final int POST_DELAY = 33;
	private static final String GUIDE_FLAG = "guide_flag";
	/**
	 * 拍照
	 */
	private static final int PHOTO_REQUEST_CODE = 1;
	/**
	 * 相册
	 */
	private static final int IMAGE_REQUEST_CODE = 0;
	/**
	 * 裁剪
	 */
	private static final int RESULT_REQUEST_CODE = 2;
	/**
	 * 用户头像
	 */
	private Bitmap mPortrait;
	/**
	 * 选择头像的popupwindow
	 */
	private SelectMenuPopupWindow mMenuWindow;
	private File mTempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
	private ImageDownloader downloader;
	/**
	 * 微信审核过后提供的appkid
	 */
	private static final String APP_ID = "wx7e2557d7ee299a9d";
	/**
	 * 微信的api
	 */
	private IWXAPI mWXApi;
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final int CAPACITY_SIZE = 8;
	private ListView mDemoList;
	private String mPackName;
	private CircleImageView mUserPortrait;
	/**
	 * 所有的数据集合
	 */
	private List<DemoListBena> mDemoAllListData;
	private List<DemoListBena> mDemoListDataCurrent;
	private String[] mAllClass;
	private DemoListAdapter mAdapter;
	private Dialog mDialog;
	private TextView mChange2Setting;
	private RelativeLayout mRoot;
	private View rootView;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case POST_DELAY:
				if (!readGuideFlag) {
					UserConfig.getInstance(MainActivity.this).saveGuideFlag(true);
					showFingerNoticePopup();
				}
				break;

			case MSG_AUTH_CANCEL: {
				// 取消授权
				T.show(MainActivity.this, "取消授权", 0);
			}
				break;
			case MSG_AUTH_ERROR: {
				// 授权失败
				T.show(MainActivity.this, "授权失败", 0);
			}
				break;
			case MSG_AUTH_COMPLETE: {
				// 授权成功
				T.show(MainActivity.this, "授权成功", 0);
				Object[] objs = (Object[]) msg.obj;
				String platformString = (String) objs[0];
				platform = ShareSDK.getPlatform(platformString);

				HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
				setOnLoginListener(signupListener);
				if (signupListener != null && signupListener.onSignin(platformString, res)) {
					// TODO 授权成功,获取用户资料
					getTplUserData();
					mDialog.dismiss();
					rl.clearAnimation();
					rl = null;
					mDialog = null;

				}

				getTplUserData();
				mDialog.dismiss();
			}
				break;

			default:
				break;

			}
		}

	};
	private PopupWindow mPopupWindow;
	private boolean readGuideFlag;
	private TextView mDemoTitle;

	// private PullToRefreshScrollView mPullRefreshScrollView;
	/** 设置授权回调，用于判断是否进入注册 */
	public void setOnLoginListener(OnLoginListener l) {
		this.signupListener = l;
	}

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
//		StatusBarCompat.compat(this, getResources().getColor(R.color.white));
		// 程序启动的时候调用初始化推送服务
//		JPushInterface.setDebugMode(true);
//		JPushInterface.init(this);

		mPackName = getPackageName() + ".ui.";
		initData();
		// 初始化部分数据
		// processData();
		initView();
		initPullRefreshScrollView();
		initEvent();
		registWXAPI();
	}

	/**
	 * 获取第三方用户平台资料
	 * 
	 * @param
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
		mDemoTitle.setText(userInfo.getUserName() + "(" + platform.getName() + "授权)");
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
		downloader.download(imageUrl, mUserPortrait);

	}

	/**
	 * 弹出提示的popupwindow
	 */
	protected void showFingerNoticePopup() {

		View inflateView = getLayoutInflater().inflate(R.layout.dialog_guide, null);
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(inflateView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}

		mPopupWindow.setTouchable(true);
		mPopupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		int right = 40;// getResources().getDimensionPixelSize(R.dimen.where_traffic_popwindow_right);
		int bottom = 10;// getResources().getDimensionPixelSize(R.dimen.where_traffic_popwindow_bottom);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.dialogAnimationStyle);
		mPopupWindow.showAtLocation(mRoot, Gravity.RIGHT | Gravity.TOP, right, bottom);

	}

	/**
	 * 将app注册到微信
	 */
	private void registWXAPI() {
		mWXApi = WXAPIFactory.createWXAPI(this, APP_ID, true);
		mWXApi.registerApp(APP_ID);
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		if (mAdapter == null) {
			mAdapter = new DemoListAdapter();
		}
		if (mDemoAllListData != null) {
			// if (mDemoListDataCurrent != null) {
			mAdapter.setTitles(mDemoAllListData);
			// mAdapter.setTitles(mDemoListDataCurrent);
			mDemoList.setAdapter(mAdapter);
			mDemoList.setOnItemClickListener(this);
		}
		if (mPortrait != null) {
			mUserPortrait.setImageBitmap(mPortrait);
		}
		mUserPortrait.setOnClickListener(this);
		mUserPortrait.setOnLongClickListener(this);
		mChange2Setting.setOnClickListener(this);

		if (!readGuideFlag) {
			Message msg = mHandler.obtainMessage();
			msg.what = POST_DELAY;
			mHandler.sendMessageDelayed(msg, 1000);
		}
	}

	/**
	 * 初始化界面控件
	 */
	private void initView() {
		mDemoList = (ListView) findViewById(R.id.lv_demo_list);
		mDemoTitle = (TextView) findViewById(R.id.tv_demo_list_title);
		mUserPortrait = (CircleImageView) findViewById(R.id.circle_image_user_portrait);
		mChange2Setting = (TextView) findViewById(R.id.tv_demo_list_title);
		mRoot = (RelativeLayout) findViewById(R.id.root);
		rootView = getLayoutInflater().inflate(R.layout.activity_main, null);
		// mPullRefreshScrollView = (PullToRefreshScrollView)
		// findViewById(R.id.ptsv_refresh);
	}

	/*
	 * 初始化下拉刷新控件
	 */
	private void initPullRefreshScrollView() {
		// mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
		// mPullRefreshScrollView.setOnRefreshListener(this);
		// mPullRefreshScrollView.setOnPullEventListener(this);

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		ShareSDK.initSDK(this);
		downloader = new ImageDownloader(null);
		byte[] readDragedMenu = (byte[]) Settings.getSettings(this).readDragedMenu(AppConstants.USERPORTRINT);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		if (readDragedMenu != null) {
			mPortrait = Tools.getBitmapFromBytes(readDragedMenu, opts);
		}
		readGuideFlag = UserConfig.getInstance(this).readGuideFlag();
		mDemoAllListData = new ArrayList<DemoListBena>();
		String[] titleList = getResources().getStringArray(R.array.title_name_list);
		mAllClass = getResources().getStringArray(R.array.title_name_list_activity);
		int[] intArray = { R.drawable.bg_demo_circle1, R.drawable.bg_demo_circle2, R.drawable.bg_demo_circle3, R.drawable.bg_demo_circle4, R.drawable.bg_demo_circle5, R.drawable.bg_demo_circle6, R.drawable.bg_demo_circle7,
				R.drawable.bg_demo_circle8 };
		int[] textColor = getResources().getIntArray(R.array.water_colors);
		for (int i = 0; i < titleList.length; i++) {
			String titleName = titleList[i];
			int pointColorResource = intArray[i % intArray.length];
			int tC = textColor[i % textColor.length];
			DemoListBena bena = new DemoListBena(titleName, pointColorResource);
			bena.setTextColor(tC);
			mDemoAllListData.add(bena);
		}
	}

	/**
	 * 处理数据
	 */
	private void processData() {
		if (mDemoListDataCurrent == null) {
			mDemoListDataCurrent = new ArrayList<DemoListBena>();
		}
		int currentSize = mDemoAllListData.size() > CAPACITY_SIZE ? CAPACITY_SIZE : mDemoAllListData.size();

		for (int i = 0; i < currentSize; i++) {
			mDemoListDataCurrent.add(mDemoAllListData.get(i));
		}
		mDemoAllListData.removeAll(mDemoListDataCurrent);
	}

	@Override
	public void onBackPressed() {
		Tools.doublePressExit(this);
	}

	/**
	 * demo list 适配器
	 * 
	 * @author guofl
	 * 
	 */
	class DemoListAdapter extends BaseAdapter {
		private List<DemoListBena> allDemoData;

		public void setTitles(List<DemoListBena> allDemoData) {
			this.allDemoData = allDemoData;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allDemoData.size();
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
			DemoListHolder holder = null;
			if (convertView == null) {
				holder = new DemoListHolder();
				convertView = getLayoutInflater().inflate(R.layout.item_demo_list, null);
				holder.itemPoint = (ImageView) convertView.findViewById(R.id.civ_demo_point);
				holder.itemTitle = (TextView) convertView.findViewById(R.id.tv_demo_title_name);
				convertView.setTag(holder);
			} else {
				holder = (DemoListHolder) convertView.getTag();
			}
			DemoListBena demoListBena = allDemoData.get(position);
			setData2Positon(holder, position, demoListBena);
			return convertView;
		}

	}

	class DemoListHolder {
		ImageView itemPoint;
		TextView itemTitle;
	}

	/**
	 * 设置数据到对应的item上面
	 * 
	 * @param holder
	 * @param position
	 * @param demoListBena
	 */
	public void setData2Positon(DemoListHolder holder, int position, DemoListBena demoListBena) {
		int pointBG = demoListBena.getPointColorResource();
		holder.itemPoint.setBackgroundDrawable(getResources().getDrawable(pointBG));
		holder.itemTitle.setText(demoListBena.getTitleName());
		holder.itemTitle.setTextColor(demoListBena.getTextColor());
	}

	class DemoListBena {
		String titleName;
		int pointBgResource;
		int textColor;

		public int getTextColor() {
			return textColor;
		}

		public void setTextColor(int textColor) {
			this.textColor = textColor;
		}

		public DemoListBena(String titleName, int pointBgResource) {
			super();
			this.titleName = titleName;
			this.pointBgResource = pointBgResource;
		}

		public String getTitleName() {
			return titleName;
		}

		public void setTitleName(String titleName) {
			this.titleName = titleName;
		}

		public int getPointColorResource() {
			return pointBgResource;
		}

		public void setPointColorResource(int pointColorResource) {
			this.pointBgResource = pointBgResource;
		}

	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {

	}

	@Override
	public void onPullEvent(PullToRefreshBase<ScrollView> refreshView, State state, Mode direction) {

		if (Mode.PULL_FROM_START.equals(direction)) {
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
		} else if (Mode.PULL_FROM_END.equals(direction)) {
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DemoListBena demoListBena = mDemoAllListData.get(position);
		// DemoListBena demoListBena = mDemoListDataCurrent.get(position);
		try {
			Class<? extends Object> clazz = Class.forName(mPackName + mAllClass[position]).newInstance().getClass();
			ViewUtils.changeActivity(MainActivity.this, clazz, demoListBena.getTitleName(), demoListBena.getTextColor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.circle_image_user_portrait:
			showCustomDialog();
			// ViewUtils.changeActivity(this, MyDialogActivity.class);
			break;
		case R.id.ibt_dialog_close:
			mDialog.dismiss();
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
		case R.id.tv_demo_list_title:
			UserConfig.getInstance(this).saveGuideFlag(true);
			ViewUtils.changeActivity(this, SettingActivity.class);
			if (mPopupWindow != null) {
				mPopupWindow.dismiss();
			}
			break;
		default:
			break;
		}
	}

	// 执行授权,获取用户信息
	private void authorize(Platform plat) {
		plat.setPlatformActionListener(this);
		// 关闭SSO授权
		plat.SSOSetting(true);
		plat.showUser(null);
		// plat.removeAccount();
	}

	/**
	 * 授权登录
	 */
	private void wxAuthorization() {
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "wechat_sdk_demo_test";
		mWXApi.sendReq(req);

	}

	/**
	 * 分享微信朋友圈
	 * 
	 * @param context
	 * @param bmp
	 */
	public void sendReqWX(Context context, Bitmap bmp, boolean isFriend) {
		WXWebpageObject localWXWebpageObject = new WXWebpageObject();
		localWXWebpageObject.webpageUrl = "www.baidu.com";
		WXMediaMessage localWXMediaMessage = new WXMediaMessage(localWXWebpageObject);
		localWXMediaMessage.title = "ceshi";// 不能太长，否则微信会提示出错。不过博主没验证过具体能输入多长。
		localWXMediaMessage.description = "ceshis";
		SendMessageToWX.Req localReq = new SendMessageToWX.Req();
		localReq.transaction = System.currentTimeMillis() + "";
		localReq.message = localWXMediaMessage;
		if (isFriend) {
			localReq.scene = SendMessageToWX.Req.WXSceneTimeline;
		} else {
			localReq.scene = SendMessageToWX.Req.WXSceneSession;
		}

		mWXApi.sendReq(localReq);

	}

	/**
	 * 显示自定义dialog,在window中加动画
	 */
	private void showCustomDialog() {

		if (mDialog == null) {
			mDialog = new Dialog(this, R.style.myDialogTheme);
			mDialog.setContentView(R.layout.dialog_usercenter_shared);
		}

		initDialogView(mDialog);
		mDialog.setCancelable(false);
		rl = (RelativeLayout) mDialog.findViewById(R.id.rl_container);
		ImageView ibt_dialog_close = (ImageView) mDialog.findViewById(R.id.ibt_dialog_close);
		ibt_dialog_close.setOnClickListener(this);
		TextView wxOth2 = (TextView) mDialog.findViewById(R.id.tv_wx_oth2);
		TextView sinaOth2 = (TextView) mDialog.findViewById(R.id.tv_sina_oth2);
		TextView qqOth2 = (TextView) mDialog.findViewById(R.id.tv_qq_oth2);

		wxOth2.setOnClickListener(this);
		sinaOth2.setOnClickListener(this);
		qqOth2.setOnClickListener(this);

		Window window = mDialog.getWindow();
		window.setWindowAnimations(R.style.dialogAnimationStyle);

		mDialog.show();
		
		LayoutAnimationController controller = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.custom_item_anim));
		controller.setDelay(0.8f);
		rl.setLayoutAnimation(controller );

	}

	/**
	 * 初始化dialog中的控件
	 * 
	 * @param mDialog2
	 */
	private void initDialogView(Dialog mDialog2) {
		// TODO 用dialog找子控件
	}

	@Override
	public boolean onLongClick(View v) {
		changeUserPortrait();
		return true;
	}

	/**
	 * 更换头像
	 */
	private void changeUserPortrait() {
		// 对话框
		mMenuWindow = new SelectMenuPopupWindow(this, itemsOnClick);
		// 是否显示移除按钮
		if (null == null) {
			mMenuWindow.setDeleteVisibility(View.GONE);
		} else {
			mMenuWindow.setDeleteVisibility(View.VISIBLE);
		}
		// 显示窗口
		mMenuWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			mMenuWindow.dismiss();
			switch (v.getId()) {
			case R.id.ll_take_photo:
				// 使用相机拍照
				Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				cameraintent.addCategory(Intent.CATEGORY_DEFAULT);
				// 指定调用相机拍照后照片的储存路径
				cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempFile));
				startActivityForResult(cameraintent, PHOTO_REQUEST_CODE);

				break;
			case R.id.ll_photo_album:
				// 从相册选择图片
				Intent intentFromGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
				break;
			case R.id.ll_photo_delete:

				ViewUtils.showYesNoDialogAutoDismiss(MainActivity.this, false, "Remove?", getString(R.string.button_confirm), getString(R.string.button_cancel), new AutoDismissOnClickListener() {

					@Override
					public void onYesClick() {
						// TODO 删除头像代码
					}

					@Override
					public void onNoClick() {
						// TODO Auto-generated method stub

					}
				});
				break;
			default:
				break;
			}
		}
	};
	private RelativeLayout rl;

	/**
	 * 使用系统当前日期加以调整作为照片的名称
	 * 
	 * @return
	 */
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case RESULT_REQUEST_CODE:// 裁剪
				if (data != null) {
					getImageToView(data);
				}
				break;
			case PHOTO_REQUEST_CODE:// 当选择拍照时调用
				startPhotoZoom(Uri.fromFile(mTempFile));
				break;
			}
		}

	}

	/**
	 * 开启裁剪图片
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);

		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param data
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			mPortrait = extras.getParcelable("data");
			toServer();// 上传头像
		}

	}

	/**
	 * bitmap 转 bytes
	 */
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 上传群头像到服务器
	 */
	private void toServer() {
		byte[] portrait = Bitmap2Bytes(mPortrait);
		Message bigmapMessage = mHandler.obtainMessage();
		mHandler.sendMessage(bigmapMessage);
		// 设置头像
		mUserPortrait.setImageBitmap(mPortrait);

		// 把当前的头像存储到内存
		Settings.getSettings(this).saveDargedMenu(AppConstants.USERPORTRINT, portrait);
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
