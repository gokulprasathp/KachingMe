package com.wifin.kachingme.bradcast_recivers;

import android.content.Context;

import com.wifin.kachingme.adaptors.UsageAdapters;
import com.wifin.kachingme.registration_and_login.OtpVerification;
import com.wifin.kachingme.util.Constant;

import java.util.Date;

/**
 * Created by siva(wifin) on 28/03/2017
 */

public class CallReceiver extends PhonecallReceiver {
    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
        Constant.printMsg("Call Recevier.......onIncomingCallReceived::::" + number + "..." + start);
        String otpNumber = number.replaceAll("[-+.^:,]", "").substring(0, 5);
        if (Constant.loginOtp != null && !Constant.loginOtp.equalsIgnoreCase("null")
                && !Constant.loginOtp.isEmpty()){
            if (Constant.loginOtp.equals(otpNumber))
                OtpVerification.otpConfirmation(otpNumber);
        } else {
            if (Constant.Otp != null && !Constant.Otp.equalsIgnoreCase("null")
                    && !Constant.Otp.isEmpty()) {
                if (Constant.Otp.equals(otpNumber))
                    OtpVerification.otpConfirmation(otpNumber);
            }else{
                if (Constant.secondaryOtp != null && !Constant.secondaryOtp.equalsIgnoreCase("null")
                        && !Constant.secondaryOtp.isEmpty()) {
                    if (Constant.secondaryOtp.equals(otpNumber))
                        UsageAdapters.otpConfirmation(otpNumber);
                }
            }
        }
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        Constant.printMsg("Call Recevier.......onMissedCall::::" + number + "..." + start);
    }
}
