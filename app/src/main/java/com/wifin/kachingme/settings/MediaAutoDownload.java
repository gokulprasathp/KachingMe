package com.wifin.kachingme.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.wifin.kaching.me.ui.R;

import static android.content.Context.MODE_WORLD_READABLE;

@SuppressWarnings("deprecation")
public class MediaAutoDownload extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_media_auto_download);

        final CheckBoxPreference mImagePref = (CheckBoxPreference) getPreferenceManager().findPreference("media_auto_download_images");
        final CheckBoxPreference mVideoPref = (CheckBoxPreference) getPreferenceManager().findPreference("media_auto_download_videos");
        final CheckBoxPreference mAudioPref = (CheckBoxPreference) getPreferenceManager().findPreference("media_auto_download_audio");
        //final CheckBoxPreference mFilesPref = (CheckBoxPreference) getPreferenceManager().findPreference("media_auto_download_files");

        mImagePref.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                java.util.Date date = new java.util.Date();
                SharedPreferences myPrefs = getActivity().getSharedPreferences("img_pref", MODE_WORLD_READABLE);
                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putString("image_time", date.toString());
                prefsEditor.commit();
                return true;
            }
        });

        mVideoPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                java.util.Date date = new java.util.Date();

                SharedPreferences myPrefs = getActivity().getSharedPreferences("video_pref", MODE_WORLD_READABLE);
                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putString("video_time", date.toString());
                prefsEditor.commit();
                return true;
            }
        });

        mAudioPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                java.util.Date date = new java.util.Date();

                SharedPreferences myPrefs = getActivity().getSharedPreferences("audio_pref", MODE_WORLD_READABLE);
                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putString("audio_time", date.toString());
                prefsEditor.commit();
                return true;
            }
        });

        /*mFilesPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                java.util.Date date = new java.util.Date();
                SharedPreferences myPrefs = getActivity().getSharedPreferences("file_pref", MODE_WORLD_READABLE);
                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                prefsEditor.putString("file_time", date.toString());
                prefsEditor.commit();
                return true;
            }
        });*/
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
    {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}