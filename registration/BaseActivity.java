package com.wifin.kachingme.registration_and_login;

import android.app.Activity;
import android.os.Bundle;

import com.wifin.kachingme.applications.NiftyApplication;
/**
 * Created by Wifintech on 12-Sep-16.
 */
public class BaseActivity extends Activity {
	private NiftyApplication globalApplication;
	private static String TAG = SherlockBaseActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		globalApplication = (NiftyApplication) getApplication();
	}

	@Override
	protected void onResume() {
		super.onResume();
		globalApplication.onActivityResumed(this);

//		 Log.d(TAG, "IS Resume base::" + globalApplication.isApplicationBroughtToBackground());

	}

	@Override
	protected void onPause() {
		super.onPause();
		globalApplication.onActivityPaused(this);
//		Log.d(TAG,
//				"IS Paused base::"
//						+ globalApplication.isApplicationBroughtToBackground());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}