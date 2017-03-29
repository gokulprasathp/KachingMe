/*
* @author Gokul
*
* @usage -  This class is used to reacive SMS action
*
*
* */

package com.wifin.kachingme.bradcast_recivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    String pin;
    String TAG = IncomingSms.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage
                            .createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage
                            .getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    Constant.printMsg("message::::>>>>>>>" + message + " num "
                            + senderNum);

                    pin = message.toString().substring(28);

                    if (senderNum.substring(3).equals("NFCHAT")) {
                        Intent sms_broadcast = new Intent("sms");
                        sms_broadcast.putExtra("pin", pin);
                        context.sendBroadcast(sms_broadcast);
                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
            // //ACRA.getErrorReporter().handleException(e);

        }
    }
}