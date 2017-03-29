package com.wifin.kachingme.applications;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SherlockFragmentBaseActivity extends AppCompatActivity {
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
            System.exit(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        globalApplication.onActivityPaused(this);

    }

}