//package com.wifin.kachingme.services;
//
//import android.app.IntentService;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.database.SQLException;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.preference.PreferenceManager;
//import android.provider.Settings;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.wifin.kachingme.bradcast_recivers.GcmBroadcastReceiver;
//import com.wifin.kachingme.database.Dbhelper;
//import com.wifin.kachingme.chat_home.SliderTesting;
//import com.wifin.kachingme.util.CommonMethods;
//import com.wifin.kachingme.util.Constant;
//import com.wifin.kaching.me.ui.R;
//
//public class GCMNotificationIntentService extends IntentService {
//	String mydate;
//	public static final int NOTIFICATION_ID = 1;
//	private NotificationManager mNotificationManager;
//	NotificationCompat.Builder builder;
//
//	public static String SERVER_MSGS = "";
//
//	SharedPreferences pref;
//	SharedPreferences sp;
//	String mToNUm;
//	Dbhelper db;
//
//	public GCMNotificationIntentService() {
//		super("GcmIntentService");
//	}
//
//	public static final String TAG = "GCMNotificationIntentService";
//
//	@Override
//	protected void onHandleIntent(Intent intent) {
//		pref = PreferenceManager.getDefaultSharedPreferences(this);
//		Bundle extras = intent.getExtras();
//		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
//
//		String messageType = gcm.getMessageType(intent);
//		sp = PreferenceManager.getDefaultSharedPreferences(this);
//		Constant.printMsg("Message Type : " + messageType);
//		db = new Dbhelper(getApplicationContext());
//
//		if (!extras.isEmpty()) {
//			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
//					.equals(messageType)) {
//
//				sendNotification("Send error: " + extras.toString());
//
//			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
//					.equals(messageType)) {
//
//				sendNotification("Deleted messages on server: "
//						+ extras.toString());
//
//			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
//					.equals(messageType)) {
//
//				for (int i = 0; i < 3; i++) {
//					Log.i(TAG,
//							"Working... " + (i + 1) + "/3 @ "
//									+ SystemClock.elapsedRealtime());
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//					}
//
//				}
//				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
//				String received_message = extras.get(Constant.MESSAGE_KEY)
//						.toString();
//
//				SERVER_MSGS = received_message;
//
//				// handleResponse(received_message);
//				Constant.printMsg("received message ::::>>>"
//						+ received_message);
//				sendNotification(received_message);
//
//				Log.i("PUSH msgssss", received_message);
//
//				Log.i(TAG, "Received: " + extras.toString());
//			}
//		}
//		GcmBroadcastReceiver.completeWakefulIntent(intent);
//	}
//
//	private void sendNotification(String msg) {
//		Log.d(TAG, "Preparing to send notification...: " + msg);
//		Constant.printMsg("push msg ::>>> " + msg);
//		String ecword = msg.replaceAll("\\[", "");
//
//		String[] wrArray = ecword.split("\\s*,\\s*");
//
//		for (int i = 0; i < wrArray.length; i++) {
//
//			// String wr = wrArray[i].toLowerCase();
//			//
//			// Constant.blinkoffer.add(wr);
//
//		}
//
//		String[] parts = msg.split(" ");
//		Constant.mPushMsg = msg;
//		for (int i = 0; i < parts.length; i++) {
//
//			// String wr = parts[i].toLowerCase();
//			//
//			// Constant.printMsg("push mess::1" + wr + i);
//
//			if (i == 0) {
//
//				// mToNUm = wr;
//				// Constant.pushNum = mToNUm;
//
//			}
//
//			if (i == 2) {
//
//				// Constant.printMsg("push mess::" + wr);
//				//
//				// Long point = sp.getLong("buxvalue", 0);
//				//
//				// Constant.userId = sp.getLong("uservalue", 0);
//				//
//				// Long dtpoint = sp.getLong("donationpoint", 0);
//				//
//				// Constant.bux = point;
//				//
//				// Constant.point = dtpoint;
//				//
//				// Long bx = Long.valueOf(wr);
//				//
//				// Constant.donatepoint = Long.valueOf(wr);
//				//
//				// Long buxvalue = Constant.bux + bx;
//				// Constant.mDonatedBux = Constant.bux + bx;
//				//
//				// Constant.bux = buxvalue;
//				//
//				// Editor e = sp.edit();
//				// e.putLong("buxvalue", buxvalue);
//				// e.putLong("uservalue", Constant.userId);
//				// e.commit();
//				//
//				// Constant.printMsg("consat::" + Constant.donatepoint
//				// + Constant.bux + "vbx" + bx + "pt" + Constant.point);
//				//
//				// Long pointdon = bx + Constant.point;
//				//
//				// Editor e1 = sp.edit();
//				// e1.putLong("donationpoint", pointdon);
//				// e1.commit();
//				//
//				// Constant.point = pointdon;
//				//
//				// Constant.printMsg("name" + mToNUm + Constant.bux + bx
//				// + pointdon + Constant.point);
//				//
//				// mydate = java.text.DateFormat.getDateTimeInstance().format(
//				// Calendar.getInstance().getTime());
//				// Constant.mPushDate = mydate;
//				// Constant.printMsg("calllledddd GCM::::::>>>>>>>>>");
//				//
//				// DonationDto dp = new DonationDto();
//				//
//				// dp.setName(mToNUm);
//				// dp.setDate(mydate);
//				// // dp.setPoint(String.valueOf(Constant.donatepoint));
//				// dp.setPoint(String.valueOf(Constant.point));
//				// Constant.donatelust.add(dp);
//				//
//				// ContentValues cv = new ContentValues();
//				//
//				// cv.put("date", mydate);
//				// cv.put("points", Constant.donatepoint);
//				// cv.put("name", mToNUm);
//				//
//				// insertDBDonation(cv);
//
//			}
//
//		}
//
//		// MithraHome.jsonString = msg;
//		mNotificationManager = (NotificationManager) this
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		// PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//		// new Intent(this, MainActivity.class), 0);
//		Constant.requestCode = 0;
//		PendingIntent contentIntent = PendingIntent.getActivity(this,
//				Constant.requestCode, new Intent(this, SliderTesting.class), 0);
//		PendingIntent contentIntent1 = PendingIntent.getActivity(this,
//				Constant.requestCode, new Intent(this, SliderTesting.class), 0);
//		// Notification builder to receive message
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//				this)
//				.setSmallIcon(R.drawable.ic_launcher)
//				.setContentTitle("Please Verify! By Kachingme")
//				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//				.setContentText(msg)
//				.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//				.addAction(R.drawable.ic_action_navigation_accept, "Accept",
//						contentIntent)
//				.addAction(R.drawable.ic_action_navigation_cancel, "Reject",
//						contentIntent);
//		// .setCategory("yes");
//
//		// .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
//
//		mBuilder.setContentIntent(contentIntent);
//		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//		Log.d(TAG, "Notification sent successfully.");
//
//		// added on 11-09-2014 by prabhakar for get location after received push
//		// msg
//		// LocationService.obj.getLatiLongi();
//		handleResponse(msg);
//	}
//
//	public void handleResponse(String result) {
//		String s = result;
//		Constant.printMsg("ressultt :: >>> " + result);
//		// @1blocked1@
//
//	}
//
//	public void insertDBDonation(ContentValues v) {
//
//		try {
//			int a = (int) db.open().getDatabaseObj()
//					.insert(Dbhelper.TABLE_DONATE, null, v);
//
//			Constant.printMsg("No of inserted rows in shop details :::::::::"
//					+ a);
//		} catch (SQLException e) {
//			Constant.printMsg("Sql exception in new shop details ::::::"
//					+ e.toString());
//		} finally {
//			db.close();
//		}
//
//	}
//
//}
