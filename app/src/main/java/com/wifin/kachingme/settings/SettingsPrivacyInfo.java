package com.wifin.kachingme.settings;

import java.util.HashMap;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.v7.widget.Toolbar;

import com.wifin.kachingme.applications.BasePreferenceActivity;
import com.wifin.kaching.me.ui.R;

public class SettingsPrivacyInfo extends BasePreferenceActivity {
	ListPreference lastseen, profilePic, status;
	SharedPrefPrivacy pref;
	String last_seen_data = null;
	String profile_pic = null;
	String status_data = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// addPreferencesFromResource(R.xml.setting_notifications);
		pref = new SharedPrefPrivacy(this);
		final Toolbar toolbar = getToolbar();
		toolbar.setTitle("Privacy");
		lastseen = (ListPreference) findPreference("notify_lastseen_length");
		profilePic = (ListPreference) findPreference("notify_profile_pic_length");
		status = (ListPreference) findPreference("notify_status_length");
		getAllDetails_pref();
		lastseen.setValue(last_seen_data);
		profilePic.setValue(profile_pic);
		status.setValue(status_data);

		lastseen.setSummary(getLabelData(last_seen_data));
		profilePic.setSummary(getLabelData(profile_pic));
		status.setSummary(getLabelData(status_data));

		// Toast.makeText(this, ""+lastseen+"--"_profile_, duration)
		lastseen.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub

				String valu = lastseen.getValue();
				pref.setLastSeen(valu);
				lastseen.setSummary(getLabelData(valu));
				return true;
			}

		});

		lastseen.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				// TODO Auto-generated method stub
				// String valu = lastseen.getValue();
				// pref.setLastSeen(valu);
				// lastseen.setSummary(getLabelData(valu));
				String valu = newValue.toString();
				pref.setLastSeen(valu);
				lastseen.setSummary(getLabelData(valu));

				return true;
			}
		});

		profilePic
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						// TODO Auto-generated method stub

						pref.setProfilePic(profilePic.getValue());
						profilePic.setSummary(getLabelData(profilePic
								.getValue()));
						return true;
					}

				});
		profilePic
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						// TODO Auto-generated method stub
						// String valu = lastseen.getValue();
						// pref.setLastSeen(valu);
						// lastseen.setSummary(getLabelData(valu));
						String valu = newValue.toString();
						pref.setProfilePic(valu);
						profilePic.setSummary(getLabelData(valu));

						return true;
					}
				});
		status.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub

				pref.setStatus(status.getValue());
				status.setSummary(getLabelData(status.getValue()));
				return true;
			}

		});
		status.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				// TODO Auto-generated method stub
				// String valu = lastseen.getValue();
				// pref.setLastSeen(valu);
				// lastseen.setSummary(getLabelData(valu));
				String valu = newValue.toString();
				pref.setStatus(valu);
				status.setSummary(getLabelData(valu));

				return true;
			}
		});
	}

	public void getAllDetails_pref() {
		HashMap<String, String> map = pref.getAll_Details();

		last_seen_data = map.get(SharedPrefPrivacy.KEY_LAST_SEEN_GET);
		profile_pic = map.get(SharedPrefPrivacy.KEY_PROFILE_PIC_GET);
		status_data = map.get(SharedPrefPrivacy.KEY_STATUS_GET);
	}

	public String getLabelData(String data) {
		if (data.equalsIgnoreCase(SharedPrefPrivacy.EVERY_ONE)) {
			return "Everyone";
		} else if (data.equalsIgnoreCase(SharedPrefPrivacy.STATUS)) {
			return "Nobody";

		} else {
			return "My Contacts";
		}
	}

	@Override
	protected int getPreferencesXmlId() {
		// TODO Auto-generated method stub
		return R.xml.setting_privacy_info;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}