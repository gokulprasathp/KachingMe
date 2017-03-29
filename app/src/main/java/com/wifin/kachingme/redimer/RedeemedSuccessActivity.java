package com.wifin.kachingme.redimer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.util.Constant;

/**
 * Created by user on 10/12/2016.
 */
public class RedeemedSuccessActivity extends Activity implements View.OnClickListener {
    LinearLayout mRedeemerIdLayout, mCustomerCareNumLayout;
    TextView mCongratulationText, mSuccessText, mDescriptionText,
            mRedeemerIDLabel, mRedeemerIDValue, mCustomerCareNumLabel,
            mCustomerCareNumValue;
    ImageView mSuccessImg, logo;
    Button mOkayBtn;
    View mTopView, mBottomView, mFooterBottomView;
    int height = 0;
    int width = 0;
    Dbhelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
//		View.inflate(this, R.layout.activity_redeemed_success, vg);
        setContentView(R.layout.activity_redeemed_success);
        db = new Dbhelper(getApplicationContext());
        initilizeVariable();
        screenArrangement();
        mRedeemerIDValue.setText(Constant.mFreebieShopId);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String status = extras.getString("is_success");
            String value = extras.getString("failure_msg");
            Constant.printMsg("status status ::>>>> " + status);
            if (status.equalsIgnoreCase("Success")) {
//				value = "Redeemed your Freebie";

                mDescriptionText.setText(value);
                String query = "DELETE FROM " + Dbhelper.TABLE_CART
                        + " WHERE deelid = '" + Constant.mFreebieId + "'";
                Constant.printMsg("delete query ::>>>> " + query);
                delete(query);
                String query1 = "DELETE FROM " + Dbhelper.TABLE_FREEBIE;
                Constant.printMsg("delete query1 ::>>>> " + query1);

                delete(query1);
                // mImgView.setBackgroundResource(R.drawable.freepee_success);
                mSuccessImg.setImageDrawable(getResources().getDrawable(
                        R.drawable.success));
            } else if (status.equalsIgnoreCase("Failure")) {
                mCongratulationText.setText("Oops!");
//				value = "your FreeBee has been expired";
                mDescriptionText.setText(value);
                mSuccessText.setVisibility(View.GONE);
                String query = "DELETE FROM " + Dbhelper.TABLE_CART
                        + " WHERE deelid = '" + Constant.mFreebieId + "'";
                Constant.printMsg("delete query ::>>>> " + query);
                delete(query);
                String query1 = "DELETE FROM " + Dbhelper.TABLE_FREEBIE;
                Constant.printMsg("delete query1 ::>>>> " + query1);

                // mImgView.setBackgroundResource(R.drawable.freepee_failure);
                mSuccessImg.setImageDrawable(getResources().getDrawable(
                        R.drawable.un_success));
            }
        }
    }

    private void initilizeVariable() {
        mRedeemerIdLayout = (LinearLayout) findViewById(R.id.redeemer_id_layout);
        mCustomerCareNumLayout = (LinearLayout) findViewById(R.id.customercare_num_layout);
        logo = (ImageView) findViewById(R.id.redeem_top_logo);
        mCongratulationText = (TextView) findViewById(R.id.congratulation_text);
        mSuccessText = (TextView) findViewById(R.id.successfully_text);
        mDescriptionText = (TextView) findViewById(R.id.description_text);
        mSuccessImg = (ImageView) findViewById(R.id.success_img);
        mOkayBtn = (Button) findViewById(R.id.okay_btn);
        mTopView = (View) findViewById(R.id.success_top_view);
        mBottomView = (View) findViewById(R.id.success_bottom_view);
        mFooterBottomView = (View) findViewById(R.id.success_footerBottomView);
        mRedeemerIDLabel = (TextView) findViewById(R.id.redeemer_id_label);
        mRedeemerIDValue = (TextView) findViewById(R.id.redeemer_id_value);
        mCustomerCareNumLabel = (TextView) findViewById(R.id.customercare_num_label);
        mCustomerCareNumValue = (TextView) findViewById(R.id.customercare_num_value);

        Constant.typeFace(this, mCongratulationText);
        Constant.typeFace(this, mSuccessText);
        Constant.typeFace(this, mDescriptionText);
        Constant.typeFace(this, mRedeemerIDLabel);
        Constant.typeFace(this, mRedeemerIDValue);
        Constant.typeFace(this, mCustomerCareNumLabel);
        Constant.typeFace(this, mCustomerCareNumValue);

        mOkayBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okay_btn:
                startActivity(new Intent(RedeemedSuccessActivity.this, SliderTesting.class));
                finish();
                break;
        }

    }

    private void delete(String query) {
        // TODO Auto-generated method stub
        Cursor c = null;

        try {
            c = db.open().getDatabaseObj().rawQuery(query, null);
            Constant.printMsg("No of deleted rows ::::::::::" + c.getCount());
        } catch (SQLException e) {

        } finally {
            if (c != null) {
                c.close();
            }

            db.close();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RedeemedSuccessActivity.this, SliderTesting.class));
        finish();
    }

    private void screenArrangement() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        LinearLayout.LayoutParams logolay = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        logolay.width = width * 40 / 100;
        logolay.height = height * 11 / 2 / 100;
        logolay.gravity = Gravity.CENTER | Gravity.TOP;
        logolay.setMargins(width * 1 / 100, height * 3 / 100, width * 1 / 100, height * 1 / 100);
        logo.setLayoutParams(logolay);

        LinearLayout.LayoutParams successImgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        successImgParams.gravity = Gravity.CENTER;
        successImgParams.width = width * 35 / 100;
        successImgParams.height = width * 35 / 100;
        successImgParams.topMargin = width * 10 / 100;
        successImgParams.bottomMargin = width * 10 / 100;
        mSuccessImg.setLayoutParams(successImgParams);

        LinearLayout.LayoutParams successTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        successTextParams.gravity = Gravity.CENTER;
        successTextParams.width = width * 90 / 100;
        mCongratulationText.setLayoutParams(successTextParams);
        mSuccessText.setLayoutParams(successTextParams);
        mDescriptionText.setLayoutParams(successTextParams);
        mCongratulationText.setGravity(Gravity.CENTER);
        mSuccessText.setGravity(Gravity.CENTER);
        mDescriptionText.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams successViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        successViewParams.gravity = Gravity.CENTER;
        successViewParams.width = width;
        successViewParams.height = (int) (width * 0.5 / 100);
        successViewParams.topMargin = width * 10 / 100;
        mTopView.setLayoutParams(successViewParams);

        LinearLayout.LayoutParams successbottomViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        successbottomViewParams.gravity = Gravity.CENTER;
        successbottomViewParams.width = width;
        successbottomViewParams.height = (int) (width * 0.5 / 100);
        successbottomViewParams.bottomMargin = width * 10 / 100;
        successbottomViewParams.topMargin = width * 5 / 100;
        mBottomView.setLayoutParams(successbottomViewParams);

        LinearLayout.LayoutParams redeemerIdParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        redeemerIdParams.gravity = Gravity.CENTER;
        redeemerIdParams.width = width;
        redeemerIdParams.topMargin = width * 5 / 100;
        mRedeemerIdLayout.setLayoutParams(redeemerIdParams);
        mCustomerCareNumLayout.setLayoutParams(redeemerIdParams);

        LinearLayout.LayoutParams redeemerBtnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        redeemerBtnParams.gravity = Gravity.CENTER;
        redeemerBtnParams.height = height * 7 / 100;
        redeemerBtnParams.width = width * 35 / 100;
        redeemerBtnParams.topMargin = width * 4 / 100;
        redeemerBtnParams.bottomMargin = width * 15 / 100;
        mOkayBtn.setGravity(Gravity.CENTER);
        mOkayBtn.setLayoutParams(redeemerBtnParams);

        LinearLayout.LayoutParams redeemerIDtextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        redeemerIDtextParams.gravity = Gravity.CENTER;
        redeemerIDtextParams.width = width * 55 / 100;
        redeemerIDtextParams.leftMargin = width * 5 / 100;
        mRedeemerIDLabel.setLayoutParams(redeemerIDtextParams);
        // mRedeemerIDValue.setLayoutParams(redeemerIDtextParams);
        mCustomerCareNumLabel.setLayoutParams(redeemerIDtextParams);
        // mCustomerCareNumValue.setLayoutParams(redeemerIDtextParams);

        FrameLayout.LayoutParams bottomViewParama = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomViewParama.height = (int) (height * 0.6 / 100);
        bottomViewParama.gravity = Gravity.CENTER | Gravity.BOTTOM;
        mFooterBottomView.setLayoutParams(bottomViewParama);

        if (width >= 600) {
            mCongratulationText.setTextSize(16);
            mSuccessText.setTextSize(16);
            mDescriptionText.setTextSize(16);
            mRedeemerIDLabel.setTextSize(16);
            mRedeemerIDValue.setTextSize(16);
            mCustomerCareNumLabel.setTextSize(16);
            mCustomerCareNumValue.setTextSize(16);

        } else if (width < 600 && width >= 480) {
            mCongratulationText.setTextSize(15);
            mSuccessText.setTextSize(15);
            mDescriptionText.setTextSize(15);
            mRedeemerIDValue.setTextSize(15);
            mCustomerCareNumLabel.setTextSize(15);
            mCustomerCareNumValue.setTextSize(15);

        } else if (width < 480 && width >= 320) {
            mCongratulationText.setTextSize(12);
            mSuccessText.setTextSize(12);
            mDescriptionText.setTextSize(12);
            mRedeemerIDValue.setTextSize(12);
            mCustomerCareNumLabel.setTextSize(12);
            mCustomerCareNumValue.setTextSize(12);

        } else if (width < 320) {
            mCongratulationText.setTextSize(10);
            mSuccessText.setTextSize(10);
            mDescriptionText.setTextSize(10);
            mRedeemerIDValue.setTextSize(10);
            mCustomerCareNumLabel.setTextSize(10);
            mCustomerCareNumValue.setTextSize(10);

        }
    }
}
