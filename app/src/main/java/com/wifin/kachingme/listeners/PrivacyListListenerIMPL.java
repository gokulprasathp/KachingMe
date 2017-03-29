package com.wifin.kachingme.listeners;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smackx.privacy.PrivacyListListener;
import org.jivesoftware.smackx.privacy.PrivacyListManager;
import org.jivesoftware.smackx.privacy.packet.PrivacyItem;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;

public class PrivacyListListenerIMPL implements PrivacyListListener {

	TempConnectionService service;
	SharedPreferences sp;
	Editor editor;

	public PrivacyListListenerIMPL(TempConnectionService ser) {
		service = ser;

		sp = service.getSharedPreferences(
				KachingMeApplication.getPereference_label(), Activity.MODE_PRIVATE);
		editor = sp.edit();
	}

	@Override
	public void setPrivacyList(String arg0, List<PrivacyItem> arg1) {
		// TODO Auto-generated method stub
		Log.d("PrivacyListListener", arg0);
		ArrayList<String> list = new ArrayList<String>();
		for (PrivacyItem privacyItem : arg1) {
			list.add(privacyItem.getValue());
		}

		KachingMeApplication.setBlocked_user(list);

	}

	@Override
	public void updatedPrivacyList(String arg0) {
		// TODO Auto-generated method stub

		int b = 0;
		String blocked = "";

		Log.d("PrivacyListListener", "Privacy List Updated " + arg0);
		try {
			ArrayList<String> list = new ArrayList<String>();

			PrivacyListManager mngr = TempConnectionService.privacymanager;
			List<PrivacyItem> privacy_items = mngr.getPrivacyList(arg0)
					.getItems();
			for (PrivacyItem privacyItem : privacy_items) {
				list.add(privacyItem.getValue());
				Log.d("Privacy Listener",
						"Blocked users::" + privacyItem.getValue());

				if (b == 0) {
					blocked = privacyItem.getValue();
				} else {
					blocked += "," + privacyItem.getValue();
				}
				b++;

			}

			KachingMeApplication.setBlocked_user(list);

			editor.putString(Constant.BLOCKED_USERS, blocked);
			editor.commit();
		} catch (Exception e) {
			// ACRA.getErrorReporter().handleException(e);
			// TODO: handle exception
		}
	}

}
