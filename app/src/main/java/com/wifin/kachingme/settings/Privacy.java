package com.wifin.kachingme.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;

import com.wifin.kachingme.applications.BasePreferenceActivity;
import com.wifin.kachingme.util.Log;
import com.wifin.kaching.me.ui.R;

public class Privacy extends BasePreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// addPreferencesFromResource(R.xml.setting_privacy);
		final Toolbar toolbar = getToolbar();
		toolbar.setTitle(R.string.privacy);

	}

	@Override
	protected int getPreferencesXmlId() {
		// TODO Auto-generated method stub
		return R.xml.setting_privacy;
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		if (preference.getKey().equals("Default Scroll")) {

			Log.d("Default Scroll", "Default Scroll Clicked...");
			// Intent intent=new Intent(this,Delete_Account_Confirm.class);
			// startActivity(intent);
			//
			// return true;
		}
		if (preference.getKey().equals("delete_account")) {

			Log.d("delete_account", "Delete Account Clicked...");
			Intent intent = new Intent(this, Delete_Account_Confirm.class);
			startActivity(intent);

			return true;
		}

		return super.onPreferenceTreeClick(preferenceScreen, preference);

	}

}