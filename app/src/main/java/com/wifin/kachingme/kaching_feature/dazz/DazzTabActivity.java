package com.wifin.kachingme.kaching_feature.dazz;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/2/2016.
 */
public class DazzTabActivity extends HeaderActivity implements TabLayout.OnTabSelectedListener {

    public static Activity mDazzTabActivity;
    public static DazzTabActivity mActivity;
    TabLayout tabLayout;
    int height, width;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.dazz_tab, vg);

        mActivity = this;
        mHeading.setText("Library");
        mHeaderImg.setImageResource(R.drawable.dazz);
        mNextBtn.setVisibility(View.GONE);
        mFooterLayout.setVisibility(View.GONE);
        mDazzTabActivity = this;
        screenResolution();
        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);
        //Creating our pager adapter
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        mBackBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Intent intent = new Intent(DazzTabActivity.this, DazzPlainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

    }

    private void screenResolution() {
        width = Constant.screenWidth;
        height = Constant.screenHeight;
        Constant.screenWidth = width;
        Constant.screenHeight = height;

        LinearLayout.LayoutParams mAddBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mAddBtnParams.width = (int) width * 15 / 100;
        mAddBtnParams.height = (int) height * 8 / 100;
        mAddBtnParams.rightMargin = width * 2 / 100;
        mHeaderImg.setLayoutParams(mAddBtnParams);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DazzActivity(), "Plain");
        adapter.addFragment(new LEDLibrary(), "LED");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        Constant.printMsg("selected tab position :::::::::: " + tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Constant.printMsg("selected tab position ::::::::::111 " + position);
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            Constant.printMsg("selected tab position ::::::::::111 " + title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
