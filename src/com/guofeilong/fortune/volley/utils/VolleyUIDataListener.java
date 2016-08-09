package com.guofeilong.fortune.volley.utils;

/**
 * <pre>
 * 对数据进行封装
 * 统一泛型,返回给UI bean对象
 * </pre>
 * 
 * @author guofl
 * 
 * @param <T>
 */
public interface VolleyUIDataListener<T> {
	public void onDataChanged(T data);

	public void onErrorHappened(String errorCode, String errorMessage);
}
