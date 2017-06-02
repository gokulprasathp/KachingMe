package com.wifin.kachingme.kaching_feature.karaoke;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.KaraokeAdapter_List;
import com.wifin.kachingme.applications.OnItemClickListenerInterface;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class KaraokeListActivity extends AppCompatActivity implements OnItemClickListenerInterface
{
    int currentapiVersion;
    public ViewPager viewPager;
    int height = 0, width = 0;
    public static String url;

    TabLayout tabLayout;
    AppBarLayout appBarLayout;

    public static KaraokeListActivity mActivity;
    public static KaraokeListActivity mParentActivity;

    private static final String TAMIL = "Tamil";
    private static final String ENGLISH = "English";
    private static final String HINDI = "Hindi";

    static int mTabStatus = 0;

    Bundle bundlePosition;
    int tabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karaoke_list_activity);
        getSupportActionBar().hide();
        mActivity=this;

        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.red_status_bar));
        }

        mParentActivity = this;
        initializeSongs();
        mScreenArrangement();

        bundlePosition = getIntent().getExtras();

        if (bundlePosition != null)
        {
            tabPosition = bundlePosition.getInt("tabposition");
        }

        currentapiVersion = Build.VERSION.SDK_INT;
        appBarLayout = (AppBarLayout) findViewById(R.id.tabanim_appbar);

        viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayoutCustomDesign();

        if (bundlePosition != null)
        {
            viewPager.setCurrentItem(tabPosition);

            Constant.printMsg("Selected Song 2 :: " + " Tab Pos " + tabPosition + " View " + viewPager.getCurrentItem() + " Position " + tabLayout.getSelectedTabPosition());
        }
        //viewPager.setCurrentItem(0);

        if (tabLayout.getSelectedTabPosition() == 0)
        {
            mTabStatus = 1;
        }
        else if (tabLayout.getSelectedTabPosition() == 1)
        {
            mTabStatus = 2;
        }
        else if (tabLayout.getSelectedTabPosition() == 2)
        {
            mTabStatus = 3;
        }

        Constant.printMsg("Selected Song 0 :: " + " Tab Pos " + tabPosition + " View " + viewPager.getCurrentItem() + " Position " + tabLayout.getSelectedTabPosition() + " Tab Lang " + mTabStatus);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());
                View view = tabLayout.getTabAt(tab.getPosition()).getCustomView();
                TextView textView = (TextView) view.findViewById(R.id.tab_title);
                textView.setTypeface(null, Typeface.NORMAL);

                if (tabLayout.getSelectedTabPosition() == 0)
                {
                    mTabStatus = 1;
                }
                else if (tabLayout.getSelectedTabPosition() == 1)
                {
                    mTabStatus = 2;
                }
                else if (tabLayout.getSelectedTabPosition() == 2)
                {
                    mTabStatus = 3;
                }

                Constant.printMsg("Selected Song 1 :: " + " Tab Pos " + tabPosition + " View " + viewPager.getCurrentItem() + " Position " + tabLayout.getSelectedTabPosition() + " Tab Lang " + mTabStatus);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                View view = tabLayout.getTabAt(tab.getPosition()).getCustomView();
                TextView textView = (TextView) view.findViewById(R.id.tab_title);
                textView.setTypeface(null, Typeface.BOLD);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DummyFragment(ContextCompat.getColor(this, R.color.accent_material_light), TAMIL), TAMIL);
        adapter.addFrag(new DummyFragment(ContextCompat.getColor(this, R.color.ripple_material_light), ENGLISH), ENGLISH);
        adapter.addFrag(new DummyFragment(ContextCompat.getColor(this, R.color.button_material_dark), HINDI), HINDI);
        viewPager.setAdapter(adapter);
    }

    @SuppressWarnings("deprecation")
    public void tabLayoutCustomDesign()
    {
        for (int i = 0; i < tabLayout.getTabCount(); i++)
        {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.karaoke_tab, tabLayout, false);

            if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN)
            {
                if (i == 0)
                    relativeLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.karaoke_rectangle_left));
                else if (i == 1)
                    relativeLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.karaoke_rectangel));
                else if (i == 2)
                    relativeLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.karaoke_rectangle_right));
            }
            else
            {
                if (i == 0)
                    relativeLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.karaoke_rectangle_left));
                else if (i == 1)
                    relativeLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.karaoke_rectangel));
                else if (i == 2)
                    relativeLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.karaoke_rectangle_right));
            }

            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title);
            tabTextView.setText(tab.getText());

            if (width >= 600)
            {
                tabTextView.setTextSize(16);

            }
            else if (width < 600 && width >= 480)
            {
                tabTextView.setTextSize(13);

            }
            else if (width < 480 && width >= 320)
            {
                tabTextView.setTextSize(10);

            }
            else if (width < 320)
            {
                tabTextView.setTextSize(8);
            }
            tab.setCustomView(relativeLayout);
            tab.select();
        }
        Constant.printMsg("Selected Song 3 :: " + mTabStatus + " " + tabLayout.getSelectedTabPosition());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view, int position)
    {
        if(mTabStatus==1)
        {
            Constant.mSong = mTamilSongsList.get(position).getSongName();
            Constant.url =  mTamilSongsList.get(position).getUrl();
        }
            else if(mTabStatus==2)
        {
            Constant.mSong = mEnglishSongsList.get(position).getSongName();
            Constant.url =  mEnglishSongsList.get(position).getUrl();
        }
            else if(mTabStatus==3)
        {
            Constant.mSong = mHindiSongsList.get(position).getSongName();
            Constant.url =  mHindiSongsList.get(position).getUrl();
        }

        if(Constant.url!=null)
        {
            Constant.printMsg("Selected Song :: " + Constant.url + " " + Constant.mSong + " Tab Status " + mTabStatus);
            Intent i = new Intent(KaraokeListActivity.this, KaraokeYoutubeActivity.class).putExtra("tabselected", viewPager.getCurrentItem());
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onLongClick(View view, int position)
    {

    }

    static class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager)
        {
            super(manager);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitleList.get(position);
        }
    }

    public static class DummyFragment extends Fragment
    {
        int color;
        KaraokeAdapter_List adapter;
        String langType;

        public DummyFragment()
        {

        }

        @SuppressLint("ValidFragment")
        public DummyFragment(int color, String langType)
        {
            this.color = color;
            this.langType = langType;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.karaoke_fragment, container, false);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            if (langType.equalsIgnoreCase(TAMIL))
            {
                adapter = new KaraokeAdapter_List(mTamilSongsList, mParentActivity);
            }
            else if (langType.equalsIgnoreCase(ENGLISH))
            {
                adapter = new KaraokeAdapter_List(mEnglishSongsList, mParentActivity);
            }
            else if (langType.equalsIgnoreCase(HINDI))
            {
                adapter = new KaraokeAdapter_List(mHindiSongsList, mParentActivity);
            }
            recyclerView.setAdapter(adapter);
            return view;
        }
    }

    public void mScreenArrangement()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        Constant.width = width;
        Constant.height = height;
    }

    static List<KaraokeSongsList> mTamilSongsList = new ArrayList<>();
    static List<KaraokeSongsList> mHindiSongsList = new ArrayList<>();
    static List<KaraokeSongsList> mEnglishSongsList = new ArrayList<>();

    public void initializeSongs() {

        if(mTamilSongsList.size()==0)
        {
            mTamilSongsList.add(new KaraokeSongsList("Moongil Thottam", "IBYqEi7kzrc"));
            mTamilSongsList.add(new KaraokeSongsList("Idhu Varai", "cS6BghmRzi0"));
            mTamilSongsList.add(new KaraokeSongsList("Amma Endrazhaikaadha", "Vc3Jih4vGnc"));
            mTamilSongsList.add(new KaraokeSongsList("Kannazhaga", "PRVEU0fGJfI"));
            mTamilSongsList.add(new KaraokeSongsList("Mandram Vandha", "VHu29D-FqB8"));
        }

        if(mEnglishSongsList.size()==0)
        {
            mEnglishSongsList.add(new KaraokeSongsList("Smack that", "F9m87qSxuWk"));
            mEnglishSongsList.add(new KaraokeSongsList("Lonely", "tVjikUbIOa0"));
            mEnglishSongsList.add(new KaraokeSongsList("Show Me The Meaning", "TJpkd2og0NY"));
        }

        if(mHindiSongsList.size()==0)
        {
            mHindiSongsList.add(new KaraokeSongsList("Jiya re", "mDqEW7MfYb4"));
            mHindiSongsList.add(new KaraokeSongsList("tujh mein rab dikhta", "0wE21a_udO4"));
            mHindiSongsList.add(new KaraokeSongsList("HAULE HAULE HO JAYEGA", "4is68dyAuq0"));
        }
    }

    public class KaraokeSongsList
    {
        private String songName;
        private String url;

        public KaraokeSongsList(String name, String url)
        {
            this.songName = name;
            this.url = url;
        }

        public String getSongName()
        {
            return songName;
        }

        public void setSongName(String songName)
        {
            this.songName = songName;
        }

        public String getUrl()
        {
            return url;
        }

        public void setUrl(String url)
        {
            this.url = url;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
