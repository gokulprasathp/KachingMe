package com.wifin.kachingme.handler;

import android.os.Handler;
import android.os.Message;

import com.wifin.kachingme.util.Constant;

public class RefreshHandler extends Handler {
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	public void sleep(long delayMillis) {
		Constant.printMsg("on sleep:::::entered:::");
		this.removeMessages(0);
		sendMessageDelayed(obtainMessage(0), delayMillis);
		Constant.printMsg("on sleep:::::done:::");
	}

	public void resume(long delayMillis) {
		Constant.printMsg("on resme:::::entered:::");
		// this.removeMessages(0);
		// sendMessageDelayed(obtainMessage(0), delayMillis);
		Constant.printMsg("on resme:::::done:::");
	}
};
