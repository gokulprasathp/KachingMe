package com.wifin.kachingme.registration_and_login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.util.Constant;
import com.wifin.kaching.me.ui.R;


public class WelcomeActivity extends Activity {

    TextView mHeader, mCongratulation, mDescription, mStartMesasage;
    View mBottomView;
    ImageView mThumbImage;
    ProgressDialog progressDialog;
    int height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        inialize();
        screenArrange();
        mDescription.setText(Html.fromHtml("You have earned your first <font color='#ff0000'>10,000 BuxS</font>" +
                " for Registering and Downloading KaChing.me App"));

        mStartMesasage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				Intent intent11 = new Intent(WelcomeActivity.this,
//						Contact_Re_Sync_Service.class);
//				startService(intent11);

                progressDialog = ProgressDialog.show(WelcomeActivity.this,
                        getResources().getString(R.string.please_wait),
                        "Contact Syncing", false);
                progressDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        progressDialog.dismiss();
						Intent i = new Intent(WelcomeActivity.this,
								SliderTesting.class);
						startActivity(i);
						finish();

                    }
                }, 3000);
            }
        });
    }

    private void inialize() {
        // TODO Auto-generated method stub
        mCongratulation = (TextView) findViewById(R.id.welc_txt1);
        mHeader = (TextView) findViewById(R.id.welcome_headtext);
        mDescription = (TextView) findViewById(R.id.welc_txt2);
        mStartMesasage = (TextView) findViewById(R.id.wel_btn);
        mThumbImage = (ImageView) findViewById(R.id.wel_logo);
        mBottomView=(View)findViewById(R.id.welcome_bottomView);

        Constant.typeFace(this, mCongratulation);
        Constant.typeFace(this, mHeader);
        Constant.typeFace(this, mDescription);
        Constant.typeFace(this, mStartMesasage);

    }

    private void screenArrange() {
        // TODO Auto-generated method stub
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        Constant.screenHeight = height;
        Constant.screenWidth = width;

        LinearLayout.LayoutParams headerParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        headerParama.width = width;
        headerParama.height = height * 7 / 100;
        mHeader.setLayoutParams(headerParama);
        mHeader.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams thumImageParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        thumImageParama.width = width * 50 / 100;
        thumImageParama.height = height * 20 / 100;
        thumImageParama.gravity = Gravity.CENTER;
        thumImageParama.topMargin = height * 5 / 100;
        mThumbImage.setLayoutParams(thumImageParama);

        LinearLayout.LayoutParams congratulationParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        congratulationParama.gravity = Gravity.CENTER;
        congratulationParama.topMargin = height * 5 / 100;
        mCongratulation.setLayoutParams(congratulationParama);
        mCongratulation.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams descriptionParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        descriptionParama.width=width*70/100;
        descriptionParama.topMargin=height*2/100;
        descriptionParama.gravity = Gravity.CENTER;
        mDescription.setLayoutParams(descriptionParama);
        mDescription.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams startMsgParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        startMsgParama.width = width * 40 / 100;
        startMsgParama.height = height * 6 / 100;
        startMsgParama.gravity = Gravity.CENTER;
        startMsgParama.topMargin = height * 5 / 100;
        mStartMesasage.setLayoutParams(startMsgParama);
        mStartMesasage.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams bottomViewParama = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomViewParama.height = (int) (height * 0.6 / 100);
        bottomViewParama.gravity = Gravity.CENTER | Gravity.BOTTOM;
        mBottomView.setLayoutParams(bottomViewParama);

        if (width >= 600) {
            mHeader.setTextSize(19);
            mCongratulation.setTextSize(16);
            mDescription.setTextSize(16);
            mStartMesasage.setTextSize(16);
        } else if (width > 501 && width < 600) {
            mHeader.setTextSize(18);
            mCongratulation.setTextSize(15);
            mDescription.setTextSize(15);
            mStartMesasage.setTextSize(15);
        } else if (width > 260 && width < 500) {
            mHeader.setTextSize(17);
            mCongratulation.setTextSize(14);
            mDescription.setTextSize(14);
            mStartMesasage.setTextSize(14);
        } else if (width <= 260) {
            mHeader.setTextSize(16);
            mCongratulation.setTextSize(13);
            mDescription.setTextSize(13);
            mStartMesasage.setTextSize(13);
        }
    }
}
