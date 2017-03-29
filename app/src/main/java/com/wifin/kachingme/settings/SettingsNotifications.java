package com.wifin.kachingme.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.wifin.kaching.me.ui.R;

public class SettingsNotifications extends PreferenceFragment
{
    Preference reset;
    ListPreference vibrate, Light;
    RingtonePreference ring_tone;
    Context contextNotify;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_notifications);

        contextNotify = getActivity();

        reset = getPreferenceManager().findPreference("notification_reset");
        vibrate = (ListPreference) getPreferenceManager().findPreference("notify_vibrate_length");
        Light = (ListPreference) getPreferenceManager().findPreference("notify_light_color");
        ring_tone = (RingtonePreference) getPreferenceManager().findPreference("notify_ringtone");

        reset.setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Alert();
                return false;
            }
        });
    }

    protected void Alert()
    {
        AlertDialog.Builder b = new AlertDialog.Builder(contextNotify);
        b.setMessage("Are you sure you want to reset notification's setting ?").setCancelable(false);
        b.setNegativeButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                vibrate.setValue("1");
                Light.setValue("6");
                ring_tone.setOrder(1);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(contextNotify);
                builder.setSound(Uri.parse("content://settings/system/notification_sound"));
                ring_tone.setDefaultValue("1");
                ring_tone.setShowDefault(true);
                ring_tone.setEnabled(true);

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(contextNotify);
                Uri configuredUri = Uri.parse(sharedPrefs.getString("notify_ringtone", Settings.System.DEFAULT_RINGTONE_URI.toString()));
                if (!configuredUri.equals(Settings.System.DEFAULT_RINGTONE_URI))
                {
                    sharedPrefs.edit().putString("notify_ringtone", Settings.System.DEFAULT_RINGTONE_URI.toString()).commit();
                }
            }
        });

        b.setPositiveButton("NO", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {

            }
        });

        b.setCancelable(true);

        AlertDialog alert = b.create();
        alert.show();
    }
}