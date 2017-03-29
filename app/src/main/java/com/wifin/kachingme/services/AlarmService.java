package com.wifin.kachingme.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.wifin.kachingme.wish_list.AddWishList;
import com.wifin.kaching.me.ui.R;

public class AlarmService extends IntentService {
	private NotificationManager alarmNotificationManager;

	public AlarmService() {
		super("AlarmService");
	}

	@Override
	public void onHandleIntent(Intent intent) {
		sendNotification("Reminder For Wish List");
	}

	private void sendNotification(String msg) {
		Log.d("AlarmService", "Preparing to send notification...: " + msg);
		alarmNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, AddWishList.class), 0);

		NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
				this).setContentTitle("Alarm")
				.setSmallIcon(R.drawable.ic_launcher)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		alamNotificationBuilder.setContentIntent(contentIntent);
		alarmNotificationManager.notify(1, alamNotificationBuilder.build());
		Log.d("AlarmService", "Notification sent.");
	}
}
