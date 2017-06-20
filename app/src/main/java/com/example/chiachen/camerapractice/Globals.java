package com.example.chiachen.camerapractice;

import android.app.Application;

/**
 * Created by chiachen on 2017/6/20.
 */

public class Globals extends Application {
	private static Globals sInstance;

	public static Globals getInstance() {
		return sInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
	}
}
