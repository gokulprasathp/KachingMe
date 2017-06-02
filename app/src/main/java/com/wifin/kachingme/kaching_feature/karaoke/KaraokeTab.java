package com.wifin.kachingme.kaching_feature.karaoke;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.adaptors.SongListAdapter;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class KaraokeTab extends MainActivity implements OnClickListener {

    public static int height, width;
    public static KaraokeTab mActivity;
    TextView $Tab1, $Tab2, $Tab3;
    LinearLayout $TopLayout;
    ListView $SongList;
    List<String> tab1Song = new ArrayList<String>();
    List<String> tab2Song = new ArrayList<String>();
    List<String> tab3Song = new ArrayList<String>();
    boolean tab1 = false, tab2 = false, tab3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mActivity = this;
        ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
        View.inflate(this, R.layout.kaching_test, vg);
        logo.setVisibility(ImageView.GONE);
        back.setVisibility(ImageView.VISIBLE);
        cart.setVisibility(ImageView.INVISIBLE);
        sideMenufoot.setVisibility(LinearLayout.GONE);
        head.setTextColor(Color.parseColor("#FFFFFF"));
        Ka_newlogo.setVisibility(ImageView.GONE);
        back.setBackgroundResource(R.drawable.arrow);
        headlay.setBackgroundColor(Color.parseColor("#FE0000"));
        head.setVisibility(View.VISIBLE);
        head.setText("Karaoke");
        initializatio();
        screeenArrange();
        tab1 = true;
        // $Tab1.setBackgroundResource(R.color.kaching_selected_color);
        $Tab1.setBackgroundResource(R.drawable.whitecolr);
        $Tab1.setTextColor(Color.parseColor("#ffffff"));
        // $Tab2.setBackgroundResource(R.color.kaching_color);

        $Tab2.setBackgroundResource(R.drawable.selectorforclick);
        $Tab2.setTextColor(Color.parseColor("#ffffff"));

        $Tab3.setBackgroundResource(R.drawable.selectorforclick);
        $Tab3.setTextColor(Color.parseColor("#ffffff"));

        $SongList.setAdapter(new SongListAdapter(KaraokeTab.this, tab1Song));

        tab1Song.add("Moongil Thottam");
        tab1Song.add("Idhu Varai");
        tab1Song.add("Amma Endrazhaikaadha");
        tab1Song.add("Kannazhaga");
        tab1Song.add("Osaka Osaka");

        tab2Song.add("Smack that");
        tab2Song.add("Lonely");
        tab2Song.add("Show Me The Meaning");

        tab3Song.add("Jiya re");
        tab3Song.add("tujh mein rab dikhta");
        tab3Song.add("HAULE HAULE HO JAYEGA");

        $Tab1.setBackgroundResource(R.drawable.whitecolr);
        $Tab1.setTextColor(Color.parseColor("#ffffff"));
        $Tab2.setBackgroundResource(R.drawable.selectorforclick);
        $Tab2.setTextColor(Color.parseColor("#ffffff"));
        $Tab3.setBackgroundResource(R.drawable.selectorforclick);
        $Tab3.setTextColor(Color.parseColor("#ffffff"));
        $SongList
                .setAdapter(new SongListAdapter(KaraokeTab.this, tab1Song));
        tab1 = true;
        tab2 = false;
        tab3 = false;

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (Constant.mKroKFromSlider == true) {
                    Constant.mKroKFromSlider = false;
                    Intent i = new Intent(KaraokeTab.this, SliderTesting.class);
                    startActivity(i);
                    finish();

                } else if (Constant.mKrokFromGroup == true) {
                    Constant.mKrokFromGroup = false;
                    Constant.isMUC_Paused = true;
                    Intent i = new Intent(KaraokeTab.this, MUCTest.class);
                    startActivity(i);
                    finish();

                } else {
                    Intent i = new Intent(KaraokeTab.this, ChatTest.class);
                    startActivity(i);
                    finish();

                }

            }
        });

        $SongList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (tab1 == true) {
                    if (tab1Song.get(position).toString()
                            .equalsIgnoreCase("Moongil Thottam")) {
                        Constant.mSong = "Moongil Thottam";
                        Constant.url = "IBYqEi7kzrc";
                        Intent i = new Intent(KaraokeTab.this,
                                KaraokeYoutube.class);
                        startActivity(i);
                        finish();

                    } else if (tab1Song.get(position).toString()
                            .equalsIgnoreCase("Idhu Varai")) {

                        Constant.mSong = "Idhu Varai";
                        Constant.url = "cS6BghmRzi0";
                        Intent i = new Intent(KaraokeTab.this,
                                KaraokeYoutube.class);
                        startActivity(i);
                        finish();

                    } else if (tab1Song.get(position).toString()
                            .equalsIgnoreCase("Amma Endrazhaikaadha")) {

                        Constant.url = "Vc3Jih4vGnc";
                        Constant.mSong = "Amma Endrazhaikaadha";
                        Intent i = new Intent(KaraokeTab.this,
                                KaraokeYoutube.class);
                        startActivity(i);
                        finish();

                    } else if (tab1Song.get(position).toString()
                            .equalsIgnoreCase("Kannazhaga")) {

                        Constant.url = "PRVEU0fGJfI";
                        Constant.mSong = "Kannazhaga";
                        Intent i = new Intent(KaraokeTab.this,
                                KaraokeYoutube.class);
                        startActivity(i);
                        finish();
                    } else if (tab1Song.get(position).toString()
                            .equalsIgnoreCase("Osaka Osaka")) {

                        Constant.url = "YKp00pvX3rg&list=PLgk9fgwjV1z-Cmst7crDGPefzHgS_29CW";
                        Constant.mSong = "Osaka Osaka";
                        Intent i = new Intent(KaraokeTab.this,
                                KaraokeYoutube.class);
                        startActivity(i);
                        finish();
                    }
                } else if (tab2 == true) {
                    Constant.printMsg("yesghfs::::::>>>>>>>>"
                            + tab2Song.get(position).toString().trim());
                    if (tab2Song.get(position).toString()

                            .equalsIgnoreCase("Smack that")) {
                        Constant.mSong = "Smack that";
                        Constant.url = "svGc6zlI3eQ";
                        Intent i = new Intent(KaraokeTab.this,
                                KaraokeYoutube.class);
                        startActivity(i);
                        finish();
                    } else if (tab2Song.get(position).toString()

                            .equalsIgnoreCase("Lonely")) {
                        Constant.mSong = "Lonely";
                        Constant.url = "UvcG3dMNeNE";
                        Intent i = new Intent(KaraokeTab.this,
                                KaraokeYoutube.class);
                        startActivity(i);
                        finish();
                    } else if (tab2Song.get(position).toString()

                            .equalsIgnoreCase("Show Me The Meaning")) {
                        Constant.mSong = "Show Me The Meaning";
                        Constant.url = "8qUr6bmQwG4";
                        Intent i = new Intent(KaraokeTab.this,
                                KaraokeYoutube.class);
                        startActivity(i);
                        finish();
                    }

                } else if (tab3 == true) {
                    Constant.printMsg("yesghfs::::::>>>>>>>>"
                            + tab3Song.get(position).toString().trim());
                    if (tab3Song.get(position).toString()

                            .equalsIgnoreCase("Jiya re")) {
                        Constant.mSong = "Jiya re";

                        Constant.url = "mDqEW7MfYb4";
                        Intent i = new Intent(KaraokeTab.this,
                                KaraokeYoutube.class);
                        startActivity(i);
                        finish();
                    } else if (tab3Song.get(position).toString()

                            .equalsIgnoreCase("tujh mein rab dikhta")) {
                        Constant.mSong = "tujh mein rab dikhta";

                        Constant.url = "0wE21a_udO4";
                        Intent i = new Intent(KaraokeTab.this,
                                KaraokeYoutube.class);
                        startActivity(i);
                        finish();
                    } else if (tab3Song.get(position).toString()

                            .equalsIgnoreCase("HAULE HAULE HO JAYEGA")) {
                        Constant.mSong = "HAULE HAULE HO JAYEGA";

                        Constant.url = "4is68dyAuq0";
                        Intent i = new Intent(KaraokeTab.this,
                                KaraokeYoutube.class);
                        startActivity(i);
                        finish();
                    }

                }

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (Constant.mKroKFromSlider == true) {
            Constant.mKroKFromSlider = false;
            Intent i = new Intent(KaraokeTab.this, SliderTesting.class);
            startActivity(i);
            finish();

        } else if (Constant.mKrokFromGroup == true) {
            Constant.mKrokFromGroup = false;

            Constant.isMUC_Paused = true;

            Intent i = new Intent(KaraokeTab.this, MUCTest.class);
            startActivity(i);
            finish();

        } else {
            Intent i = new Intent(KaraokeTab.this, ChatTest.class);
            startActivity(i);
            finish();

        }

    }

    private void initializatio() {
        // TODO Auto-generated method stub
        $TopLayout = (LinearLayout) findViewById(R.id.kaching_topLayout);
        $Tab1 = (TextView) findViewById(R.id.kaching_tab1);
        $Tab2 = (TextView) findViewById(R.id.kaching_tab2);
        $Tab3 = (TextView) findViewById(R.id.kaching_tab3);
        $SongList = (ListView) findViewById(R.id.kaching_listView);

        Constant.typeFace(this, $Tab1);
        Constant.typeFace(this, $Tab2);
        Constant.typeFace(this, $Tab3);

        $Tab1.setOnClickListener(this);
        $Tab2.setOnClickListener(this);
        $Tab3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.kaching_tab1:
                $Tab1.setBackgroundResource(R.drawable.whitecolr);
                $Tab1.setTextColor(Color.parseColor("#ffffff"));
                $Tab2.setBackgroundResource(R.drawable.selectorforclick);
                $Tab2.setTextColor(Color.parseColor("#ffffff"));
                $Tab3.setBackgroundResource(R.drawable.selectorforclick);
                $Tab3.setTextColor(Color.parseColor("#ffffff"));
                $SongList
                        .setAdapter(new SongListAdapter(KaraokeTab.this, tab1Song));
                tab1 = true;
                tab2 = false;
                tab3 = false;
                break;
            case R.id.kaching_tab2:
                $Tab1.setBackgroundResource(R.drawable.selectorforclick);
                $Tab1.setTextColor(Color.parseColor("#ffffff"));
                $Tab2.setBackgroundResource(R.drawable.whitecolr);
                $Tab2.setTextColor(Color.parseColor("#ffffff"));
                $Tab3.setBackgroundResource(R.drawable.selectorforclick);
                $Tab3.setTextColor(Color.parseColor("#ffffff"));
                $SongList
                        .setAdapter(new SongListAdapter(KaraokeTab.this, tab2Song));

                tab1 = false;
                tab2 = true;
                tab3 = false;
                break;
            case R.id.kaching_tab3:
                $Tab1.setBackgroundResource(R.drawable.selectorforclick);
                $Tab1.setTextColor(Color.parseColor("#ffffff"));
                $Tab2.setBackgroundResource(R.drawable.selectorforclick);
                $Tab2.setTextColor(Color.parseColor("#ffffff"));
                $Tab3.setBackgroundResource(R.drawable.whitecolr);
                $Tab3.setTextColor(Color.parseColor("#ffffff"));
                $SongList
                        .setAdapter(new SongListAdapter(KaraokeTab.this, tab3Song));
                tab1 = false;
                tab2 = false;
                tab3 = true;
                break;
        }
    }

    private void screeenArrange() {
        // TODO Auto-generated method stub
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;
        LinearLayout.LayoutParams abbtx1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        abbtx1.width = width * 60 / 100;
        abbtx1.gravity = Gravity.CENTER;
        abbtx1.leftMargin = width * 5 / 100;
        head.setLayoutParams(abbtx1);
        head.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutdatat = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutdatat.height = (int) (height * 83 / 100);
        layoutdatat.gravity = Gravity.CENTER;
        datalay.setLayoutParams(layoutdatat);

        LinearLayout.LayoutParams headLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        headLayout.width = width;
        headLayout.height = height * 7 / 100;
        headLayout.setMargins(0, width * 2 / 100, 0, 0);
        $TopLayout.setLayoutParams(headLayout);

        LinearLayout.LayoutParams headTextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        headTextParama.width = (int) (width * 100 / 3 / 100);
        headTextParama.height = height * 7 / 100;
        headTextParama.gravity = Gravity.CENTER;
        $Tab1.setLayoutParams(headTextParama);
        $Tab1.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams headText1Parama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        headText1Parama.width = (int) (width * 100 / 3 / 100);
        headText1Parama.height = height * 7 / 100;
        headText1Parama.gravity = Gravity.CENTER;
        headText1Parama.leftMargin = (int) (width * 0.9 / 100);
        $Tab2.setLayoutParams(headText1Parama);
        $Tab3.setLayoutParams(headText1Parama);
        $Tab2.setGravity(Gravity.CENTER);
        $Tab3.setGravity(Gravity.CENTER);

        if (width >= 600) {
            $Tab1.setTextSize(16);
            $Tab2.setTextSize(16);
            $Tab3.setTextSize(16);
        } else if (width > 501 && width < 600) {
            $Tab1.setTextSize(15);
            $Tab2.setTextSize(15);
            $Tab3.setTextSize(15);
        } else if (width > 260 && width < 500) {
            $Tab1.setTextSize(14);
            $Tab2.setTextSize(14);
            $Tab3.setTextSize(14);
        } else if (width <= 260) {
            $Tab1.setTextSize(13);
            $Tab2.setTextSize(13);
            $Tab3.setTextSize(13);
        }
    }
}
