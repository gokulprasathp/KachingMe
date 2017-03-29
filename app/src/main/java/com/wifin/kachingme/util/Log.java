package com.wifin.kachingme.util;

import com.wifin.kachingme.util.Constant;

public class Log {

	public static void d(String tag, String messages) {
		if (Constant.DEBUG)
			android.util.Log.d(tag, messages);
	}

	public static void e(String tag, String messages) {
		if (Constant.DEBUG)
			android.util.Log.e(tag, messages);
	}

	public static void i(String tag, String messages) {
		if (Constant.DEBUG)
			android.util.Log.i(tag, messages);
	}

	public static void w(String tag, String messages) {
		if (Constant.DEBUG)
			android.util.Log.w(tag, messages);
	}

	public static void v(String tag, String messages) {
		if (Constant.DEBUG)
			android.util.Log.v(tag, messages);
	}
}
