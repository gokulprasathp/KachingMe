/*
* @author Sivanesan
*
* @usage -  This class is used to display app first screen to explain app concept in slide show
*
*
* */

package com.wifin.kachingme.adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wifin.kachingme.registration_and_login.SliderFragment;

public class SlideShowAdapter extends FragmentPagerAdapter {


    public SlideShowAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SliderFragment.newInstance(0, "Page # 1");
            case 1:
                return SliderFragment.newInstance(1, "Page # 2");
            case 2:
                return SliderFragment.newInstance(2, "Page # 3");
            case 3:
                return SliderFragment.newInstance(3, "Page # 1");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}