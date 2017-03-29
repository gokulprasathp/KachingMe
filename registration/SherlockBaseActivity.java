package com.wifin.kachingme.registration_and_login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wifin.kachingme.applications.NiftyApplication;
/**
 * Created by Wifintech on 12-Sep-16.
 */
public class SherlockBaseActivity extends AppCompatActivity {
	private NiftyApplication globalApplication;
	private static String TAG = SherlockBaseActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/* globalApplication = (NiftyApplication) getApplication(); */
	}

	@Override
	protected void onResume() {
		super.onResume();
		// globalApplication.onActivityResumed(this);

		// Log.d(TAG,"IS Resume::"+globalApplication.isApplicationBroughtToBackground());
		// service.SendOnline();

	}

	@Override
	protected void onPause() {
		super.onPause();
		/*
		 * globalApplication.onActivityPaused(this);
		 * Log.d(TAG,"IS Paused::"+globalApplication
		 * .isApplicationBroughtToBackground()); service.SendOffline();
		 */
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}