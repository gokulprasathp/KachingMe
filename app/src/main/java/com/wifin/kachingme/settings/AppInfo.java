package com.wifin.kachingme.settings;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.util.Constant;

public class AppInfo extends HeaderActivity
{
    AppBarLayout appbarAppInfo;
    Toolbar toolbarAppInfo;
    FrameLayout frameAppInfo;
    ImageView imgBackAppInfo, imgAppNameInfo, imgAppIcon;
    TextView tvTitleAppInfo, tvAppCopyRight, tvContactUsInfo, tvHelpInfo, tvVersionAppInfo;
    String TAG = AppInfo.class.getSimpleName(), version;
    Context contextAppInfo;
    int height, width;
    LinearLayout linearAppInfoContent, linearContactUsAppInfo, linearHelpAppInfo, linearAppNameVer, linearAppIconInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.activity_app_info, vg);
        mHeaderLayout.setVisibility(View.GONE);
        mFooterLayout.setVisibility(View.GONE);
        mFooterView.setVisibility(View.GONE);
        contextAppInfo = this;

        PackageInfo pInfo;

        try
        {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            version = pInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        initAppInfo();

        screenAppInfo();

        tvVersionAppInfo.setText(version);

        tvContactUsInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(AppInfo.this, Contact.class));

                finish();
            }
        });

        tvHelpInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(AppInfo.this, FaqActivity.class));

                finish();
            }
        });

        imgBackAppInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }

    public void initAppInfo()
    {
        appbarAppInfo = (AppBarLayout) findViewById(R.id.appbarAppInfo);
        toolbarAppInfo = (Toolbar) findViewById(R.id.toolbarAppInfo);
        frameAppInfo = (FrameLayout) findViewById(R.id.frameAppInfo);
        imgBackAppInfo = (ImageView) findViewById(R.id.imgBackAppInfo);
        imgAppNameInfo = (ImageView) findViewById(R.id.imgAppNameInfo);
        imgAppIcon = (ImageView) findViewById(R.id.imgAppIcon);
        tvTitleAppInfo = (TextView) findViewById(R.id.tvTitleAppInfo);
        tvAppCopyRight = (TextView) findViewById(R.id.tvAppCopyRight);
        tvContactUsInfo = (TextView) findViewById(R.id.tvContactUsInfo);
        tvHelpInfo = (TextView) findViewById(R.id.tvHelpInfo);
        tvVersionAppInfo = (TextView) findViewById(R.id.tvVersionAppInfo);
        linearAppNameVer = (LinearLayout) findViewById(R.id.linearAppNameVer);
        linearAppInfoContent = (LinearLayout) findViewById(R.id.linearAppInfoContent);
        linearContactUsAppInfo = (LinearLayout) findViewById(R.id.linearContactAppInfo);
        linearHelpAppInfo = (LinearLayout) findViewById(R.id.linearHelpAppInfo);
        linearAppIconInfo = (LinearLayout) findViewById(R.id.linearAppIconInfo);

        Constant.typeFace(this,tvTitleAppInfo);
        Constant.typeFace(this,tvAppCopyRight);
        Constant.typeFace(this,tvContactUsInfo);
        Constant.typeFace(this,tvHelpInfo);
        Constant.typeFace(this,tvVersionAppInfo);
    }

    public void screenAppInfo()
    {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Log.e(TAG, "Height : " + height + "\n" + "Width : " + width);

        Constant.height = height;
        Constant.width = width;

        int margin = width * 2 / 100;

        android.support.design.widget.AppBarLayout.LayoutParams toolBarPrivacy = new android.support.design.widget.AppBarLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        toolBarPrivacy.width = (int) width;
        toolBarPrivacy.height = (int) height * 8 / 100;
      //  toolBarPrivacy.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        toolbarAppInfo.setLayoutParams(toolBarPrivacy);

        FrameLayout.LayoutParams buttonPrivacyBack = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonPrivacyBack.width = width * 6 / 100;
        buttonPrivacyBack.height = width * 6 / 100;
        buttonPrivacyBack.leftMargin = width * 4 / 100;
        buttonPrivacyBack.gravity = Gravity.START | Gravity.CENTER;
        imgBackAppInfo.setLayoutParams(buttonPrivacyBack);

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

        LinearLayout.LayoutParams linearConversation = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearConversation.width = LinearLayout.LayoutParams.MATCH_PARENT;
        linearConversation.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearConversation.setMargins(0, margin, 0, margin);
        linearAppInfoContent.setLayoutParams(linearConversation);

        LinearLayout.LayoutParams linearHelpInfo = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearHelpInfo.width = LinearLayout.LayoutParams.MATCH_PARENT;
        linearHelpInfo.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearHelpInfo.topMargin = width * 3 / 100;
        linearHelpAppInfo.setLayoutParams(linearHelpInfo);

        LinearLayout.LayoutParams linearContactUs = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearContactUs.width = LinearLayout.LayoutParams.MATCH_PARENT;
        linearContactUs.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearContactUs.bottomMargin = width * 3 / 100;
        linearContactUsAppInfo.setLayoutParams(linearContactUs);

        LinearLayout.LayoutParams linearAppName = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearAppName.width = (int) width * 60 / 100;
        linearAppName.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearAppName.gravity = Gravity.CENTER;
        linearAppName.topMargin = width * 2 / 100;
        linearAppName.bottomMargin = width * 6 / 100;
        linearAppNameVer.setLayoutParams(linearAppName);

        LinearLayout.LayoutParams linearAppNameIcon = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearAppNameIcon.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearAppNameIcon.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearAppNameIcon.gravity = Gravity.CENTER;
        imgAppNameInfo.setLayoutParams(linearAppNameIcon);

        LinearLayout.LayoutParams linearAppNameText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearAppNameText.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearAppNameText.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearAppNameText.gravity = Gravity.RIGHT;
        tvVersionAppInfo.setLayoutParams(linearAppNameText);

        LinearLayout.LayoutParams linearAppIcon = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearAppIcon.width = (int) width * 40 / 100;
        linearAppIcon.height = (int) width * 40 / 100;
        linearAppIcon.gravity = Gravity.CENTER;
        linearAppIcon.topMargin = width * 6 / 100;
        linearAppIconInfo.setLayoutParams(linearAppIcon);

        LinearLayout.LayoutParams linearAppInfoIcon = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearAppInfoIcon.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearAppInfoIcon.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        linearAppInfoIcon.gravity = Gravity.CENTER;
        imgAppIcon.setLayoutParams(linearAppInfoIcon);

        LinearLayout.LayoutParams notifyConverText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        notifyConverText.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        notifyConverText.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        notifyConverText.gravity = Gravity.CENTER;
        notifyConverText.setMargins(margin, margin, margin, margin);
        tvAppCopyRight.setLayoutParams(notifyConverText);

        LinearLayout.LayoutParams contactUsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        contactUsText.width = LinearLayout.LayoutParams.MATCH_PARENT;
        contactUsText.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        contactUsText.setMargins(width * 3 / 100, margin, margin, margin);
        contactUsText.gravity = Gravity.CENTER_VERTICAL;
        tvContactUsInfo.setLayoutParams(contactUsText);
        tvHelpInfo.setLayoutParams(contactUsText);

        if (width >= 600)
        {
            tvTitleAppInfo.setTextSize(19);
            tvAppCopyRight.setTextSize(18);
            tvContactUsInfo.setTextSize(18);
            tvHelpInfo.setTextSize(18);
        }
        else if (width > 501 && width < 600)
        {
            tvTitleAppInfo.setTextSize(18);
            tvAppCopyRight.setTextSize(17);
            tvContactUsInfo.setTextSize(17);
            tvHelpInfo.setTextSize(17);
        }
        else if (width > 260 && width < 500)
        {
            tvTitleAppInfo.setTextSize(17);
            tvAppCopyRight.setTextSize(16);
            tvContactUsInfo.setTextSize(16);
            tvHelpInfo.setTextSize(16);
        }
        else if (width <= 260)
        {
            tvTitleAppInfo.setTextSize(16);
            tvAppCopyRight.setTextSize(15);
            tvContactUsInfo.setTextSize(15);
            tvHelpInfo.setTextSize(15);
        }
    }

    public void onBackPressed(){
        startActivity(new Intent(AppInfo.this, SettingsActivity.class));
        finish();
        super.onBackPressed();
    }
}
