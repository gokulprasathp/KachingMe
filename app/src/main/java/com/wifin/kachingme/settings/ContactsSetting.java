package com.wifin.kachingme.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;

import org.jivesoftware.smackx.privacy.PrivacyList;
import org.jivesoftware.smackx.privacy.PrivacyListManager;
import org.jivesoftware.smackx.privacy.packet.PrivacyItem;

import java.util.List;

@SuppressWarnings("deprecation")
public class ContactsSetting extends PreferenceFragment
{
	int Count = 1;
	Preference pre;
	SharedPreferences sp;
	Context contextContact;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.setting_contacts);

		contextContact = getActivity();

		sp = PreferenceManager.getDefaultSharedPreferences(contextContact);

		pre = getPreferenceManager().findPreference("pre_blocked_contacts");
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
	{
		/*if (preference.getKey().equals("pre_blocked_contacts"))
		{
			Log.d("delete_account", "Delete Account Clicked...");
			Intent intent = new Intent(contextContact, blocked_users.class);
			startActivity(intent);
			return true;
		}*/
		if (preference.getKey().equals("pre_tell_friend"))
		{
			int point = sp.getInt("intropoint", 0);

			Constant.totalcontact = point;
			Constant.printMsg("contintro:;" + Constant.totalcontact);
			Constant.totalcontact = Count + Constant.totalcontact;

			Editor e = sp.edit();
			e.putInt("intropoint", Constant.totalcontact);
			e.commit();

			Intent mailer = new Intent(Intent.ACTION_SEND);
			mailer.setType("text/plain");
			mailer.putExtra("sms_body", "Check out Kaching.Me Messanger for your smartphone.\n Download it from Google play store.");
			mailer.putExtra(Intent.EXTRA_SUBJECT, "KaChing.me");
			mailer.putExtra(Intent.EXTRA_TEXT, "Hey,\n\n"
							+ "I just downloaded Kaching.Me Messanger on my Android.\n\n"
							+ "It is a smartphone messanger which replaces SMS. This app event lets me send pictures,video and other multimedia.\n\n"
							+ "Kaching.Me is available for Android and there is no PIN or username to remember - it work just like SMS and uses your internet data plan.\n\n"
							+ "Get it now from Google play and say good-bye to SMS!");
			startActivity(Intent.createChooser(mailer, "Send email..."));
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private class block_Async extends AsyncTask<String, String, String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				PrivacyListManager privacyManager = TempConnectionService.privacymanager;
				PrivacyList list = null;

				try
				{
					list = privacyManager.getPrivacyList(KachingMeApplication.getUserID());
				}
				catch (Exception e)
				{
					// //ACRA.getErrorReporter().handleException(e);
					// TODO: handle exception
				}

				List<PrivacyItem> items = list.getItems();

				if (items.size() > 0)
				{
					pre.setTitle("Blocked contacts:" + items.size());
				}

				for (PrivacyItem privacyItem : items)
				{
					Log.d("Privacy List", privacyItem.getValue());
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				// TODO: handle exception
			}
			return null;
		}
	}
}