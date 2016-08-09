package com.guofeilong.fortune;

import java.io.File;

import android.os.Environment;

/**
 * 常量
 * 
 * @author Android_Tian
 * 
 */
public interface AppConstants {
	public static final long KB_UNIT = 1024;
	public static final long MB_UNIT = KB_UNIT * 1024;
	public static final long GB_UNIT = MB_UNIT * 1024;
	public static final long TB_UNIT = GB_UNIT * 1024;

	public static final long MINUTE_UNIT = 60;
	public static final long HOUR_UNIT = 60 * MINUTE_UNIT;
	public static final long DAY_UNIT = HOUR_UNIT * 24;
	// 密码最低长度
	public static final int MIN_PASSWORD_SIZE = 8;
	// 密码最大长度
	public static final int MAX_PASSWORD_SIZE = 30;

	public static final String sdCardPath = Environment.getExternalStorageDirectory().toString();
	public static final String cacheTempFilePath = sdCardPath + File.separator + "RTSS" + File.separator + "Temp";
	public static final String INTENT_SERVICEID = "serviceId";
	public static final String INTENT_SERVICETYPE = "serviceType";
	public static final String INTENT_AMOUNT = "amount";
	public static final String INTENT_USER = "INTENT_USER";
	public static final String INTENT_SOURCE_SERVICEID = "sourceServiceId";
	public static final String INTENT_SOURCE_SERVICETYPE = "sourceServiceType";

	public static final String PRODUCT_ID_TARGET_KEY = "PRODUCT_ID_TARGET";
	public static final String RESOURCE_ID_TARGET_KEY = "RESOURCE_ID_TARGET";
	public static final String RESOURCE_NAME_TARGET_KEY = "RESOURCE_NAME_TARGET";
	public static final String RESOURCE_UNIT_TARGET_KEY = "RESOURCE_UNIT_TARGET";
	public static final String RESOURCE_AMOUNT_SOURCE_KEY = "RESOURCE_AMOUNT_SOURCE";
	public static final String PRODUCT_ID_SOURCE_KEY = "PRODUCT_ID_SOURCE";
	public static final String RESOURCE_ID_SOURCE_KEY = "RESOURCE_ID_SOURCE";
	public static final String RESOURCE_NAME_SOURCE_KEY = "RESOURCE_NAME_SOURCE";
	public static final String RESOURCE_UNIT_SOURCE_KEY = "RESOURCE_UNIT_SOURCE";

	/**
	 * 从何处跳转过来
	 */
	public static final String INTENT_NAME_FROM = "intent_name_from";
	public static final String USERPORTRINT = "user_portrint";

	public static final String APP_KEY = "1af327905852bf87339e7eb1";
	public static final String APP_MASTER_SERCRET = "67727d14c60cc448ebfe4133";
	
	public static final String TULING_APP_KEY = "8b94e083f7160f61995b6f4d89f4e582";
	public static final String TULING_API_ADDRESS = "http://www.tuling123.com/openapi/api?key=";
	public static final String TULING_ENCODE="utf-8";

}
