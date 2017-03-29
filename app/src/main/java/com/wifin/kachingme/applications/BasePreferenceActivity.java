package com.wifin.kachingme.applications;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.wifin.kachingme.emojicons.action_bar_preference_activity_library.ActionBarPreferenceActivity;

public class BasePreferenceActivity extends ActionBarPreferenceActivity {
    SharedPreferences sp;
    private KachingMeApplication globalApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        globalApplication = (KachingMeApplication) getApplication();
        sp = getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        globalApplication.onActivityResumed(this);


        if (!sp.contains("pin")) {
            finish();
        }

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

    @Override
    protected int getPreferencesXmlId() {
        // TODO Auto-generated method stub
        return 0;
    }
}