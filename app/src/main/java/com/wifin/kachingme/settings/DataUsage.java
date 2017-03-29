package com.wifin.kachingme.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.DataUsageAdapter;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class DataUsage extends Activity
{
    List<String> listUsageTitle, listUsageSubs, listUsageBytes;
    String[] listTitleValue, listSubsValue, listBytesValue;
    String TAG = DataUsage.class.getSimpleName(), lastResetPref;
    Context contextUsage;
    DataUsageAdapter dataUsageAdapter = new DataUsageAdapter();
    public static RecyclerView recyclerDataUsage;
    AppBarLayout dataUsageAppBar;
    Toolbar dataUsageToolBar;
    FrameLayout dataUsageFrame;
    LinearLayout linearResetButton, linearResetText;
    public static TextView tvDataLastReset;
    Button btResetUsage;
    ImageView dataUsageImgBack;
    int height, width;
    TextView tvTitleNetworkUsage;
    SharedPreferences sharePrefReset;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_data_usage);

        contextUsage = this;

        initDataUsage();
        screenDataUsage();

        sharePrefReset = PreferenceManager.getDefaultSharedPreferences(contextUsage);

        recyclerDataUsage = (RecyclerView) findViewById(R.id.recyclerDataUsage);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerDataUsage.setLayoutManager(linearLayoutManager);
        recyclerDataUsage.setHasFixedSize(true);

        listUsageTitle = new ArrayList();
        listUsageSubs = new ArrayList();
        listUsageBytes = new ArrayList();

        listTitleValue = contextUsage.getResources().getStringArray(R.array.data_usage_title);
        listSubsValue = contextUsage.getResources().getStringArray(R.array.data_usage_subs);
        listBytesValue = contextUsage.getResources().getStringArray(R.array.data_usage_value);

        for (int usage = 0; usage < listTitleValue.length; usage++)
        {
            listUsageTitle.add(listTitleValue[usage]);
            listUsageSubs.add(listSubsValue[usage]);
            listUsageBytes.add(listBytesValue[usage]);
        }

        dataUsageAdapter = new DataUsageAdapter(contextUsage, listUsageTitle, listUsageSubs, listUsageBytes);
        recyclerDataUsage.setAdapter(dataUsageAdapter);

        lastResetPref = sharePrefReset.getString("last_reset", "");

        if (!lastResetPref.equalsIgnoreCase(""))
        {
            tvDataLastReset.setText("Last Reset " + "\n" + lastResetPref);
        }
        else
        {
            tvDataLastReset.setText("Last Reset " + "\n" + "Never");
        }

        dataUsageImgBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        btResetUsage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dataUsageAdapter.showConfirmationDialog();
            }
        });
    }

    public void initDataUsage()
    {
        recyclerDataUsage = (RecyclerView) findViewById(R.id.recyclerDataUsage);
        dataUsageAppBar = (AppBarLayout) findViewById(R.id.dataUsageAppBar);
        dataUsageToolBar = (Toolbar) findViewById(R.id.dataUsageToolBar);
        dataUsageFrame = (FrameLayout) findViewById(R.id.dataUsageFrame);
        linearResetButton = (LinearLayout) findViewById(R.id.linearResetButton);
        linearResetText = (LinearLayout) findViewById(R.id.linearResetText);
        tvDataLastReset = (TextView) findViewById(R.id.tvDataLastReset);
        btResetUsage = (Button) findViewById(R.id.btResetUsage);
        dataUsageImgBack = (ImageView) findViewById(R.id.dataUsageImgBack);
        tvTitleNetworkUsage = (TextView) findViewById(R.id.tvTitleNetworkUsage);

        Constant.typeFace(this, tvDataLastReset);
        Constant.typeFace(this, tvTitleNetworkUsage);
        Constant.typeFace(this,btResetUsage);
    }

    public void screenDataUsage()
    {
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
        dataUsageToolBar.setLayoutParams(toolBarUsage);

        FrameLayout.LayoutParams buttonUsageBack = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonUsageBack.width = width * 6 / 100;
        buttonUsageBack.height = width * 6 / 100;
        buttonUsageBack.leftMargin = width * 4 / 100;
        buttonUsageBack.gravity = Gravity.START | Gravity.CENTER;
        dataUsageImgBack.setLayoutParams(buttonUsageBack);

        LinearLayout.LayoutParams resetText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        resetText.width = LinearLayout.LayoutParams.MATCH_PARENT;
        resetText.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        resetText.setMargins(width * 2 / 100, width * 2 / 100, width * 2 / 100, width * 2 / 100);
        resetText.gravity = Gravity.CENTER_VERTICAL;
        linearResetText.setLayoutParams(resetText);

        LinearLayout.LayoutParams resetButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        resetButton.width = LinearLayout.LayoutParams.MATCH_PARENT;
        resetButton.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        resetButton.setMargins(width * 2 / 100, width * 4 / 100, width * 2 / 100, 0);
        resetButton.gravity = Gravity.CENTER_VERTICAL;
        linearResetButton.setLayoutParams(resetButton);

        LinearLayout.LayoutParams lastResetText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lastResetText.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        lastResetText.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lastResetText.setMargins(width * 2 / 100, width * 3 / 100, width * 1 / 100, width * 3 / 100);
        lastResetText.gravity = Gravity.CENTER_VERTICAL;
        tvDataLastReset.setLayoutParams(lastResetText);

        LinearLayout.LayoutParams buttonReset = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonReset.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        buttonReset.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        buttonReset.setMargins(width * 2 / 100, width * 2 / 100, width * 2 / 100, width * 2 / 100);
        buttonReset.gravity = Gravity.CENTER;
        btResetUsage.setLayoutParams(buttonReset);

        if (width >= 600)
        {
            tvDataLastReset.setTextSize(18);
            btResetUsage.setTextSize(18);
            tvTitleNetworkUsage.setTextSize(18);
        }
        else if (width > 501 && width < 600)
        {
            tvDataLastReset.setTextSize(17);
            btResetUsage.setTextSize(17);
            tvTitleNetworkUsage.setTextSize(17);
        }
        else if (width > 260 && width < 500)
        {
            tvDataLastReset.setTextSize(16);
            btResetUsage.setTextSize(16);
            tvTitleNetworkUsage.setTextSize(16);
        }
        else if (width <= 260)
        {
            tvDataLastReset.setTextSize(15);
            btResetUsage.setTextSize(15);
            tvTitleNetworkUsage.setTextSize(15);
        }
    }

    public void onBackPressed()
    {
        super.onBackPressed();

        startActivity(new Intent(DataUsage.this, UsageActivity.class));

        finish();
    }
}
