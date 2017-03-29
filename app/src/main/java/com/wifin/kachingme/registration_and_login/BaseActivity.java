/*
* @author Sivanesan
*
* @usage -  This class is parent of Luncher Activity
*
* */

package com.wifin.kachingme.registration_and_login;

import android.app.Activity;
import android.os.Bundle;
import com.wifin.kachingme.applications.KachingMeApplication;

/**
 * Created by Wifintech on 12-Sep-16.
 */
public class BaseActivity extends Activity {
	private KachingMeApplication globalApplication;
	private static String TAG = SherlockBaseActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		globalApplication = (KachingMeApplication) getApplication();
	}

	@Override
	protected void onResume() {
		super.onResume();
		globalApplication.onActivityResumed(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		globalApplication.onActivityPaused(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}