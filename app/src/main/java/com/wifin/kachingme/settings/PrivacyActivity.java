package com.wifin.kachingme.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.PrivacyAdapter;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class PrivacyActivity extends Activity
{
    List<String> privacyListTitle, privacyListSubs;
    String[] privacyTitleValue, privacySubsValue;
    String TAG = PrivacyActivity.class.getSimpleName();
    Context contextPrivacy;
    RecyclerView recyclerPrivacy;
    AppBarLayout privacyAppBar;
    Toolbar privacyToolBar;
    FrameLayout privacyFrameLayout;
    ImageView privacyImageBack;
    TextView privacyTextTitle, tvPrivacyContent;
    PrivacyAdapter privacyAdapter = new PrivacyAdapter();
    int height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_privacy);

        contextPrivacy = this;

        initPrivacy();
        screenPrivacy();

        recyclerPrivacy = (RecyclerView) findViewById(R.id.recyclerPrivacy);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerPrivacy.setLayoutManager(linearLayoutManager);
        recyclerPrivacy.setHasFixedSize(true);

        privacyListTitle = new ArrayList<>();
        privacyListSubs = new ArrayList<>();

        privacyTitleValue = contextPrivacy.getResources().getStringArray(R.array.privacy_title);
        privacySubsValue = contextPrivacy.getResources().getStringArray(R.array.privacy_subs);

        for (int privacy = 0; privacy < privacyTitleValue.length; privacy++)
        {
            privacyListTitle.add(privacyTitleValue[privacy]);
            privacyListSubs.add(privacySubsValue[privacy]);
        }

        privacyAdapter = new PrivacyAdapter(contextPrivacy, privacyListTitle, privacyListSubs);
        recyclerPrivacy.setAdapter(privacyAdapter);

        privacyImageBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }

    public void initPrivacy()
    {
        privacyAppBar = (AppBarLayout) findViewById(R.id.privacyAppBar);
        privacyToolBar = (Toolbar) findViewById(R.id.privacyToolBar);
        privacyFrameLayout = (FrameLayout) findViewById(R.id.privacyFrameLayout);
        privacyImageBack = (ImageView) findViewById(R.id.privacyImageBack);
        privacyTextTitle = (TextView) findViewById(R.id.privacyTextTitle);
        recyclerPrivacy = (RecyclerView) findViewById(R.id.recyclerPrivacy);
        tvPrivacyContent = (TextView) findViewById(R.id.tvPrivacyContent);

        Constant.typeFace(this, privacyTextTitle);
        Constant.typeFace(this, tvPrivacyContent);

    }

    public void screenPrivacy()
    {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Log.e(TAG, "Height : " + height + "\n" + "Width : " + width);

        Constant.height = height;
        Constant.width = width;

        AppBarLayout.LayoutParams toolBarPrivacy = new AppBarLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        toolBarPrivacy.width = (int) width;
        toolBarPrivacy.height = (int) height * 10 / 100;
        toolBarPrivacy.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        privacyToolBar.setLayoutParams(toolBarPrivacy);

        FrameLayout.LayoutParams buttonPrivacyBack = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonPrivacyBack.width = (int) width * 4 / 100;
        buttonPrivacyBack.height = (int) width * 6 / 100;
        buttonPrivacyBack.leftMargin = (int) width * 3 / 100;
        buttonPrivacyBack.gravity = Gravity.START | Gravity.CENTER;
        privacyImageBack.setLayoutParams(buttonPrivacyBack);

        LinearLayout.LayoutParams contentPrivacy = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        contentPrivacy.width = width;
        contentPrivacy.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        contentPrivacy.gravity = Gravity.CENTER_VERTICAL;
        int marginMsg = (int) width * 2 / 100;
        contentPrivacy.setMargins(marginMsg, marginMsg, marginMsg, marginMsg);
        tvPrivacyContent.setLayoutParams(contentPrivacy);

        if (width >= 600)
        {
            privacyTextTitle.setTextSize(19);
            tvPrivacyContent.setTextSize(18);
        }
        else if (width > 501 && width < 600)
        {
            privacyTextTitle.setTextSize(18);
            tvPrivacyContent.setTextSize(17);
        }
        else if (width > 260 && width < 500)
        {
            privacyTextTitle.setTextSize(17);
            tvPrivacyContent.setTextSize(16);
        }
        else if (width <= 260)
        {
            privacyTextTitle.setTextSize(16);
            tvPrivacyContent.setTextSize(15);
        }
    }

    public void onBackPressed()
    {
        super.onBackPressed();

        startActivity(new Intent(PrivacyActivity.this, UsageActivity.class));
    }
}
