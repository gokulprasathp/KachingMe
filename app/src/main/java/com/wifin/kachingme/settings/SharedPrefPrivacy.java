package com.wifin.kachingme.settings;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefPrivacy {

	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "PrivacyInfo_#";

	public static final String KEY_LAST_SEEN = "LAST_SEEN";
	public static final String KEY_PROFILE_PIC = "PROFILE_PIC";
	public static final String KEY_STATUS = "STATUS";

	public static final String KEY_LAST_SEEN_GET = "LAST_SEEN_GET";
	public static final String KEY_PROFILE_PIC_GET = "PROFILE_PIC_GET";
	public static final String KEY_STATUS_GET = "STATUS_GET";

	public static final String EVERY_ONE = "0";
	public static final String MY_CONTACT = "1";
	public static final String STATUS = "2";

	// Constructor
	public SharedPrefPrivacy(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	// Set last seen
	public void setLastSeen(String val) {

		editor.putString(KEY_LAST_SEEN, val);

		editor.commit();
	}

	// Set profile pic
	public void setProfilePic(String val) {

		editor.putString(KEY_PROFILE_PIC, val);

		editor.commit();
	}

	// Set status
	public void setStatus(String val) {

		editor.putString(KEY_STATUS, val);

		editor.commit();
	}

	// Get Last seen
	public HashMap<String, String> getLastSeen() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_LAST_SEEN_GET, pref.getString(KEY_LAST_SEEN, "0"));

		return data;
	}

	// Get Profile pic
	public HashMap<String, String> getProfilePic() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_PROFILE_PIC_GET, pref.getString(KEY_PROFILE_PIC, "1"));

		return data;
	}

	// Get status
	public HashMap<String, String> getStatus() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_STATUS_GET, pref.getString(KEY_STATUS, "1"));

		return data;
	}

	// Get All Data
	public HashMap<String, String> getAll_Details() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_LAST_SEEN_GET, pref.getString(KEY_LAST_SEEN, "0"));
		data.put(KEY_PROFILE_PIC_GET, pref.getString(KEY_PROFILE_PIC, "1"));
		data.put(KEY_STATUS_GET, pref.getString(KEY_STATUS, "1"));

		return data;
	}

	public void clearDataNewtork() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

	}

}
