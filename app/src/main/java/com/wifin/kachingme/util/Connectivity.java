package com.wifin.kachingme.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.wifin.kachingme.services.TempConnectionService;

public class Connectivity {

	public static final int NETWORK_TYPE_EHRPD = 14; // Level 11
	public static final int NETWORK_TYPE_EVDO_B = 12; // Level 9
	public static final int NETWORK_TYPE_HSPAP = 15; // Level 13
	public static final int NETWORK_TYPE_IDEN = 11; // Level 8
	public static final int NETWORK_TYPE_LTE = 13; // Level 11

	/**
	 * Check if there is any connectivity
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	/**
	 * Check if there is fast connectivity
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isConnectedFast(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected() && Connectivity
				.isConnectionFast(info.getType(), info.getSubtype()));
	}

	/**
	 * Check if the connection is fast
	 * 
	 * @param type
	 * @param subType
	 * @return boolean
	 */
	public static boolean isConnectionFast(int type, int subType) {
		if (type == ConnectivityManager.TYPE_WIFI) {
			return true;
		} else if (type == ConnectivityManager.TYPE_MOBILE) {
			switch (subType) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return false; // ~ 14-64 kbps
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return true; // ~ 400-1000 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return true; // ~ 600-1400 kbps
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return false; // ~ 100 kbps
			case Connectivity.NETWORK_TYPE_EHRPD:
				return true; // ~ 1-2 Mbps
			case Connectivity.NETWORK_TYPE_EVDO_B:
				return true; // ~ 5 Mbps
			case Connectivity.NETWORK_TYPE_HSPAP:
				return true; // ~ 10-20 Mbps
			case Connectivity.NETWORK_TYPE_IDEN:
				return false; // ~25 kbps
			case Connectivity.NETWORK_TYPE_LTE:
				return true; // ~ 10+ Mbps
				// Unknown
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return false;
			default:
				return false;
			}
		} else {
			return false;
		}
	}


    /* Check connectivity */
    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        cm = null;
        // should check null because in airplane mode it will be null
        boolean isConn = false;
        if(netInfo != null ) {
            if(netInfo.isConnected()) {
                Constant.printMsg("isOnline status :" + true);
                isConn = true;
            }
        }
        else {
            Constant.printMsg("isOnline status :" + false);
            isConn = false;
        }
        netInfo = null;
        return isConn;

    }

    public static boolean isTempConnection()
    {
        try {
            if (TempConnectionService.connection != null) {
                if (TempConnectionService.connection.isConnected() && TempConnectionService.connection.isAuthenticated())
                    return true;
                else
                    return false;
            } else
                return false;
        }catch (Exception e)
        {
            return false;
        }
    }
}
