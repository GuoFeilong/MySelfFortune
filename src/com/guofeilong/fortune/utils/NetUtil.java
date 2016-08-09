package com.guofeilong.fortune.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class NetUtil {
	/**
	 * 无网络
	 */
	public final static int TYPE_NETWORK_DISABLE=0;
	/**
	 * moblie网络
	 */
	public final static int TYPE_NETWORK_MOBILE=1;
	/**
	 * wifi网络
	 */
	public final static int TYPE_NETWORK_WIFI=2;

	/**
	 * 开关 3G信号
	 * 
	 * @param context
	 * @param enabled
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public static void setMobileDataEnabled(Context context, boolean enabled)
			throws ClassNotFoundException, SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException, NoSuchMethodException,
			InvocationTargetException {

		final ConnectivityManager conman = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final Class conmanClass = Class.forName(conman.getClass().getName());
		final Field iConnectivityManagerField = conmanClass
				.getDeclaredField("mService");
		iConnectivityManagerField.setAccessible(true);
		final Object iConnectivityManager = iConnectivityManagerField
				.get(conman);
		final Class iConnectivityManagerClass = Class
				.forName(iConnectivityManager.getClass().getName());
		final Method setMobileDataEnabledMethod = iConnectivityManagerClass
				.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		setMobileDataEnabledMethod.setAccessible(true);

		setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	}
	
	/**
	 * 
	 * Description:获取网络的类型
	 * Date:2013-5-21
	 * @author MrJing
	 * @param context
	 * @return 
	 * @return int
	 */
	public static int getTypeNet(Context context) {
		int type = TYPE_NETWORK_DISABLE;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo == null || !networkInfo.isAvailable()) {
			type = TYPE_NETWORK_DISABLE;// 网络不可用
		} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			type = TYPE_NETWORK_MOBILE;
		} else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			type = TYPE_NETWORK_WIFI;
		}
		return type;
	}


	/**
	 * 
	 * 
	 * @Title: setNet
	 * 
	 * @Description: 弹出手机设置网络界面
	 * 
	 * @param 设定文件
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 * 
	 * @author MrJing
	 * 
	 * @date 2012-2-21 下午8:28:53
	 */
	public void setNet(Context context) {
		Intent mIntent = new Intent("/");
		ComponentName comp = new ComponentName("com.android.settings",
				"com.android.settings.WirelessSettings");
		mIntent.setComponent(comp);
		mIntent.setAction("android.intent.action.VIEW");
		context.startActivity(mIntent); // 如果在设置完成后需要再次进行操作，可以重写操作代码，在这里不再重写
	}

	// 获取本地IP函数
	/**
	 * <uses-permission
	 * android:name="android.permission.INTERNET"></uses-permission>
	 * <uses-permission
	 * android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
	 * 
	 * @return
	 */
	public static String getLocalIPAddress() {
		try {
			for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
					.getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
				NetworkInterface intf = mEnumeration.nextElement();
				for (Enumeration<InetAddress> enumIPAddr = intf
						.getInetAddresses(); enumIPAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIPAddr.nextElement();
					// 如果不是回环地址
					if (!inetAddress.isLoopbackAddress()) {
						// 直接返回本地IP地址
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("Error", ex.toString());
		}
		return null;
	}

	/**
	 * 是否开启 wifi true：开启 false：关闭
	 * 
	 * 一定要加入权限： <uses-permission
	 * android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
	 * <uses-permission
	 * android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
	 * 
	 * 
	 * @param isEnable
	 */
	public static void setWifi(Context context, boolean isEnable) {

		//
		WifiManager mWm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

		if (isEnable) {// 开启wifi

			if (!mWm.isWifiEnabled()) {

				mWm.setWifiEnabled(true);

			}
		} else {
			// 关闭 wifi
			if (mWm.isWifiEnabled()) {
				mWm.setWifiEnabled(false);
			}
		}

	}
	
	/**
	 * 判断手机网络是否可用
	 * @param context
	 */
	public static boolean isAvailableNet(Context context){
		if (NetUtil.getTypeNet(context) == NetUtil.TYPE_NETWORK_DISABLE) {
			return false;
		}else {
			return true;
		}
	}

}