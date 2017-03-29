package com.wifin.kachingme.util;

import java.util.ArrayList;
import java.util.Collections;

import android.support.v4.app.NotificationCompat;

import com.wifin.kachingme.pojo.Notification_GetSet;

public class NotificationHandler {

	static NotificationHandler instance;
	static NotificationCompat.InboxStyle inboxstyle;
	int counter = 0;
	ArrayList<String> conversation = new ArrayList<String>();
	ArrayList<Notification_GetSet> notifications = new ArrayList<Notification_GetSet>();

	public static NotificationHandler Instance() {
		if (instance == null)
			instance = new NotificationHandler();

		return instance;
	}

	public int addCounter() {
		counter++;
		return counter;
	}

	public int getCounter() {

		return counter;
	}

	public int resetCounter() {
		counter = 0;
		resetNotificationMessages();
		return counter;
	}

	public NotificationCompat.InboxStyle getNotificationInbox() {
		if (counter == 0) {
			inboxstyle = new NotificationCompat.InboxStyle();
			notifications = new ArrayList<Notification_GetSet>();
			return inboxstyle;
		}

		return inboxstyle;
	}

	public ArrayList<String> getConversation() {
		if (counter == 0) {
			conversation = new ArrayList<String>();
		}

		return conversation;
	}

	public void addConversation(String jid) {
		if (counter == 0) {
			conversation = new ArrayList<String>();
		}

		if (!conversation.contains(jid))
			conversation.add(jid);

	}

	public void resetNotificationMessages() {
		notifications = new ArrayList<Notification_GetSet>();
	}

	public void addNotificationMessages(Notification_GetSet notification) {
		if (notifications != null)
			notifications.add(notification);

	}

	public ArrayList<String> getInboxMessages() {
		ArrayList<String> inbox = new ArrayList<String>();
		ArrayList<Notification_GetSet> reverse_list = new ArrayList<Notification_GetSet>();
		reverse_list = (ArrayList<Notification_GetSet>) notifications.clone();
		Collections.reverse(reverse_list);

		int j = 0;

		for (int i = 0; i < reverse_list.size(); i++) {

			j++;
			if (j <= 7) {
				if (conversation.size() > 1) {
					if (reverse_list.get(i).is_group) {
						inbox.add(reverse_list.get(i).name + " @ "
								+ reverse_list.get(i).group_name + ": "
								+ reverse_list.get(i).message);
					} else {
						inbox.add(reverse_list.get(i).name + ": "
								+ reverse_list.get(i).message);
					}
				} else {
					if (reverse_list.get(i).is_group) {
						inbox.add(reverse_list.get(i).name + ": "
								+ reverse_list.get(i).message);
					} else {
						inbox.add(reverse_list.get(i).message);
					}
				}
			}
		}

		return inbox;
	}

}
