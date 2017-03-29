package com.wifin.kachingme.settings;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.BasePreferenceActivity;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.social_media.TellFriend;
import com.wifin.kachingme.util.Constant;

public class Settings extends BasePreferenceActivity /*
													 * extends SherlockActivity
													 * implements
													 * OnClickListener
													 */{

	/* Button btn_chat_settings; */
	DatabaseHelper dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// addPreferencesFromResource(R.xml.settings);

		/*
		 * setContentView(R.layout.settings);
		 * 
		 * btn_chat_settings=(Button)findViewById(R.id.btn_chat_settings);
		 * 
		 * btn_chat_settings.setOnClickListener(this);
		 */

		final Toolbar toolbar = getToolbar();
		toolbar.setTitle(R.string.settings);
		Preference pf1 = findPreference("network_status");
		dbAdapter = KachingMeApplication.getDatabaseAdapter();

		if (KachingMeApplication.getsharedpreferences().getBoolean(Constant.NETWORK_STATUS, false))
        {
			Preference pf = findPreference("network_status");
			pf.setSummary("Connected");
		}
        else
        {
			Preference pf = findPreference("network_status");
			pf.setSummary("Not Connected");
		}
	}

	// @Override
	// public void onClick(View v) {
	// Intent intent;
	// switch (v.getId()) {
	// case R.id.btn_chat_settings:
	// intent = new Intent(this, Chat_Settings.class);
	// startActivity(intent);
	// break;
	//
	// default:
	// break;
	// }
	//
	// }

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
    {
		Intent intent;
		if (preference.getKey().equals("chat")) {
			intent = new Intent(this, Chat_Settings.class);
			startActivity(intent);
			return true;
		} else if (preference.getKey().equals("about")) {
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			return true;
		} else if (preference.getKey().equals("frnd")) {
			// inviting friend
			intent = new Intent(this, TellFriend.class);
			startActivity(intent);
			return true;
		} else if (preference.getKey().equals("web")) {
			// kaching me web
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			return true;
		} else if (preference.getKey().equals("archive")) {
			// archive
			archiveAllChats();
			return true;
		} else if (preference.getKey().equals("clearall")) {
			// intent = new Intent(this, About_Settings.class);
			// startActivity(intent);
			AlertDialog.Builder alert = new AlertDialog.Builder(Settings.this);
			alert.setTitle(getResources().getString(
					R.string.delete_all_conversastion));
			alert.setMessage(getResources().getString(
					R.string.are_sure_delete_coversastion));

			alert.setPositiveButton(getResources().getString(R.string.Ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Constant.printMsg("db apater::clear alllllllll");
							dbAdapter.setDelete_All_Messages();
							Constant.printMsg("db apater::cleared"
									+ dbAdapter);
							ArrayList<MessageGetSet> list = dbAdapter
									.getMessages_group_by_jid();
							Constant.printMsg("db apater::list" + list);
							for (int i = 0; i < list.size(); i++) {
								dbAdapter.setUpdateChat_lits(list.get(i)
										.getKey_remote_jid(), list.get(i)
										.get_id());
								Constant.printMsg("db apater::clea" + i
										+ "list:" + list + "remote::"
										+ list.get(i).getKey_remote_jid()
										+ "id:" + list.get(i).get_id());
							}
						}
					});
			alert.setNegativeButton(getResources().getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					});
			alert.show();

			return true;
		} else if (preference.getKey().equals("account")) {
			intent = new Intent(this, Settings_account.class);
			startActivity(intent);
			return true;
		} else if (preference.getKey().equals("notifications")) {
			intent = new Intent(this, SettingsNotifications.class);
			startActivity(intent);
			return true;
		} else if (preference.getKey().equals("profile")) {

			intent = new Intent(this, Profile.class);
			startActivity(intent);
			return true;
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		/*
		 * Intent intent;
		 * 
		 * switch (item.getItemId()) {
		 * 
		 * case android.R.id.home:
		 * 
		 * intent = new Intent(settings.this, SliderTesting.class);
		 * startActivity(intent); finish();
		 */

		Intent myIntent = new Intent(getApplicationContext(), SliderTesting.class);
		startActivity(myIntent);
		finish();

		return true;
		// }
		// return super.onOptionsItemSelected(item);
	}

	@Override
	protected int getPreferencesXmlId() {
		// TODO Auto-generated method stub
		return R.xml.settings;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		registerReceiver(broadcast, new IntentFilter(Constant.NETWORK_STATUS
				+ "_connection"));
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterReceiver(broadcast);
		super.onPause();
	}

	BroadcastReceiver broadcast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Boolean status = intent.getExtras().getBoolean("status");
			if (status) {
				Preference pf = findPreference("network_status");
				pf.setSummary("Connected");
			} else {
				Preference pf = findPreference("network_status");
				pf.setSummary("Not Connected");
			}
		}

	};

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent i = new Intent(Settings.this, SliderTesting.class);
		startActivity(i);
		finish();
	}

	private void archiveAllChats() {
		// TODO Auto-generated method stub
		AlertDialog.Builder alert = new AlertDialog.Builder(Settings.this);
		alert.setTitle(getResources().getString(
				R.string.archive_all_conversastion));
		alert.setMessage(getResources().getString(
				R.string.are_sure_archieve_coversastion));

		alert.setPositiveButton(getResources().getString(R.string.Ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Constant.printMsg("db apater::archive alllllllll");
						Constant.printMsg("db apater::archive" + dbAdapter);
						// update archive all chats,.
						// set flag ==1;
						ArrayList<MessageGetSet> list = dbAdapter
								.getMessages_group_by_jid();
						Constant.printMsg("test list is::" + list
								+ "dbapater get value is::"
								+ dbAdapter.getMessages_group_by_jid());
						Constant.printMsg("db apater::list" + list);
						for (int i = 0; i < list.size(); i++) {
							dbAdapter.setUpdateArchive(list.get(i)
									.getKey_remote_jid());
							Constant.printMsg("test db apater::archive" + i
									+ "list:" + list + "remote::"
									+ list.get(i).getKey_remote_jid() + "id:"
									+ list.get(i).get_id());
						}
					}
				});
		alert.setNegativeButton(getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});
		alert.show();

	}

}
