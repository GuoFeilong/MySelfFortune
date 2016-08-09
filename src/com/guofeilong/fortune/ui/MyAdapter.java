/*
 * 官网地站:http://www.ShareSDK.cn
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 ShareSDK.cn. All rights reserved.
 */

package com.guofeilong.fortune.ui;

import android.view.View;
import cn.sharesdk.framework.authorize.AuthorizeAdapter;
import cn.sharesdk.framework.TitleLayout;

/**
 * 一个用于演示{@link AuthorizeAdapter}的例子。
 * <p>
 * 本demo将在授权页面底部显示一个“关注官方微博”的提示框，
 *用户可以在授权期间对这个提示进行控制，选择关注或者不关
 *注，如果用户最后确定关注此平台官方微博，会在授权结束以
 *后执行关注的方法。
 */
public class MyAdapter extends AuthorizeAdapter {
	public void onCreate() {
		// 隐藏标题栏右部的ShareSDK Logo
		hideShareSDKLogo();
		TitleLayout llTitle = getTitleLayout();
		llTitle.getTvTitle().setText("授权登录");
		llTitle.getChildAt(1).setVisibility(View.GONE);
		
		
	}

}
