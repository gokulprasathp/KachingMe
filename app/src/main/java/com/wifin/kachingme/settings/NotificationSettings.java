package com.wifin.kachingme.settings;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.util.Constant;

public class NotificationSettings extends Activity {
    TextView tvNotifyTitleText;
    FrameLayout frameNotifyMenu;
    ImageView imgNotifyBack;
    AppBarLayout notifyAppBarLayout;
    Toolbar notifyToolbar;
    int height, width;
    Context contextNotify;
    String TAG = NotificationSettings.class.getSimpleName(), valueIntent;
    Bundle extraValue;

    public NotificationSettings() {
        Constant.printMsg(TAG + " Started ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notification_settings);
        contextNotify = this;
        initNotification();
        screenNotification();
        extraValue = getIntent().getExtras();
        valueIntent = extraValue.getString("TAG").toString();
        Constant.printMsg(valueIntent);

        if (valueIntent.equalsIgnoreCase("Notify")) {
            tvNotifyTitleText.setText("Notification Settings");
            getFragmentManager().beginTransaction().replace(R.id.contentPreference, new SettingsNotifications()).commit();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (valueIntent.equalsIgnoreCase("Chat")) {
            tvNotifyTitleText.setText("Chat Settings");
            getFragmentManager().beginTransaction().replace(R.id.contentPreference, new Chat_Settings()).commit();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (valueIntent.equalsIgnoreCase("Media")) {
            tvNotifyTitleText.setText("Media Auto Download");
            getFragmentManager().beginTransaction().replace(R.id.contentPreference, new MediaAutoDownload()).commit();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (valueIntent.equalsIgnoreCase("Share")) {
            tvNotifyTitleText.setText("Contacts");
            getFragmentManager().beginTransaction().replace(R.id.contentPreference, new ContactsSetting()).commit();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        imgNotifyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valueIntent.equalsIgnoreCase("Share")) {
                    Constant.printMsg("Share");
                    startActivity(new Intent(NotificationSettings.this, SliderTesting.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else if (valueIntent.equalsIgnoreCase("Media")) {
                    Constant.printMsg("Media");
                    startActivity(new Intent(NotificationSettings.this, NotificationSettings.class).putExtra("TAG", "Chat")
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else {
                    Constant.printMsg("Image Back Pressed");
                    startActivity(new Intent(NotificationSettings.this, SettingsActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        });
    }

    public void initNotification() {
        tvNotifyTitleText = (TextView) findViewById(R.id.tvNotifyTitleText);
        frameNotifyMenu = (FrameLayout) findViewById(R.id.frameNotifyMenu);
        imgNotifyBack = (ImageView) findViewById(R.id.imgNotifyBack);
        notifyAppBarLayout = (AppBarLayout) findViewById(R.id.notifyAppBarLayout);
        notifyToolbar = (Toolbar) findViewById(R.id.notifyToolbar);

        try {
            Constant.typeFace(this, tvNotifyTitleText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void screenNotification() {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Log.e(TAG, "Height : " + height + "\n" + "Width : " + width);

        Constant.height = height;
        Constant.width = width;

        /* App Bar Layout Param for Toolbar */

        AppBarLayout.LayoutParams toolBarUsage = new AppBarLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        toolBarUsage.width = (int) width;
        toolBarUsage.height = (int) height * 10 / 100;
        toolBarUsage.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        notifyToolbar.setLayoutParams(toolBarUsage);

        Log.e(TAG, " " + toolBarUsage.width + " " + toolBarUsage.height);

        /* Back Button Param for Image View */

        FrameLayout.LayoutParams buttonUsageBack = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonUsageBack.width = width * 6 / 100;
        buttonUsageBack.height = width * 6 / 100;
        buttonUsageBack.leftMargin = (int) width * 4 / 100;
        buttonUsageBack.gravity = Gravity.START | Gravity.CENTER;
        imgNotifyBack.setLayoutParams(buttonUsageBack);

        if (width >= 600) {
            tvNotifyTitleText.setTextSize(19);
        } else if (width > 501 && width < 600) {
            tvNotifyTitleText.setTextSize(18);
        } else if (width > 260 && width < 500) {
            tvNotifyTitleText.setTextSize(17);
        } else if (width <= 260) {
            tvNotifyTitleText.setTextSize(16);
        }
    }

    public void onBackPressed() {
        if (valueIntent.equalsIgnoreCase("Share")) {
            Constant.printMsg("Share");
            startActivity(new Intent(NotificationSettings.this, SliderTesting.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        } else if (valueIntent.equalsIgnoreCase("Media")) {
            Constant.printMsg("Media");
            startActivity(new Intent(NotificationSettings.this, NotificationSettings.class).putExtra("TAG", "Chat")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        } else {
            Constant.printMsg("Image Back Pressed");
            startActivity(new Intent(NotificationSettings.this, SettingsActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
        super.onBackPressed();
    }
}
