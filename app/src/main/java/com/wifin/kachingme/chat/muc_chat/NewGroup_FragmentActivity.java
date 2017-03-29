package com.wifin.kachingme.chat.muc_chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.SherlockFragmentBaseActivity;
import com.wifin.kachingme.chat.broadcast_chat.Broadcast_create;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.util.Utils;

import java.util.ArrayList;

public class NewGroup_FragmentActivity extends SherlockFragmentBaseActivity
        implements ActionBar.TabListener {

    public static ViewPager mViewPager;
    public static Context ACTIVITY;
    public static Boolean IS_FRONT = false;
    public static Context context;
    TabsAdapter mTabsAdapter;
    Handler h;
    Thread refresh;
    ProgressDialog progressdialog;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    ActionBar actionBar;

    @Override
    protected void onStop() {
        IS_FRONT = false;
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Broadcast_create.selected_users = new ArrayList<ContactsGetSet>();
        setContentView(R.layout.tabsexample);
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        ACTIVITY = this;
        Utils.setHasEmbeddedTabs(actionBar, false);
        context = this;
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
                getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {

                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {

            actionBar.addTab(actionBar.newTab()
                    .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    @Override
    protected void onResume() {
        IS_FRONT = true;
        // doBindService();
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        // doUnbindService();
        super.onDestroy();
    }

    // private ServiceConnection mConnection = new ServiceConnection() {
    // public void onServiceConnected(ComponentName className, IBinder service)
    // {
    //
    // mBoundService = ((KaChingMeService.LocalBinder) service)
    // .getService();
    // connection = mBoundService.getConnection();
    //
    // try {
    // Presence presence = new Presence(Presence.Type.available);
    // presence.setMode(Mode.available);
    //
    // connection.sendStanza(presence);
    //
    // } catch (Exception e) {// ACRA.getErrorReporter().handleException(e);
    // // TODO: handle exception
    // }
    //
    // /*
    // * Log.d("signin","Service Connected!!!!");
    // * Log.d("signin","is Connedted::"+connection.isConnected());
    // */
    // }
    //
    // public void onServiceDisconnected(ComponentName className) {
    //
    // mBoundService = null;
    // }
    // };
    //
    // void doBindService() {
    //
    // bindService(new Intent(NewGroup_FragmentActivity.this,
    // KaChingMeService.class), mConnection, Context.BIND_AUTO_CREATE);
    // isBound = true;
    // }
    //
    // void doUnbindService() {
    // if (isBound) {
    //
    // unbindService(mConnection);
    // isBound = false;
    // }
    // }

    public void onBackPressed() {

        Intent i = new Intent(this, SliderTesting.class);
        startActivity(i);
        finish();

    }

	/*
     * @Override protected void onSaveInstanceState(Bundle outState) {
	 * super.onSaveInstanceState(outState); outState.putInt("tab",
	 * mTabHost.getCurrentTab()); outState.putString("tab",
	 * mTabHost.getCurrentTabTag()); }
	 * 
	 * @Override protected void onRestoreInstanceState(Bundle
	 * savedInstanceState) { // TODO Auto-generated method stub
	 * super.onRestoreInstanceState(savedInstanceState);
	 * Log.d("Chatlist","onRestoreInstanceState called..................."); if
	 * (savedInstanceState != null) {
	 * mTabHost.setCurrentTab(savedInstanceState.getInt("tab")); } }
	 */

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        String tab_title[] = {context.getResources().getString(R.string.group_info),
                context.getResources().getString(R.string.members_lists)
        };
        Fragment tab_fragment[] = {new NewGroup_Info(),
                new NewGroup_MemberList()};

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:

                    return tab_fragment[0];

                default:

                    Fragment fragment = tab_fragment[1];

                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return tab_fragment.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tab_title[position];
        }
    }

    /**
     * This is a helper class that implements the management of tabs and all
     * details of connecting a ViewPager with associated TabHost. It relies on a
     * trick. Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show. This is not sufficient for switching
     * between pages. So instead we make the content part of the tab host 0dp
     * high (it is not shown) and the TabsAdapter supplies its own dummy view to
     * show as the tab content. It listens to changes in tabs, and takes care of
     * switch to the correct paged in the ViewPager whenever the selected tab
     * changes.
     */
    public static class TabsAdapter extends FragmentPagerAdapter implements
            TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        public TabsAdapter(FragmentActivity activity, TabHost tabHost,
                           ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(),
                    info.args);
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.

            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }
    }
}
