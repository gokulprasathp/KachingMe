package com.wifin.kachingme.registration_and_login;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * Created on 8/2/2016 To save the data in register_activity screen when application
 * is in paused state.
 * 
 * @author dilip
 * 
 */
public class OtpSharedPreference {

	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "RegistrationPreference_#";

	public static final String KEY_FIRST_NAME_SET = "firstName";
	public static final String KEY_FIRST_NAME_GET = "firstNameGet";

	public static final String KEY_LAST_NAME_SET = "lastName";
	public static final String KEY_LAST_NAME_GET = "lastNameGet";

	public static final String KEY_DATE_SET = "dateSet";
	public static final String KEY_DATE_GET = "dateGet";

	public static final String KEY_TERMS_SET = "termsSet";
	public static final String KEY_TERMS_GET = "termsGet";

	public static final String KEY_CONTACT_SET = "contactSet";
	public static final String KEY_CONTACT_GET = "contactGet";

	public static final String KEY_GENDER_SET = "gender";

	public static final String KEY_RESGISTRATION_STATUS = "isRegistered";

	// Constructor
	public OtpSharedPreference(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	// Set Registrations details values
	public void setRegistrationData(String frstName, String lstName,
			String date, boolean isTerm, boolean isContact, boolean isGender) {

		editor.putString(KEY_FIRST_NAME_SET, frstName);
		editor.putString(KEY_LAST_NAME_SET, lstName);
		editor.putString(KEY_DATE_SET, date);
		editor.putBoolean(KEY_TERMS_SET, isTerm);
		editor.putBoolean(KEY_CONTACT_SET, isContact);
		editor.putBoolean(KEY_GENDER_SET, isGender);
		editor.commit();
	}

	// Set registered status
	public void setRegistered(boolean regst) {

		editor.putBoolean(KEY_RESGISTRATION_STATUS, regst);

		editor.commit();
	}

	public boolean isRegistered() {
		return pref.getBoolean(KEY_RESGISTRATION_STATUS, false);
	}

	// Get All Data
	public HashMap<String, String> getAll_Details() {
		HashMap<String, String> data = new HashMap<String, String>();

		data.put(KEY_FIRST_NAME_GET, pref.getString(KEY_FIRST_NAME_SET, ""));
		data.put(KEY_LAST_NAME_GET, pref.getString(KEY_FIRST_NAME_SET, ""));

		data.put(KEY_DATE_GET, pref.getString(KEY_DATE_SET, ""));

		if (pref.getBoolean(KEY_TERMS_SET, false))
			data.put(KEY_TERMS_GET, "true");
		else
			data.put(KEY_TERMS_GET, "false");

		if (pref.getBoolean(KEY_CONTACT_SET, false))
			data.put(KEY_CONTACT_GET, "true");
		else
			data.put(KEY_CONTACT_GET, "false");

		if (pref.getBoolean(KEY_GENDER_SET, false))
			data.put(KEY_GENDER_SET, "true");
		else
			data.put(KEY_GENDER_SET, "false");

		return data;
	}

	public void clearRegistrationDetails() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

	}

}
