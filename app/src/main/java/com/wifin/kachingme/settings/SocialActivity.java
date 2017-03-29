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

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.SocialMediaAdapter;
import com.wifin.kachingme.chat_home.HeaderActivity;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class SocialActivity extends HeaderActivity
{
    List<String> socialItem;
    String[] socialItems;
    int[] socialImages = {R.drawable.mail_bubble,
            R.drawable.msg_bubble,
            R.drawable.twitter_bubble,
            R.drawable.facebook_bubble};
    int height, width;
    String TAG = SocialActivity.class.getSimpleName();
    SocialMediaAdapter socialMediaAdapter = new SocialMediaAdapter();
    Context contextSocial;
    RecyclerView recyclerSocial;
    AppBarLayout socialAppBar;
    Toolbar socialToolBar;
    FrameLayout socialFrameLayout;
    ImageView imgSocialBack;
    LinearLayout linearSocial, linearCancelButton;
    Button btSocialCancel;
    TextView tvTitleAppSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_layout);
        View.inflate(this, R.layout.activity_social, vg);
        mHeaderLayout.setVisibility(View.GONE);

        contextSocial = this;

        initSocial();
        screenSocial();

        recyclerSocial = (RecyclerView) findViewById(R.id.recyclerSocial);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerSocial.setLayoutManager(linearLayoutManager);
        recyclerSocial.setHasFixedSize(true);

        socialItem = new ArrayList<>();
        socialItems = contextSocial.getResources().getStringArray(R.array.social_items);

        for (int social = 0; social < socialImages.length; social++)
        {
            socialItem.add(socialItems[social]);
        }

        socialMediaAdapter = new SocialMediaAdapter(contextSocial, socialItem, socialImages);
        recyclerSocial.setAdapter(socialMediaAdapter);

        imgSocialBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        btSocialCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }

    public void initSocial()
    {
        socialAppBar = (AppBarLayout) findViewById(R.id.socialAppBar);
        socialToolBar = (Toolbar) findViewById(R.id.socialToolBar);
        socialFrameLayout = (FrameLayout) findViewById(R.id.socialFrameLayout);
        imgSocialBack = (ImageView) findViewById(R.id.imgSocialBack);
        linearSocial = (LinearLayout) findViewById(R.id.linearSocial);
        recyclerSocial = (RecyclerView) findViewById(R.id.recyclerSocial);
        linearCancelButton = (LinearLayout) findViewById(R.id.linearCancelButton);
        btSocialCancel = (Button) findViewById(R.id.btSocialCancel);
        tvTitleAppSocial = (TextView) findViewById(R.id.tvTitleAppSocial);

        Constant.typeFace(this, btSocialCancel);
        Constant.typeFace(this, tvTitleAppSocial);

    }

    public void screenSocial()
    {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Log.e(TAG, "Height : " + height + "\n" + "Width : " + width);

        Constant.height = height;
        Constant.width = width;

        AppBarLayout.LayoutParams toolBarSocial = new AppBarLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT);
        toolBarSocial.width = (int) width;
        toolBarSocial.height = (int) height * 10 / 100;
        toolBarSocial.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        socialToolBar.setLayoutParams(toolBarSocial);

        FrameLayout.LayoutParams buttonSocialBack = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonSocialBack.width = (int) width * 4 / 100;
        buttonSocialBack.height = (int) width * 6 / 100;
        buttonSocialBack.leftMargin = (int) width * 3 / 100;
        buttonSocialBack.gravity = Gravity.START | Gravity.CENTER;
        imgSocialBack.setLayoutParams(buttonSocialBack);

        LinearLayout.LayoutParams linearContentLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearContentLayout.width = width;
        linearContentLayout.height = height;
        linearContentLayout.weight = 1;
//        mContentLayout.setLayoutParams(linearContentLayout);

        LinearLayout.LayoutParams linearFooterLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearFooterLayout.width = LinearLayout.LayoutParams.MATCH_PARENT;
        linearFooterLayout.height = (int) height * 10 / 100;
        linearFooterLayout.gravity = Gravity.BOTTOM;
        mFooterLayout.setLayoutParams(linearFooterLayout);

        LinearLayout.LayoutParams deleteButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        deleteButton.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        deleteButton.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        deleteButton.setMargins(width * 2 / 100, width * 4 / 100, width * 2 / 100, 0);
        deleteButton.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER;
        linearCancelButton.setLayoutParams(deleteButton);

        LinearLayout.LayoutParams deleteButtonContent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        deleteButtonContent.width = 25 * width / 100;
        deleteButtonContent.height = 10 * width / 100;
        deleteButtonContent.setMargins(width * 2 / 100, width * 2 / 100, width * 2 / 100, width * 2 / 100);
        deleteButtonContent.gravity = Gravity.CENTER;
        btSocialCancel.setLayoutParams(deleteButtonContent);

        if (width >= 600)
        {
            tvTitleAppSocial.setTextSize(18);
            btSocialCancel.setTextSize(17);
        }
        else if (width > 501 && width < 600)
        {
            tvTitleAppSocial.setTextSize(17);
            btSocialCancel.setTextSize(16);
        }
        else if (width > 260 && width < 500)
        {
            tvTitleAppSocial.setTextSize(16);
            btSocialCancel.setTextSize(15);
        }
        else if (width <= 260)
        {
            tvTitleAppSocial.setTextSize(15);
            btSocialCancel.setTextSize(14);
        }
    }

    public void onBackPressed()
    {
        super.onBackPressed();

        startActivity(new Intent(SocialActivity.this, SettingsActivity.class));

        finish();
    }
}
