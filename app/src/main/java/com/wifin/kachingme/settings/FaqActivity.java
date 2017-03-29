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
import com.wifin.kachingme.adaptors.FaqAdapter;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class FaqActivity extends Activity
{
    List<String> listFAQContent;
    String[] listFAQValue;
    String TAG = FaqActivity.class.getSimpleName();
    Context contextFAQ;
    AppBarLayout appbarFAQ;
    Toolbar toolbarFAQ;
    FrameLayout frameFAQ;
    ImageView imgBackFAQ;
    TextView tvTitleFAQ;
    FaqAdapter faqAdapter = new FaqAdapter();
    RecyclerView recyclerFAQ;
    int height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_faq);

        contextFAQ = this;

        initFAQ();
        screenFAQ();

        recyclerFAQ = (RecyclerView) findViewById(R.id.recyclerFAQ);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerFAQ.setLayoutManager(linearLayoutManager);
        recyclerFAQ.setHasFixedSize(true);

        listFAQContent = new ArrayList<>();

        listFAQValue = contextFAQ.getResources().getStringArray(R.array.faq_content);

        for (int faq = 0; faq < listFAQValue.length; faq++)
        {
            listFAQContent.add(listFAQValue[faq]);
        }

        faqAdapter = new FaqAdapter(contextFAQ, listFAQContent);
        recyclerFAQ.setAdapter(faqAdapter);

        imgBackFAQ.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }

    public void initFAQ()
    {
        recyclerFAQ = (RecyclerView) findViewById(R.id.recyclerFAQ);
        appbarFAQ = (AppBarLayout) findViewById(R.id.appbarFAQ);
        toolbarFAQ = (Toolbar) findViewById(R.id.toolbarFAQ);
        frameFAQ = (FrameLayout) findViewById(R.id.frameFAQ);
        imgBackFAQ = (ImageView) findViewById(R.id.imgBackFAQ);
        tvTitleFAQ = (TextView) findViewById(R.id.tvTitleFAQ);
        Constant.typeFace(this, tvTitleFAQ);

    }

    public void screenFAQ()
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
        toolbarFAQ.setLayoutParams(toolBarPrivacy);

        FrameLayout.LayoutParams buttonPrivacyBack = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonPrivacyBack.width = (int) width * 6 / 100;
        buttonPrivacyBack.height = (int) width * 6 / 100;
        buttonPrivacyBack.leftMargin = (int) width * 4 / 100;
        buttonPrivacyBack.gravity = Gravity.START | Gravity.CENTER;
        imgBackFAQ.setLayoutParams(buttonPrivacyBack);

        if (width >= 600)
        {
            tvTitleFAQ.setTextSize(19);
        }
        else if (width > 501 && width < 600)
        {
            tvTitleFAQ.setTextSize(18);
        }
        else if (width > 260 && width < 500)
        {
            tvTitleFAQ.setTextSize(17);
        }
        else if (width <= 260)
        {
            tvTitleFAQ.setTextSize(16);
        }
    }

    public void onBackPressed()
    {
        super.onBackPressed();

        startActivity(new Intent(FaqActivity.this, AppInfo.class));
    }
}
