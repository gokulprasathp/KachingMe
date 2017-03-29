package com.wifin.kachingme.settings;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
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
import com.wifin.kachingme.adaptors.ChatSettingAdapter;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.util.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class ChatSettings extends Activity
{
    String TAG = ChatSettings.class.getSimpleName();
    Context contextChatSettings;
    int height, width;
    AppBarLayout appbarChatSettings;
    Toolbar toolbarChatSettings;
    FrameLayout frameChatSettings;
    ImageView imgBackChatSet;
    TextView tvTitleChatSet;
    RecyclerView recyclerChatSettings;
    ChatSettingAdapter chatSettingAdapter = new ChatSettingAdapter();
    List<String> listChatTitle, listChatSubs;
    String[] listTitleValue, listSubsValue;

    public ChatSettings()
    {
        Log.e(TAG, "Chat Settings Activity Started");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat_settings);

        contextChatSettings = this;

        initChatSettings();
        screenChatSettings();

        recyclerChatSettings = (RecyclerView) findViewById(R.id.recyclerChatSettings);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerChatSettings.setLayoutManager(linearLayoutManager);
        recyclerChatSettings.setHasFixedSize(true);

        listChatTitle = new ArrayList();
        listChatSubs = new ArrayList();

        listTitleValue = contextChatSettings.getResources().getStringArray(R.array.chat_settings_title);
        listSubsValue = contextChatSettings.getResources().getStringArray(R.array.chat_settings_subs);

        for (int chat = 0; chat < listTitleValue.length; chat++)
        {
            listChatTitle.add(listTitleValue[chat]);
            listChatSubs.add(listSubsValue[chat]);
        }

        chatSettingAdapter = new ChatSettingAdapter(contextChatSettings, listChatTitle, listChatSubs);
        recyclerChatSettings.setAdapter(chatSettingAdapter);

        imgBackChatSet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }

    public void initChatSettings()
    {
        appbarChatSettings = (AppBarLayout) findViewById(R.id.appbarChatSettings);
        toolbarChatSettings = (Toolbar) findViewById(R.id.toolbarChatSettings);
        frameChatSettings = (FrameLayout) findViewById(R.id.frameChatSettings);
        imgBackChatSet = (ImageView) findViewById(R.id.imgBackChatSet);
        tvTitleChatSet = (TextView) findViewById(R.id.tvTitleChatSet);
        recyclerChatSettings = (RecyclerView) findViewById(R.id.recyclerChatSettings);

        Constant.typeFace(this,tvTitleChatSet);

    }

    public void screenChatSettings()
    {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Log.e(TAG, "Height : " + height + "\n" + "Width : " + width);

        Constant.height = height;
        Constant.width = width;

        AppBarLayout.LayoutParams toolbarChat = new AppBarLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        toolbarChat.width = (int) width;
        toolbarChat.height = (int) height * 10 / 100;
        toolbarChat.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        toolbarChatSettings.setLayoutParams(toolbarChat);

        FrameLayout.LayoutParams imgBackChat = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        imgBackChat.width = (int) width * 4 / 100;
        imgBackChat.height = (int) width * 6 / 100;
        imgBackChat.leftMargin = (int) width * 3 / 100;
        imgBackChat.gravity = Gravity.START | Gravity.CENTER;
        imgBackChatSet.setLayoutParams(imgBackChat);

        if (width >= 600)
        {
            tvTitleChatSet.setTextSize(19);
        }
        else if (width > 501 && width < 600)
        {
            tvTitleChatSet.setTextSize(18);
        }
        else if (width > 260 && width < 500)
        {
            tvTitleChatSet.setTextSize(17);
        }
        else if (width <= 260)
        {
            tvTitleChatSet.setTextSize(16);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 0)
        {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String filePath = cursor.getString(columnIndex);

            File f = new File(Constant.local_profile_picture_dir + new File(filePath).getName());
            FileChannel inChannel = null, outChannel = null;

            try
            {
                inChannel = new FileInputStream(filePath).getChannel();
                outChannel = new FileOutputStream(f.getAbsolutePath()).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);

                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();

                KachingMeApplication.getsharedpreferences_Editor().putString("wallpaper", Constant.local_profile_picture_dir + new File(filePath).getName()).commit();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void onBackPressed()
    {
        super.onBackPressed();

        startActivity(new Intent(ChatSettings.this, SettingsActivity.class));

        finish();
    }
}
