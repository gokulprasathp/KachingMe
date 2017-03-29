package com.wifin.kachingme.util;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class NetworkSharedPreference {

	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "NetworkPreference_#";

	public static final String KEY_MESSAGE_DATA_SX = "MSG_sent";
	public static final String KEY_MESSAGE_GET_SX = "MSG_get_sent";

	public static final String KEY_MESSAGE_DATA_RX = "MSG_recev";
	public static final String KEY_MESSAGE_GET_RX = "MSG_get_recev";

	public static final String KEY_MEDIA_DATA_RX = "media_recev";
	public static final String KEY_MEDIA_GET_RX = "media_get_recev";

	public static final String KEY_MEDIA_DATA_SX = "media_sent";
	public static final String KEY_MEDIA_GET_SX = "media_GET";

	public static final String KEY_TOTAL_DATA_SX = "total_sent";
	public static final String KEY_TOTAL_GET_SX = "total_sent_get";

	public static final String KEY_TOTAL_DATA_RX = "total_recev";
	public static final String KEY_TOTAL_GET_RX = "total_recev_get";

	public static final String KEY_TOTAL_AR_DATA_RX = "total_recev_ar";
	public static final String KEY_TOTAL_AR_GET_RX = "total_recev_get_ar";

	public static final String KEY_TOTAL_AR_DATA_SX = "total_sent_ar";
	public static final String KEY_TOTAL_AR_GET_SX = "total_sent_get_ar";

	// Constructor
	public NetworkSharedPreference(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	// Set Media receive details
	public void setMediaData_Rec(String val) {

		editor.putString(KEY_MEDIA_DATA_RX, val);

		editor.commit();
	}

	// Set Media sent details
	public void setMediaData_Sent(String val) {

		editor.putString(KEY_MEDIA_DATA_SX, val);

		editor.commit();
	}

	// Set Message sent details
	public void setMessageData_Sent(String val) {

		editor.putString(KEY_MESSAGE_DATA_SX, val);

		editor.commit();
	}

	// Set Message receive details
	public void setMessageData_Receive(String val) {

		editor.putString(KEY_MESSAGE_DATA_RX, val);

		editor.commit();
	}

	// Set total receive details
	public void setTotalData_Receive(String val) {

		editor.putString(KEY_TOTAL_DATA_RX, val);

		editor.commit();
	}

	// Set total receive details
	public void setTotalData_Sent(String val) {

		editor.putString(KEY_TOTAL_DATA_SX, val);

		editor.commit();
	}

	// Set total after reset receive details
	public void setTotalData_Ar_Recev(String val) {

		editor.putString(KEY_TOTAL_AR_DATA_RX, val);

		editor.commit();
	}

	// Set total after reset sent details
	public void setTotalData_Ar_Sent(String val) {

		editor.putString(KEY_TOTAL_AR_DATA_SX, val);

		editor.commit();
	}

	// Get Media sent details
	public HashMap<String, String> getMedia_SentDetails() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_MEDIA_GET_SX, pref.getString(KEY_MEDIA_DATA_SX, null));

		return data;
	}

	// Get Media receive details
	public HashMap<String, String> getMedia_ReceiveDetails() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_MEDIA_GET_RX, pref.getString(KEY_MEDIA_DATA_RX, null));

		return data;
	}

	// Get message sent details
	public HashMap<String, String> getMessage_SentDetails() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_MESSAGE_GET_SX, pref.getString(KEY_MESSAGE_DATA_SX, null));

		return data;
	}

	// Get message Receive details
	public HashMap<String, String> getMessage_ReceiveDetails() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_MESSAGE_GET_RX, pref.getString(KEY_MESSAGE_DATA_RX, null));

		return data;
	}

	// Get total sent details
	public HashMap<String, String> getTotal_SentData() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_TOTAL_GET_SX, pref.getString(KEY_TOTAL_DATA_SX, "0"));

		return data;
	}

	// Get total Receive details
	public HashMap<String, String> getTotal_ReceiveData() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_TOTAL_GET_RX, pref.getString(KEY_TOTAL_DATA_RX, "0"));

		return data;
	}

	// Get total after reset Receive details
	public HashMap<String, String> getTotal_Cur_ReceiveData() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_TOTAL_AR_GET_RX, pref.getString(KEY_TOTAL_AR_DATA_RX, "0"));

		return data;
	}

	// Get total after reset sent details
	public HashMap<String, String> getTotal_Cur_SentData() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_TOTAL_AR_GET_SX, pref.getString(KEY_TOTAL_AR_DATA_SX, "0"));

		return data;
	}

	// Get All Data
	public HashMap<String, String> getAll_Details() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_MEDIA_GET_SX, pref.getString(KEY_MEDIA_DATA_SX, null));
		data.put(KEY_MEDIA_GET_RX, pref.getString(KEY_MEDIA_DATA_RX, null));

		data.put(KEY_MESSAGE_GET_SX, pref.getString(KEY_MESSAGE_DATA_SX, null));
		data.put(KEY_MESSAGE_GET_RX, pref.getString(KEY_MESSAGE_DATA_RX, null));

		return data;
	}

	public void clearDataNewtork() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

	}

}
