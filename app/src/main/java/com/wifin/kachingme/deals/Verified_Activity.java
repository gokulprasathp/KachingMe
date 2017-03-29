/*
* @author Gokul
*
*
*
*
* */

package com.wifin.kachingme.deals;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.util.Constant;

public class Verified_Activity extends MainActivity {

    ImageView mImgView;
    TextView mDescriptionText, mredeemerid, mcall, mcallvalue,
            mredeemeridvalue;
    Button mOkayBtn;
    int height, width = 0;
    Dbhelper db;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
        View.inflate(this, R.layout.activity_verified, vg);
        sideMenufoot.setVisibility(LinearLayout.GONE);
        logo.setVisibility(ImageView.GONE);
        db = new Dbhelper(getApplicationContext());
        initialization();
        screenArrange();
        mcallvalue.setPaintFlags(mcallvalue.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);
        number = " +91 - 9884888178";

        mredeemeridvalue.setText(Constant.mFreebieShopId);

        mcallvalue.setText(number);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String status = extras.getString("is_success");
            String value = extras.getString("failure_msg");
            Constant.printMsg("status status ::>>>> " + status);
            if (status.equalsIgnoreCase("Success")) {
                mDescriptionText.setText(value);
                String query = "DELETE FROM " + Dbhelper.TABLE_CART
                        + " WHERE deelid = '" + Constant.mFreebieId + "'";
                Constant.printMsg("delete query ::>>>> " + query);
                delete(query);
                String query1 = "DELETE FROM " + Dbhelper.TABLE_FREEBIE;
                Constant.printMsg("delete query1 ::>>>> " + query1);

                delete(query1);
                mImgView.setImageDrawable(getResources().getDrawable(
                        R.drawable.freepee_success));
            } else if (status.equalsIgnoreCase("Failure")) {
                mDescriptionText.setText(value);

                String query = "DELETE FROM " + Dbhelper.TABLE_CART
                        + " WHERE deelid = '" + Constant.mFreebieId + "'";
                Constant.printMsg("delete query ::>>>> " + query);
                delete(query);
                String query1 = "DELETE FROM " + Dbhelper.TABLE_FREEBIE;
                Constant.printMsg("delete query1 ::>>>> " + query1);
                mImgView.setImageDrawable(getResources().getDrawable(
                        R.drawable.freepee_failure));

            }

        }

        mOkayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(Verified_Activity.this,
                        SliderTesting.class);
                startActivity(intent);
                finish();

            }
        });
        mcallvalue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);

            }
        });

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

    private void screenArrange() {
        // TODO Auto-generated method stub
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;
        Constant.screenWidth = width;
        Constant.screenHeight = height;

        LinearLayout.LayoutParams Textparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Textparams.width = width * 80 / 100;

        Textparams.topMargin = width * 5 / 100;
        Textparams.leftMargin = width * 10 / 100;
        mDescriptionText.setLayoutParams(Textparams);
        mcallvalue.setLayoutParams(Textparams);
        mcall.setLayoutParams(Textparams);
        LinearLayout.LayoutParams Textparams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Textparams1.width = width * 45 / 100;

        Textparams1.topMargin = width * 5 / 100;
        Textparams1.leftMargin = width * 10 / 100;

        mredeemeridvalue.setLayoutParams(Textparams1);

        mredeemerid.setLayoutParams(Textparams1);

        LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        imgparams.width = width * 40 / 100;
        imgparams.height = height * 20 / 100;
        imgparams.topMargin = width * 5 / 100;
        // imgparams.leftMargin = width * 15 / 100;
        imgparams.gravity = Gravity.CENTER;
        mImgView.setLayoutParams(imgparams);

        LinearLayout.LayoutParams btnparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnparams.width = width * 35 / 100;
        btnparams.height = height * 7 / 100;
        btnparams.topMargin = width * 10 / 100;
        btnparams.leftMargin = (int) (width * 32.5 / 100);
        mOkayBtn.setLayoutParams(btnparams);

        LinearLayout.LayoutParams layoutdatat = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutdatat.height = (int) (height * 83 / 100);
        layoutdatat.gravity = Gravity.CENTER;
        datalay.setLayoutParams(layoutdatat);
        if (width >= 600) {

            mDescriptionText.setTextSize(18);
            mcall.setTextSize(18);
            mredeemerid.setTextSize(23);
            mOkayBtn.setTextSize(18);
            mredeemeridvalue.setTextSize(26);
            mcallvalue.setTextSize(18);

        } else if (width > 501 && width < 600) {
            mDescriptionText.setTextSize(17);
            mcall.setTextSize(17);
            mredeemerid.setTextSize(22);
            mOkayBtn.setTextSize(17);
            mredeemeridvalue.setTextSize(25);
            mcallvalue.setTextSize(17);

        } else if (width > 260 && width < 500) {
            mDescriptionText.setTextSize(16);
            mOkayBtn.setTextSize(16);
            mcall.setTextSize(16);
            mredeemerid.setTextSize(21);
            mredeemeridvalue.setTextSize(24);
            mcallvalue.setTextSize(16);
        } else if (width <= 260) {
            mDescriptionText.setTextSize(16);
            mOkayBtn.setTextSize(16);
            mcall.setTextSize(16);
            mredeemerid.setTextSize(21);
            mredeemeridvalue.setTextSize(24);
            mcallvalue.setTextSize(16);
        }
    }

    private void initialization() {
        // TODO Auto-generated method stub
        mDescriptionText = (TextView) findViewById(R.id.description_txt);
        mImgView = (ImageView) findViewById(R.id.imgview);
        mOkayBtn = (Button) findViewById(R.id.okay_btn);
        mredeemerid = (TextView) findViewById(R.id.redeem_id);
        mcall = (TextView) findViewById(R.id.customer_call);
        mredeemeridvalue = (TextView) findViewById(R.id.redeem_idvalue);
        mcallvalue = (TextView) findViewById(R.id.customer_callvalue);

        try {
            Constant.typeFace(this, mDescriptionText);
            Constant.typeFace(this, mOkayBtn);
            Constant.typeFace(this, mredeemerid);
            Constant.typeFace(this, mcall);
            Constant.typeFace(this, mredeemeridvalue);
            Constant.typeFace(this, mcallvalue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
