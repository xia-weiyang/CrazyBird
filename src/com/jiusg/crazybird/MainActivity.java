package com.jiusg.crazybird;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

public class MainActivity extends Activity {

	public static int width = 0;
	public static int height = 0;
	private String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		width = getScreenWidth();
		height = getScreenHeight();
		Log.i(TAG, width + "");
		Log.i(TAG, height + "");

		setContentView(new BirdView(MainActivity.this));
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @return 屏幕宽度
	 */
	public int getScreenWidth() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @return 屏幕高度（不除去状态栏）
	 */
	public int getScreenHeight() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 获取顶栏的高度
	 * 
	 * @return
	 */
	public int getStatusBarHeight() {
		Class<?> c = null;
		Object obj = null;
		java.lang.reflect.Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = getResources().getDimensionPixelSize(x);
			return statusBarHeight;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}

	@Override
	protected void onResume() {

		super.onResume();
		BirdView.PAUSE = false;
	}

	@Override
	protected void onPause() {

		super.onPause();
		BirdView.PAUSE = true;
	}

}
