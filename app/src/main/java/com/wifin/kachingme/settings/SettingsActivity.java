package com.wifin.kachingme.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.wifin.kachingme.adaptors.SettingAdapter;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends Activity {
    public static RecyclerView recyclerSettingContent;
    List<String> listSettingContent;
    String[] listSettingValue;
    String TAG = SettingsActivity.class.getSimpleName();
    Context contextSettings;
    SettingAdapter settingAdapter = new SettingAdapter();
    AppBarLayout appbarSettingContent;
    Toolbar toolbarSettingContent;
    FrameLayout frameSettingContent;
    ImageView imgBackSettingCont, imgSettingTitleIcon;
    TextView tvTitleSettingCont;
    LinearLayout linearSettingTitle;
    int height, width;
    int[] listSettingIcon = {R.drawable.icon_about,
//        R.drawable.icon_tell_friend,
//        R.drawable.icon_web,
            R.drawable.icon_account,
            R.drawable.icon_chat_settings,
            R.drawable.icon_notification_settings,
            R.drawable.icon_network_status};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
        contextSettings = this;
        initSettingContent();
        screenSettingContent();
        recyclerSettingContent = (RecyclerView) findViewById(R.id.recyclerSettingContent);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerSettingContent.setLayoutManager(linearLayoutManager);
        recyclerSettingContent.setHasFixedSize(true);
        listSettingContent = new ArrayList();
        listSettingValue = contextSettings.getResources().getStringArray(R.array.settings_content_title);
        for (int setting = 0; setting < listSettingValue.length; setting++) {
            listSettingContent.add(listSettingValue[setting]);
        }
        settingAdapter = new SettingAdapter(contextSettings, listSettingContent, listSettingIcon);
        recyclerSettingContent.setItemAnimator(new DefaultItemAnimator());
        recyclerSettingContent.setAdapter(settingAdapter);
        imgBackSettingCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initSettingContent() {
        appbarSettingContent = (AppBarLayout) findViewById(R.id.appbarSettingContent);
        toolbarSettingContent = (Toolbar) findViewById(R.id.toolbarSettingContent);
        frameSettingContent = (FrameLayout) findViewById(R.id.frameSettingContent);
        imgBackSettingCont = (ImageView) findViewById(R.id.imgBackSettingCont);
        tvTitleSettingCont = (TextView) findViewById(R.id.tvTitleSettingCont);
        recyclerSettingContent = (RecyclerView) findViewById(R.id.recyclerSettingContent);
        linearSettingTitle = (LinearLayout) findViewById(R.id.linearSettingTitle);
        imgSettingTitleIcon = (ImageView) findViewById(R.id.imgSettingTitleIcon);
    }

    public void screenSettingContent() {
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
        toolBarPrivacy.height = (int) height * 8 / 100;
        toolBarPrivacy.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        toolbarSettingContent.setLayoutParams(toolBarPrivacy);

        FrameLayout.LayoutParams frameSettingTitle = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        frameSettingTitle.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        frameSettingTitle.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        frameSettingTitle.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER;
        linearSettingTitle.setLayoutParams(frameSettingTitle);

        FrameLayout.LayoutParams buttonPrivacyBack = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonPrivacyBack.width = width * 6 / 100;
        buttonPrivacyBack.height = width * 6 / 100;
        buttonPrivacyBack.leftMargin = (int) width * 4 / 100;
        buttonPrivacyBack.gravity = Gravity.START | Gravity.CENTER;
        imgBackSettingCont.setLayoutParams(buttonPrivacyBack);

        LinearLayout.LayoutParams imgTitleSetting = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        imgTitleSetting.width = (int) width * 10 / 100;
        imgTitleSetting.height = (int) width * 8 / 100;
        int margin = width * 1 / 100;
        imgTitleSetting.setMargins(margin, margin, width * 2 / 100, margin);
        imgTitleSetting.gravity = Gravity.START | Gravity.CENTER;
        imgSettingTitleIcon.setLayoutParams(imgTitleSetting);

        if (width >= 600) {
            tvTitleSettingCont.setTextSize(19);
        } else if (width > 501 && width < 600) {
            tvTitleSettingCont.setTextSize(18);
        } else if (width > 260 && width < 500) {
            tvTitleSettingCont.setTextSize(17);
        } else if (width <= 260) {
            tvTitleSettingCont.setTextSize(16);
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(SettingsActivity.this, SliderTesting.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
        super.onBackPressed();
    }
}
