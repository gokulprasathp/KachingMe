/*
* @author Gokul
*
* @usage -  This class is used to reacive connectivity and boot actions
*
*
* */

package com.wifin.kachingme.bradcast_recivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wifin.kachingme.registration_and_login.OtpVerification;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.settings.SettingsActivity;
import com.wifin.kachingme.util.Connectivity;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Utils;

import java.util.List;

public class GlobalBroadcast extends BroadcastReceiver {

    public static boolean isServiceRunning(String serviceClassName,
                                           Context context) {
        final ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(
                    serviceClassName)) {
                return true;
            }
        }
        return false;
    }

    public static void stopService(Context context) {
        new TempConnectionService().stopHandler();
        Intent login_broadcast;
        login_broadcast = new Intent("remove_subtitle");
        context.sendBroadcast(login_broadcast);

        if (TempConnectionService.mReconnectionManager != null) {
            TempConnectionService.mReconnectionManager
                    .disableAutomaticReconnection();
        }

        boolean isServiceStopped = context.stopService(new Intent(context.getApplicationContext(),
                TempConnectionService.class));
        Constant.printMsg("GGGGGGGGGG service stopped " + isServiceStopped);

        TempConnectionService.mDeliveryReciept = null;
        TempConnectionService.messageEventManager = null;
        TempConnectionService.mChatCreatedListener = null;
        TempConnectionService.MUC_MANAGER = null;
        TempConnectionService.muc_messageListener = null;
        TempConnectionService.muc = null;
        TempConnectionService.privacymanager = null;
        TempConnectionService.drm = null;
        TempConnectionService.chatmanager = null;
        TempConnectionService.roster = null;
        TempConnectionService.mReconnectionManager = null;
        TempConnectionService.connection = null;
    }

    // GlobalBroadcast obj = new GlobalBroadcast();
    @Override
    public void onReceive(Context context, Intent intent) {
        Constant.printMsg("Network there : service status onReceive:");
        if (Constant.loginOtp != null && !Constant.loginOtp.equalsIgnoreCase("null")
                && !Constant.loginOtp.isEmpty()) {
            if (Constant.isOtpVerification) {
                OtpVerification.otpConfirmation("broadcast");
                Constant.isOtpVerification = false;
            }
        } else {
            if (Constant.Otp != null && !Constant.Otp.equalsIgnoreCase("null")
                    && !Constant.Otp.isEmpty()) {
                if (Constant.isOtpVerification) {
                    OtpVerification.otpConfirmation("broadcast");
                    Constant.isOtpVerification = false;
                }
            }
        }
        Constant.printMsg("GGGGGGGGGG act" + intent.getAction().toString());
        if (Utils.isActivityIsFront(context, SettingsActivity.class.getCanonicalName()) == true) {
            SettingsActivity.recyclerSettingContent.getAdapter().notifyDataSetChanged();
        } else {
            Constant.printMsg("Settings Activity");
        }
        if (Connectivity.isOnline(context) == true) {
            Constant.printMsg("GGGGGGGGGG network there : service status :" + isServiceRunning(
                    TempConnectionService.class.getCanonicalName(), context));
            if (!isServiceRunning(
                    TempConnectionService.class.getCanonicalName(), context)) {
                context.startService(new Intent(
                        context.getApplicationContext(),
                        TempConnectionService.class));
            }
        } else {
            stopService(context);
        }


    }


}
