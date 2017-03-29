package com.wifin.kachingme.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;
import com.wifin.kaching.me.ui.R;

public class TestFragmentLogin extends FragmentPagerAdapter implements
		IconPagerAdapter {
	protected static final String[] CONTENT_Title = new String[] { "This",
			"Is", "A", "Test", };
	protected static final int[] CONTENT = new int[] { R.layout.newslide1,
			R.layout.newslide2, R.layout.newslide3, R.layout.newslide4 /*
																		 * ,R.layout
																		 * .
																		 * slide_6
																		 */};
	protected static final int[] ICONS = new int[] {
	/*
	 * R.drawable.perm_group_calendar, R.drawable.perm_group_camera,
	 * R.drawable.perm_group_device_alarms, R.drawable.perm_group_location
	 */
	};

	private int mCount = CONTENT.length;

	public TestFragmentLogin(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TestFragmentLogin.CONTENT_Title[position % CONTENT.length];
	}

	@Override
	public int getIconResId(int index) {
		return ICONS[index % ICONS.length];
	}

	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}
}