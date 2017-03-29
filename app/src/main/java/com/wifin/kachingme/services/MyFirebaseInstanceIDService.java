package com.wifin.kachingme.services;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.util.Constant;

/**
 * Created by siva(wifin) on 03/03/2017
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    public MyFirebaseInstanceIDService() {
        Constant.printMsg("Firebase MyFirebaseInstanceIDService .......................");
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Constant.printMsg("Firebase onToken Refresh Called.......................");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        //Intent registrationComplete = new Intent(Constant.REGISTRATION_COMPLETE);
        //registrationComplete.putExtra("token", refreshedToken);
        //LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void storeRegIdInPref(String token) {
        Constant.printMsg("Firebase onToken Refresh Stored Sucessfully......................." + token);
        SharedPreferences pref = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
}

