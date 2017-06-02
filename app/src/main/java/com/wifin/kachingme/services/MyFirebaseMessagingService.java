package com.wifin.kachingme.services;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.webkit.URLUtil;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Constant;

import org.json.JSONArray;

import java.util.Map;

/**
 * Created by siva(wifin) on 03/03/2017
 * Usage : Handle the Received Message from Firebase
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public MyFirebaseMessagingService() {
        Constant.printMsg("Firebase MyFirebaseMessagingService ...............");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Constant.printMsg("Firebase Message Recevied..............." + remoteMessage);
        Constant.printMsg("Firebase Message Recevied From..............." + remoteMessage.getFrom());
        if (remoteMessage == null)
            return;
        /**getIcon 0 means status && 1 means Profile*/
        /**getTitle as Jid or user Number*/
        /**getBody what user change in his or her db*/
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String logout = remoteMessage.getNotification().getBody();
            Constant.printMsg("Notification Body......: " + logout);
            if (logout != null && !logout.isEmpty() && logout.equalsIgnoreCase("Logout")) {
                SliderTesting.logoutTheUserFirebase();
            }
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Constant.printMsg("Data Payload: " + remoteMessage.getData().toString());
            Constant.printMsg("Firebase Message Payload..............." + remoteMessage.getData());
            try {
                Map data = remoteMessage.getData();
                String body = String.valueOf(data.get("body"));
                String icon = String.valueOf(data.get("icon"));
                String title = String.valueOf(data.get("title"));
                updateContact(null, title, icon, body);
            } catch (Exception e) {
                Constant.printMsg("Exception: " + e.getMessage());
            }
        }
    }

    private void updateContact(Context context, String title, String icon, String body) {
        DatabaseHelper dbAdapter = KachingMeApplication.getDatabaseAdapter();
        CommonMethods com = new CommonMethods(getApplicationContext());
        if (title != null && !title.equalsIgnoreCase("null") && !title.isEmpty() &&
                icon != null && !icon.equalsIgnoreCase("null") && !icon.isEmpty() &&
                body != null && !body.equalsIgnoreCase("null") && !body.isEmpty()) {
            Constant.printMsg("Firebase Message Payload..............." + title + "....." + icon + "....." + body);
            try {
                JSONArray jsonArray = new JSONArray(title);
                for (int i = 0; jsonArray.length() > i; i++) {
                    Constant.printMsg("Firebase Message Payload...Users P&S ............" + jsonArray.getString(i));
                    if (icon.equalsIgnoreCase("0")) {
                        /**status only update */
                        if (dbAdapter.getJidIdPresentOrNot(jsonArray.getString(i) +
                                KachingMeApplication.getHost()) != 0) {
                            dbAdapter.updateInsertedContacts(
                                    jsonArray.getString(i) + KachingMeApplication.getHost(),
                                    jsonArray.getString(i), null, body, null);
                            Intent login_broadcast = new Intent("contact_firebase_update");
                            getApplicationContext().sendBroadcast(login_broadcast);
                        }
                    } else {
                        if (icon.equalsIgnoreCase("1") && URLUtil.isValidUrl(body)) {
                            /**profile only update */
                            if (dbAdapter.getJidIdPresentOrNot(jsonArray.getString(i) +
                                    KachingMeApplication.getHost()) != 0) {
                                dbAdapter.updateInsertedContacts(
                                        jsonArray.getString(i) + KachingMeApplication.getHost(),
                                        jsonArray.getString(i), body, null, null);
                                Constant.isFirebaseBitmap = true;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    com.new getImagefromUrlAsy().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                            jsonArray.getString(i), body, null, null);
                                } else {
                                    com.new getImagefromUrlAsy().execute(jsonArray.getString(i), body, null, null);
                                }
                                Intent login_broadcast = new Intent("contact_firebase_update");
                                getApplicationContext().sendBroadcast(login_broadcast);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
