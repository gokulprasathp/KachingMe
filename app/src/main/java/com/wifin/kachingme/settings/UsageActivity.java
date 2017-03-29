package com.wifin.kachingme.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.UsageAdapters;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class UsageActivity extends HeaderActivity {
    List<String> usageItem;
    String[] usageItems;
    Context contextUsage;
    RecyclerView recyclerUsage;
    UsageAdapters usageAdapters = new UsageAdapters();
    Toolbar usageToolbar;
    AppBarLayout usageAppBarLayout;
    ImageView imgUsageBack;
    FrameLayout frameUsageMenu;
    int height, width;
    TextView tvTitleAccSetting;
    String TAG = UsageActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.activity_usage, vg);
        mHeaderLayout.setVisibility(View.GONE);
        mFooterLayout.setVisibility(View.GONE);
        mFooterView.setVisibility(View.GONE);
        contextUsage = this;

        initUsage();
        screenUsage();

        recyclerUsage = (RecyclerView) findViewById(R.id.recyclerUsage);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerUsage.setLayoutManager(linearLayoutManager);
        recyclerUsage.setHasFixedSize(true);

        usageItem = new ArrayList<>();
        usageItems = contextUsage.getResources().getStringArray(R.array.usage_items);

        for (int use = 0; use < usageItems.length; use++) {
            usageItem.add(usageItems[use]);
        }

        usageAdapters = new UsageAdapters(contextUsage, usageItem);
        recyclerUsage.setAdapter(usageAdapters);

        imgUsageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initUsage() {
        usageToolbar = (Toolbar) findViewById(R.id.usageToolbar);
        usageAppBarLayout = (AppBarLayout) findViewById(R.id.usageAppBarLayout);
        imgUsageBack = (ImageView) findViewById(R.id.imgUsageBack);
        frameUsageMenu = (FrameLayout) findViewById(R.id.frameUsageMenu);
        recyclerUsage = (RecyclerView) findViewById(R.id.recyclerUsage);
        tvTitleAccSetting = (TextView) findViewById(R.id.tvTitleAccSetting);

        Constant.typeFace(this, tvTitleAccSetting);

    }

    public void screenUsage() {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Log.e(TAG, "Height : " + height + "\n" + "Width : " + width);

        Constant.height = height;
        Constant.width = width;

        AppBarLayout.LayoutParams toolBarUsage = new AppBarLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        toolBarUsage.width = (int) width;
        toolBarUsage.height = (int) height * 10 / 100;
        toolBarUsage.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        usageToolbar.setLayoutParams(toolBarUsage);

        Log.e(TAG, " " + toolBarUsage.width + " " + toolBarUsage.height);

        FrameLayout.LayoutParams buttonUsageBack = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonUsageBack.width = width * 6 / 100;
        buttonUsageBack.height = width * 6 / 100;
        buttonUsageBack.leftMargin = width * 4 / 100;
        buttonUsageBack.gravity = Gravity.START | Gravity.CENTER;
        imgUsageBack.setLayoutParams(buttonUsageBack);

        Log.e(TAG, "" + buttonUsageBack.width + " " + buttonUsageBack.height + " " + buttonUsageBack.leftMargin);

        android.support.v4.widget.DrawerLayout.LayoutParams linearContentLayout = new android.support.v4.widget.DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearContentLayout.width = width;
        linearContentLayout.height = height;
        mContentLayout.setLayoutParams(linearContentLayout);

        LinearLayout.LayoutParams ft = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ft.height = height * 8 / 100;
        ft.gravity = Gravity.CENTER | Gravity.BOTTOM;
        mFooterLayout.setLayoutParams(ft);

        LinearLayout.LayoutParams logolay1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        logolay1.width = (int) (width * 30.5 / 100);
        logolay1.height = height * 11 / 100;
        logolay1.setMargins(width * 1 / 100, (int) (height * 1.5 / 100), width * 1 / 100, 0);
        logolay1.gravity = Gravity.CENTER;
        logolay1.topMargin = height * 2 / 100;
        mChatLayout.setLayoutParams(logolay1);
        mBuxLayout.setLayoutParams(logolay1);
        mCartLayout.setLayoutParams(logolay1);

        LinearLayout.LayoutParams img = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        img.height = height * 4 / 100;
        img.gravity = Gravity.CENTER;
        mChatImg.setLayoutParams(img);
        mBuxSImg.setLayoutParams(img);
        mCartImg.setLayoutParams(img);

        LinearLayout.LayoutParams imgtext = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        imgtext.height = height * 4 / 100;
        imgtext.gravity = Gravity.CENTER;
        mChatText.setLayoutParams(imgtext);
        mBuxText.setLayoutParams(imgtext);
        mCartText.setLayoutParams(imgtext);

        if (width >= 600) {
            tvTitleAccSetting.setTextSize(18);
        } else if (width > 501 && width < 600) {
            tvTitleAccSetting.setTextSize(17);
        } else if (width > 260 && width < 500) {
            tvTitleAccSetting.setTextSize(16);
        } else if (width <= 260) {
            tvTitleAccSetting.setTextSize(15);
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(UsageActivity.this, SettingsActivity.class));
        finish();
        super.onBackPressed();
    }
}
