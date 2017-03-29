package com.wifin.kachingme.listeners;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;

import android.content.Context;

import com.wifin.kachingme.util.Constant;

public class ChatCreatedListener implements ChatManagerListener {

	Context service;
	ChatStatusListener mChatStateListenerImpl;
	String uid;

	public ChatCreatedListener(Context service, String uid) {

		this.service = service;
		this.uid = uid;
		mChatStateListenerImpl = new ChatStatusListener(service, uid);
		Constant.printMsg("called::>>>dc secret");

	}

	@Override
	public void chatCreated(Chat chat, boolean arg1) {
		// TODO Auto-generated method stub
		chat.addMessageListener(mChatStateListenerImpl);
		// //Constant.printMsg("this is registered before recieing any message");
	}

	public ChatStatusListener getMessageListener() {
		return mChatStateListenerImpl;
	}

}
