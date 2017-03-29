package com.wifin.kachingme.chat.chat_common_classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.widget.TabHost;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.Utils;

public class ForwardList extends ActionBarActivity implements TabListener {

	TabHost mTabHost;
	ViewPager mViewPager;

	public static Context ACTIVITY;

	Handler h;
	Thread refresh;
	ProgressDialog progressdialog;

	public static String msg_ids = "";
	public static Boolean IS_EMAIL = false;
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	ActionBar actionBar;

	public static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.tabsexample);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {

			if (bundle.getString("msg_ids") != null) {

				msg_ids = bundle.getString("msg_ids");
				Log.i("Forword List", "Message IDS " + msg_ids);
				IS_EMAIL = false;
			} else if (bundle.getString("email") != null) {
				IS_EMAIL = true;
			}
		}

		// doBindService();
		ACTIVITY = this;
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		ACTIVITY = this;

		Utils.setHasEmbeddedTabs(actionBar, false);

		context = this;
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {

						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {

			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		String tab_title[] = {
				context.getResources().getString(R.string.chats),
				context.getResources().getString(R.string.groups),
				context.getResources().getString(R.string.contacts) };
		Fragment tab_fragment[] = { new ForwardCurrent(), new ForwardGroup(),
				new ForwardContacts() };

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:

				return tab_fragment[0];
			case 1:

				return tab_fragment[1];
			case 2:

				return tab_fragment[2];

			default:

				Fragment fragment = tab_fragment[1];

				return fragment;
			}
		}

		@Override
		public int getCount() {
			return tab_fragment.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tab_title[position];
		}
	}

	/*
	 * private ServiceConnection mConnection = new ServiceConnection() { public
	 * void onServiceConnected(ComponentName className, IBinder service) {
	 *
	 * mBoundService = ((KaChingMeService.LocalBinder) service) .getService();
	 * connection = mBoundService.getConnection();
	 *
	 *
	 * Log.d("signin","Service Connected!!!!");
	 * Log.d("signin","is Connedted::"+connection.isConnected());
	 *
	 * }
	 *
	 * public void onServiceDisconnected(ComponentName className) {
	 *
	 * mBoundService = null; } };
	 *
	 * void doBindService() {
	 *
	 * bindService(new Intent(forward_list.this, KaChingMeService.class),
	 * mConnection, Context.BIND_AUTO_CREATE); isBound = true; }
	 *
	 * void doUnbindService() { if (isBound) {
	 *
	 * unbindService(mConnection); isBound = false; } }
	 */

	@Override
	protected void onDestroy() {
		// doUnbindService();
		super.onDestroy();
	}

}
