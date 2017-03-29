/**
 * 
 */
package com.wifin.kachingme.kaching_feature.karaoke;

import android.os.Handler;
import android.os.Message;

import com.wifin.kachingme.util.Constant;

/**
 * @author USER
 * 
 */
class RefreshHandler extends Handler {
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
// class RefreshHandler extends Handler {
// Stack<Message> s = new Stack<Message>();
// boolean is_paused = false;
//
// public synchronized void pause() {
// is_paused = true;
//
// }
//
// public synchronized void resume() {
// is_paused = false;
// while (!s.empty()) {
// sendMessageAtFrontOfQueue(s.pop());
// }
// }
//
// @Override
// public void handleMessage(Message msg) {
// if (is_paused) {
// s.push(Message.obtain(msg));
// return;
// }
//
// // otherwise handle message as normal
// // ...
// }
// };
