package com.wifin.kachingme.services;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.util.Constant;

/**
 * Sending Typing status in Group chat @MUC_Chat.java
 * 
 * @author kachingMe
 * 
 */
public class IntentTypingService extends IntentService {

	public IntentTypingService() {
		super("IntentTypingService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Constant.group_typingstatusListner = true;

		String jid = intent.getStringExtra("jid");
		if (jid != null) {
			DatabaseHelper dbAdapter = KachingMeApplication.getDatabaseAdapter();

			ArrayList<String> list = dbAdapter.getGroupMembersForTyping(jid,
					KachingMeApplication.getjid());
			Constant.printMsg("Aaa  came to sendTYping " + list);
			for (int i = 0; i < list.size(); i++) {

				try {
					TempConnectionService.messageEventManager
							.sendComposingNotification(list.get(i),
									Constant.TYPING_STATUS_GROUP);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		try {
			Thread.sleep(5000);
			Constant.group_typingstatusListner = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
