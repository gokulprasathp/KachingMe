/*
* @author Gokul
*
* @usage -  This class is used to display Headder menu for all the screens
*
*
* */

package com.wifin.kachingme.chat_home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.util.Constant;


public class HeaderActivity extends AppCompatActivity {
    public LinearLayout mMainLayout, mContentLayout, mHeaderLayout, mFooterLayout,
            mChatLayout, mBuxLayout, mCartLayout, mHeaderTextLayout;
    public ImageView mBackBtn, mHeaderImg, mNextBtn, mChatImg, mBuxSImg, mCartImg;
    public TextView mHeading, mChatText, mBuxText, mCartText;
    public View mFooterView;
    int height = 0;
    int width = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);
        initialization();
        screenArrangement();
    }

    private void initialization() {
        mMainLayout = (LinearLayout) findViewById(R.id.main_layout);
        mContentLayout = (LinearLayout) findViewById(R.id.content_layout);
        mHeaderLayout = (LinearLayout) findViewById(R.id.header_layout);
        mFooterLayout = (LinearLayout) findViewById(R.id.footer_layout);
        mChatLayout = (LinearLayout) findViewById(R.id.chat_layout);
        mBuxLayout = (LinearLayout) findViewById(R.id.bux_layout);
        mCartLayout = (LinearLayout) findViewById(R.id.cart_layout);
        mHeaderTextLayout = (LinearLayout) findViewById(R.id.header_text_layout);

        mBackBtn = (ImageView) findViewById(R.id.back_btn);
        mHeaderImg = (ImageView) findViewById(R.id.header_img);
        mNextBtn = (ImageView) findViewById(R.id.next_btn);
        mChatImg = (ImageView) findViewById(R.id.chat_img);
        mBuxSImg = (ImageView) findViewById(R.id.buxs_img);
        mCartImg = (ImageView) findViewById(R.id.cart_img);

        mHeading = (TextView) findViewById(R.id.heading);
        mChatText = (TextView) findViewById(R.id.chat_text);
        mBuxText = (TextView) findViewById(R.id.bux_text);
        mCartText = (TextView) findViewById(R.id.cart_text);

        mFooterView = (View) findViewById(R.id.footer_view);

        Constant.typeFace(this, mHeading);
        Constant.typeFace(this, mChatText);
        Constant.typeFace(this, mBuxText);
        Constant.typeFace(this, mCartText);
    }

    private void screenArrangement() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mainLayoutParams.width = width;
        mainLayoutParams.height = height * 9 / 100;
        mainLayoutParams.gravity = Gravity.CENTER;
        mHeaderLayout.setLayoutParams(mainLayoutParams);
        mFooterLayout.setLayoutParams(mainLayoutParams);

        LinearLayout.LayoutParams backbtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        backbtnParams.width = width * 6 / 100;
        backbtnParams.height = width * 6 / 100;
        backbtnParams.leftMargin = width * 5 / 100;
        backbtnParams.gravity = Gravity.CENTER;
        mBackBtn.setLayoutParams(backbtnParams);
        mNextBtn.setLayoutParams(backbtnParams);

        LinearLayout.LayoutParams headtextimgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        headtextimgParams.width = width * 8 / 100;
        headtextimgParams.height = width * 8 / 100;
        // headtextimgParams.gravity = Gravity.CENTER;
        mHeaderImg.setLayoutParams(headtextimgParams);

        LinearLayout.LayoutParams headtextlayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        headtextlayoutParams.width = width * 68 / 100;
        headtextlayoutParams.height = width * 10 / 100;
        headtextlayoutParams.leftMargin = width * 5 / 100;
        headtextlayoutParams.gravity = Gravity.CENTER;
        mHeaderTextLayout.setLayoutParams(headtextlayoutParams);


        LinearLayout.LayoutParams contentLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        contentLayoutParams.width = width;
        contentLayoutParams.height = height * 75 / 100;
        contentLayoutParams.gravity = Gravity.CENTER;

        LinearLayout.LayoutParams bottomViewParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomViewParama.height = (int) (height * 0.7 / 100);
        bottomViewParama.gravity = Gravity.CENTER;
        mFooterView.setLayoutParams(bottomViewParama);

        LinearLayout.LayoutParams mBottomLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomLayoutParama.width = width;
        mBottomLayoutParama.height = height * 9 / 100;
        mFooterLayout.setLayoutParams(mBottomLayoutParama);

        LinearLayout.LayoutParams mBottomChatLayoutParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomChatLayoutParama.height = height * 9 / 100;
        mBottomChatLayoutParama.weight = 1;
        mChatLayout.setLayoutParams(mBottomChatLayoutParama);
        mCartLayout.setLayoutParams(mBottomChatLayoutParama);
        mBuxLayout.setLayoutParams(mBottomChatLayoutParama);

        LinearLayout.LayoutParams mBottomImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomImageParama.width = (int) width * 10 / 100;
        mBottomImageParama.height = (int) height * 4 / 100;
        mBottomImageParama.topMargin = height * 1 / 100;
        mBottomImageParama.bottomMargin = (int) (height * 0.5 / 100);
        mBottomImageParama.gravity = Gravity.CENTER | Gravity.TOP;
        mChatImg.setLayoutParams(mBottomImageParama);
        mCartImg.setLayoutParams(mBottomImageParama);
        mBuxSImg.setLayoutParams(mBottomImageParama);

        LinearLayout.LayoutParams mBottomTextParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //mBottomTextParama.height = (int)height * 3/ 100;
        mBottomTextParama.gravity = Gravity.CENTER | Gravity.TOP;
        mCartText.setLayoutParams(mBottomTextParama);
        mChatText.setLayoutParams(mBottomTextParama);
        mBuxText.setLayoutParams(mBottomTextParama);
        mCartText.setGravity(Gravity.CENTER | Gravity.TOP);
        mChatText.setGravity(Gravity.CENTER | Gravity.TOP);
        mBuxText.setGravity(Gravity.CENTER | Gravity.TOP);


        if (width >= 600) {
            mHeading.setTextSize(18);
            mChatText.setTextSize(15);
            mBuxText.setTextSize(15);
            mCartText.setTextSize(15);
        } else if (width < 600 && width >= 480) {
            mHeading.setTextSize(17);
            mChatText.setTextSize(14);
            mBuxText.setTextSize(14);
            mCartText.setTextSize(14);
        } else if (width < 480 && width >= 320) {
            mHeading.setTextSize(14);
            mChatText.setTextSize(12);
            mBuxText.setTextSize(12);
            mCartText.setTextSize(12);

        } else if (width < 320) {
            mHeading.setTextSize(12);
            mChatText.setTextSize(10);
            mBuxText.setTextSize(10);
            mCartText.setTextSize(10);
        }
    }

}
